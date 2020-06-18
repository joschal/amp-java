package de.joschal.amp.performance;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.Route;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.logic.utils.Dijkstra;
import de.joschal.amp.sim.core.logic.utils.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class RandomActivity {

    @Test
    void average() {

        // How many tests are run
        int cycles = 1000;

        // how many messages are send per run
        int messages = 200;

        // list of accumulated loggers
        LinkedList<RouteQualityLogger> routeQualityLoggers = new LinkedList<>();

        // run tests
        for (int i = 0; i < cycles; i++) {
            routeQualityLoggers.add(randomActivity(messages));
        }

        // preprare logged data for csv export
        RouteQualityLogger rootLogger = new RouteQualityLogger();
        rootLogger.rounds = messages;
        for (int i = 0; i < messages; i++) {
            int totalHopsOfKnownRoutesOverTimeSum = 0;
            int differenceToOptimalRouteoverTimeSum = 0;
            double averageOfRoutesPerNodeOverTimeSum = 0;
            double qualityOverTimeSum = 0;
            for (RouteQualityLogger logger : routeQualityLoggers) {
                totalHopsOfKnownRoutesOverTimeSum += logger.totalHopsOfKnownRoutesOverTime.get(i);
                differenceToOptimalRouteoverTimeSum += logger.differenceToOptimalRouteoverTime.get(i);
                averageOfRoutesPerNodeOverTimeSum += logger.averageOfRoutesPerNodeOverTime.get(i);
                qualityOverTimeSum += logger.qualityOverTime.get(i);
            }
            rootLogger.totalHopsOfKnownRoutesOverTime.add(totalHopsOfKnownRoutesOverTimeSum / messages);
            rootLogger.differenceToOptimalRouteoverTime.add(differenceToOptimalRouteoverTimeSum / messages);
            rootLogger.averageOfRoutesPerNodeOverTime.add(averageOfRoutesPerNodeOverTimeSum / messages);
            rootLogger.qualityOverTime.add(qualityOverTimeSum / messages);
        }

        // print results
        System.out.println("Root Logger \n\n\n");
        System.out.println(rootLogger.toString());

    }

    RouteQualityLogger randomActivity(int cycles) {

        int nodeCount = 32;
        Graph graph = NetworkUtil.getRandomBootedGraph(nodeCount);

        RouteQualityLogger routeQualityLogger = new RouteQualityLogger();

        for (int i = 1; i <= cycles; i++) {

            // randomly select source and destination node
            String sourceId = "";
            String destinationId = "";
            while (sourceId.equals(destinationId)) {
                sourceId = NetworkUtil.getRandomNodeId(nodeCount);
                destinationId = NetworkUtil.getRandomNodeId(nodeCount);
            }
            SimpleNode source = (SimpleNode) graph.getNodebyId(sourceId);
            SimpleNode destination = (SimpleNode) graph.getNodebyId(destinationId);
            log.info("Will send message from {} to {}", sourceId, destinationId);

            // Prepare datagram with random payload
            String payload = UUID.randomUUID().toString();
            source.sendDatagram(payload, destination.getAddress());

            // Network ticks until datagram was received or timeout happens
            for (int ticks = 0; ticks <= nodeCount * 10; ticks++) {

                // log data
                routeQualityLogger = compareToDijsktra(graph, routeQualityLogger);

                // Check if the datagram was delivered
                if (destination.getReceivedMessage().equals(payload)) {
                    // if delievred, end loop
                    log.debug("{} recived message from {} after {} ticks", destinationId, sourceId, ticks);
                    break;
                } else {
                    // Check for timeout, fail test iof reached
                    assertNotEquals(nodeCount * 10, ticks, "Message was not received in " + ticks + " ticks");
                    // If the datagram was not delivered, log the current state and to network tick
                    Scheduler.tick(graph);
                }
            }
            // When datagram was successfully delivered, reset logger
            routeQualityLogger.roundFinished();
        }

        return routeQualityLogger;
    }

    /**
     * Compares the currently known routes to the optimal ones, computed by Dijkstra's algorithm
     *
     * @param graph              Graph to analyze. Contains the nodes and their respective routing tables at a certain point in time
     * @param routeQualityLogger RouteQualityLogger to store the computed data
     * @return RouteQualityLogger with the added data from this tick
     */
    private static RouteQualityLogger compareToDijsktra(Graph graph, RouteQualityLogger routeQualityLogger) {

        // this adds high comlexity to the test
        // should be fine though
        for (AbstractNode node : graph.getNodes().values()) {
            HashMap<String, Dijkstra.DistanceVector> dijkstra = Dijkstra.dijkstra(graph, node);

            int totelHopsStoredByCurrentNode = 0;
            int differenceToOptimalRoutesInCurrentNode = 0;

            for (Route route : node.getRouter().getRoutingTable()) {
                totelHopsStoredByCurrentNode += route.hops;

                for (Dijkstra.DistanceVector distanceVector : dijkstra.values()) {
                    if (distanceVector.getNode().getAddress().equals(route.address)) {
                        int difference = route.hops - distanceVector.getDistance();
                        differenceToOptimalRoutesInCurrentNode += difference;
                    }
                }

            }

            routeQualityLogger.tick(totelHopsStoredByCurrentNode, differenceToOptimalRoutesInCurrentNode, node.getRouter().getRoutingTable().size());
        }
        return routeQualityLogger;
    }


    class RouteQualityLogger {
        private int rounds = 0; // count of delivered datagrams
        private int ticks = 0; // ticks within the delivery of a datagram
        private int totalHopsOfKnownRoutes = 0; // accumulated hops of all known routes of all nodes during the delivery of one datagram
        private int differenceToOptimalRoutes = 0; // accumulated difference to the optimal routes during the delivery of one datagram
        private int countOfRoutesPerNode = 0; //

        // The lists log the data over miltiple rounds of datagram delivery
        private LinkedList<Integer> totalHopsOfKnownRoutesOverTime = new LinkedList<>();
        private LinkedList<Integer> differenceToOptimalRouteoverTime = new LinkedList<>();
        private LinkedList<Double> averageOfRoutesPerNodeOverTime = new LinkedList<>();
        private LinkedList<Double> qualityOverTime = new LinkedList<>();

        /**
         * One network tick during the delivery of a datagram
         *
         * @param totalHops                 sum of hops of all routes known to a node
         * @param differenceToOptimalRoutes difference ot the known routes to the optimal ones of the current node
         * @param countOfRoutesPerNode      Number of known routes to the node
         */
        public void tick(int totalHops, int differenceToOptimalRoutes, int countOfRoutesPerNode) {
            this.ticks++;
            this.totalHopsOfKnownRoutes += totalHops;
            this.differenceToOptimalRoutes += differenceToOptimalRoutes;
            this.countOfRoutesPerNode += countOfRoutesPerNode;
        }

        /**
         * Datagram was delievred sucesfully. Data is logged and logger is reset for the next round
         */
        public void roundFinished() {
            this.totalHopsOfKnownRoutesOverTime.add(this.totalHopsOfKnownRoutes);
            this.differenceToOptimalRouteoverTime.add(this.differenceToOptimalRoutes);
            this.qualityOverTime.add((double) this.differenceToOptimalRoutes / this.totalHopsOfKnownRoutes);
            this.averageOfRoutesPerNodeOverTime.add((double) this.countOfRoutesPerNode / ticks);
            this.ticks = 0;
            this.totalHopsOfKnownRoutes = 0;
            this.differenceToOptimalRoutes = 0;
            this.countOfRoutesPerNode = 0;
            this.rounds++;
        }

        /**
         * Formats the logged data for external processing
         *
         * @return formatted csv String
         */
        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(8);

            sb.append("count;Hops of known routes;difference to optimal routes;average counts of routes per node;overall route quality\n");

            for (int i = 0; i < rounds; i++) {
                sb.
                        append(i).
                        append(";").
                        append(this.totalHopsOfKnownRoutesOverTime.get(i)).
                        append(";").
                        append(this.differenceToOptimalRouteoverTime.get(i)).
                        append(";").
                        append(df.format(this.averageOfRoutesPerNodeOverTime.get(i))).
                        append(";").
                        append(df.format(this.qualityOverTime.get(i))).
                        append("\n");
            }
            return sb.toString();
        }
    }
}

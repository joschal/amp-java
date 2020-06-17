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
        int cycles = 1000;
        int messages = 200;

        LinkedList<RouteQualityLogger> routeQualityLoggers = new LinkedList<>();

        for (int i = 0; i < cycles; i++) {
            routeQualityLoggers.add(randomActivity(messages));
        }

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

        System.out.println("Root Logger \n\n\n");

        System.out.println(rootLogger.toString());

    }

    RouteQualityLogger randomActivity(int cycles) {

        int nodeCount = 32;
        Graph graph = NetworkUtil.getRandomBootedGraph(nodeCount);

        RouteQualityLogger routeQualityLogger = new RouteQualityLogger();

        for (int i = 1; i <= cycles; i++) {

            String sourceId = "";
            String destinationId = "";

            while (sourceId.equals(destinationId)) {
                sourceId = NetworkUtil.getRandomNodeId(nodeCount);
                destinationId = NetworkUtil.getRandomNodeId(nodeCount);
            }

            SimpleNode source = (SimpleNode) graph.getNodebyId(sourceId);
            SimpleNode destination = (SimpleNode) graph.getNodebyId(destinationId);

            log.info("Will send message from {} to {}", sourceId, destinationId);

            String payload = UUID.randomUUID().toString();
            source.sendDatagram(payload, destination.getAddress());

            for (int ticks = 0; ticks <= nodeCount * 10; ticks++) {
                if (destination.getReceivedMessage() != null &&
                        destination.getReceivedMessage().equals(payload)) {
                    log.debug("{} recived message from {} after {} ticks", destinationId, sourceId, ticks);
                    break;
                } else {
                    assertNotEquals(nodeCount * 10, ticks, "Message was not received in " + ticks + " ticks");
                    Scheduler.tick(graph);
                }
                routeQualityLogger = compareToDijsktra(graph, routeQualityLogger);
            }


            routeQualityLogger.roundFinished();
        }

        //System.out.println("\n\n\n");
        //System.out.println(routeQualityLogger.toString());

        return routeQualityLogger;
    }

    private static void printRoutingTable(AbstractNode abstractNode) {

        for (Route route : abstractNode.getRouter().getRoutingTable()) {
            log.info("{} has route to {} with {} hops", abstractNode.getId(), route.address, route.hops);
        }

    }

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
        private int rounds = 0;
        private int ticks = 0;
        private int totalHopsOfKnownRoutes = 0;
        private int differenceToOptimalRoutes = 0;
        private int countOfRoutesPerNode = 0;
        private LinkedList<Integer> totalHopsOfKnownRoutesOverTime = new LinkedList<>();
        private LinkedList<Integer> differenceToOptimalRouteoverTime = new LinkedList<>();
        private LinkedList<Double> averageOfRoutesPerNodeOverTime = new LinkedList<>();
        private LinkedList<Double> qualityOverTime = new LinkedList<>();

        public void tick(int totalHops, int differenceToOptimalRoutes, int countOfRoutesPerNode) {
            this.ticks++;
            this.totalHopsOfKnownRoutes += totalHops;
            this.differenceToOptimalRoutes += differenceToOptimalRoutes;
            this.countOfRoutesPerNode += countOfRoutesPerNode;
        }

        public void roundFinished() {
            this.totalHopsOfKnownRoutesOverTime.add(this.totalHopsOfKnownRoutes);
            this.differenceToOptimalRouteoverTime.add(this.differenceToOptimalRoutes);
            this.qualityOverTime.add(differenceToOptimalRoutesInPercent());
            this.averageOfRoutesPerNodeOverTime.add((double) this.countOfRoutesPerNode / ticks);
            this.ticks = 0;
            this.totalHopsOfKnownRoutes = 0;
            this.differenceToOptimalRoutes = 0;
            this.countOfRoutesPerNode = 0;
            this.rounds++;
        }

        private double differenceToOptimalRoutesInPercent() {
            return (double) this.differenceToOptimalRoutes / this.totalHopsOfKnownRoutes;
        }

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

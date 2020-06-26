package de.joschal.amp.performance;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.routing.Route;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.io.DataLink;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.logic.utils.Dijkstra;
import de.joschal.amp.sim.core.logic.utils.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class RandomActivity {

    private static final int NODE_COUNT = 32;

    @Test
    void average() {

        // How many tests are run
        int runs = 500;

        // how many messages are send per run
        int messages = 250;

        // list of accumulated loggers
        LinkedList<RouteQualityLogger> routeQualityLoggers = new LinkedList<>();

        // run tests
        for (int i = 0; i < runs; i++) {
            routeQualityLoggers.add(randomActivity(messages));
            System.out.println("------------------ Test run " + i + " -------------------------");
        }

        // prepare logged data for csv export
        RouteQualityLogger rootLogger = new RouteQualityLogger();
        rootLogger.rounds = messages;
        for (int i = 0; i < messages; i++) {

            // local strage of accumulated data
            int totalHopsOfKnownRoutesOverTimeSum = 0;
            int differenceToOptimalRouteoverTimeSum = 0;
            double averageOfRoutesPerNodeOverTimeSum = 0;
            double qualityOverTimeSum = 0;
            double countOfAlreadyKnownRoutes = 0;

            // accumulate data from all loggers
            for (RouteQualityLogger logger : routeQualityLoggers) {
                totalHopsOfKnownRoutesOverTimeSum += logger.totalHopsOfKnownRoutesOverTime.get(i);
                differenceToOptimalRouteoverTimeSum += logger.differenceToOptimalRouteoverTime.get(i);
                averageOfRoutesPerNodeOverTimeSum += logger.averageOfRoutesPerNodeOverTime.get(i);
                qualityOverTimeSum += logger.qualityOverTime.get(i);
                if (logger.routeToDestinationWasAlreadyKnownOverTime.get(i)) {
                    countOfAlreadyKnownRoutes++;
                }
            }

            // store accumulated data in root loggers vectors
            rootLogger.totalHopsOfKnownRoutesOverTime.add(totalHopsOfKnownRoutesOverTimeSum / runs);
            rootLogger.differenceToOptimalRouteoverTime.add(differenceToOptimalRouteoverTimeSum / runs);
            rootLogger.averageOfRoutesPerNodeOverTime.add(averageOfRoutesPerNodeOverTimeSum / runs);
            rootLogger.qualityOverTime.add(1 - (qualityOverTimeSum / runs));
            rootLogger.routeToDestinationWasKnownAverage.add(countOfAlreadyKnownRoutes / messages);
        }

        for (RouteQualityLogger logger : routeQualityLoggers) {
            rootLogger.sendMessagesCounter += logger.sendMessagesCounter;
        }

        // print results
        System.out.println("Root Logger \n\n\n");
        System.out.println("Total messages " + rootLogger.sendMessagesCounter + " \n\n\n");
        System.out.println(rootLogger.toString());

    }

    RouteQualityLogger randomActivity(int messages) {

        Graph graph = NetworkUtil.getRandomBootedGraph(NODE_COUNT);

        RouteQualityLogger routeQualityLogger = new RouteQualityLogger();

        for (int i = 1; i <= messages; i++) {

            // randomly select source and destination node
            String sourceId = "";
            String destinationId = "";
            while (sourceId.equals(destinationId)) {
                sourceId = NetworkUtil.getRandomNodeId(NODE_COUNT);
                destinationId = NetworkUtil.getRandomNodeId(NODE_COUNT);
            }
            SimpleNode source = (SimpleNode) graph.getNodebyId(sourceId);
            SimpleNode destination = (SimpleNode) graph.getNodebyId(destinationId);
            log.info("Will send message from {} to {}", sourceId, destinationId);

            // Prepare datagram with random payload
            String payload = UUID.randomUUID().toString();
            source.sendDatagram(payload, destination.getAddress());

            // Log, if route discovery is needed
            boolean routeWasKnown = source.getRouter().getRoute(destination.getAddress()).isPresent();

            // Network ticks until datagram was received or timeout happens
            for (int ticks = 0; ticks <= NODE_COUNT * 10; ticks++) {

                // log data
                routeQualityLogger = compareToDijsktra(graph, routeQualityLogger);

                // Check if the datagram was delivered
                if (destination.getReceivedMessage().equals(payload)) {
                    // if delievred, end loop
                    log.debug("{} recived message from {} after {} ticks", destinationId, sourceId, ticks);
                    break;
                } else {
                    // Check for timeout, fail test iof reached
                    assertNotEquals(NODE_COUNT * 10, ticks, "Message was not received in " + ticks + " ticks");
                    // If the datagram was not delivered, log the current state and to network tick
                    Scheduler.tick(graph);
                }
            }
            // When datagram was successfully delivered, reset logger
            routeQualityLogger.roundFinished(routeWasKnown);
        }

        for (DataLink dataLink : graph.getEdges().values()) {
            routeQualityLogger.sendMessagesCounter += dataLink.getMessageCounter();
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
}

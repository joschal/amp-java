package de.joschal.amp.performance;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.DataLink;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.logic.utils.Dijkstra;
import de.joschal.amp.sim.core.logic.utils.Linker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class generates a random graph
 */
@Slf4j
public class GraphGenerator {

    /**
     * Generates random graph for testing
     *
     * @param nodeCount Count of nodes in the graph
     * @return Connected Graph
     */
    public static Graph getRandomGraph(int nodeCount) {

        Graph graph = new Graph();

        for (int i = 1; i <= nodeCount; i++) {
            AbstractNode node = new SimpleNode(String.valueOf(i));
            graph.addNode(node);
        }

        for (AbstractNode abstractNode : graph.getNodes().values()) {
            int random = getRandomSafely(nodeCount, abstractNode);

            DataLink dataLink = Linker.linkNodes(abstractNode, graph.getNodebyId(String.valueOf(random)));
            graph.addEdge(dataLink);

        }

        boolean runLinker = true;
        while (runLinker) {

            for (AbstractNode abstractNode : graph.getNodes().values()) {

                if (abstractNode.getNeighbours().size() < 2) {
                    int random = getRandomSafely(nodeCount, abstractNode);

                    DataLink dataLink = Linker.linkNodes(abstractNode, graph.getNodebyId(String.valueOf(random)));
                    graph.addEdge(dataLink);
                    break;
                }
                runLinker = false;
            }
        }

        // verify connectedness and generate a new graph if necessary
        while (!checkConnectednessWithDijsktra(graph)) {
            graph = getRandomGraph(nodeCount);
        }

        return graph;
    }

    private static int getRandomSafely(int nodeCount, AbstractNode abstractNode) {
        int random = (int) (Math.random() * nodeCount);
        while (true) {

            if (random != Integer.valueOf(abstractNode.getId())) {
                if (random > 0) {
                    break;
                }
            }
            random = (int) (Math.random() * nodeCount);
        }

        while (checkIfLinkAlreadyExists(abstractNode, random)) {

            random = getRandomSafely(nodeCount, abstractNode);
        }

        return random;
    }

    @Test
    void randomGraphTest() {

        int nodeCount = 100;
        Graph graph = getRandomGraph(100);

        assertEquals(nodeCount, graph.getNodes().size());
        assertTrue(graph.getEdges().size() >= graph.getNodes().size());

        for (AbstractNode node : graph.getNodes().values()) {
            assertNotEquals(0, node.getNetworkInterfaces().size());
            assertEquals(node.getNeighbours().size(), node.getNetworkInterfaces().size());
        }
    }

    private static boolean checkConnectednessWithDijsktra(Graph graph) {
        HashMap dijkstra = Dijkstra.dijkstra(new ArrayList(graph.getNodes().values()), graph.getNodebyId("1"));

        for (Iterator it = dijkstra.values().iterator(); it.hasNext(); ) {
            Dijkstra.DistanceVector distanceVector = (Dijkstra.DistanceVector) it.next();

            if (distanceVector.getDistance() == Integer.MAX_VALUE) {
                log.info("Graph is disconnected. Will need to generate a new one");
                return false;
            }
        }
        return true;
    }

    private static boolean checkIfLinkAlreadyExists(AbstractNode node, int random) {
        for (AbstractNode neighbour : node.getNeighbours()) {
            if (random == Integer.valueOf(neighbour.getId())) {
                return true;
            }
        }
        return false;
    }

}

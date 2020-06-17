package de.joschal.amp.performance;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.logic.utils.Scheduler;
import de.joschal.amp.sim.outbound.GraphWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NetworkUtil {

    public static Graph getRandomBootedGraph(int nodeCount) {

        Graph graph = GraphGenerator.getRandomGraph(nodeCount);

        bootGraph(graph);

        return graph;
    }

    public static Graph getBootedGraphFromFile(String filename) {

        return null;
    }

    private static void bootGraph(Graph graph) {
        // assign initial address pool to forst node in the network
        AddressPool addressPool = new AddressPool(new Address(1024), (long) Math.pow(2.0, 32.0));
        graph.getNodes().get("1").getAddressManager().addAddressPool(addressPool);

        // All nodes start up
        for (AbstractNode node : graph.getNodes().values()) {
            node.bootSequence();
        }

        for (int i = 0; i < 1000; i++) {
            Scheduler.tick(graph);

            boolean allNodesHaveAddresses = true;
            for (AbstractNode node : graph.getNodes().values()) {
                if (node.getAddress().equals(Address.undefined())) {
                    allNodesHaveAddresses = false;
                }
            }

            if (allNodesHaveAddresses) {
                break;
            }
        }

        for (AbstractNode node : graph.getNodes().values()) {

            assertEquals(node.getNeighbours().size(), node.getNetworkInterfaces().size());

            if (node.getAddress().equals(Address.undefined())) {
                new GraphWriter().graphToDot(graph, "randomTestFail.dot");
                new GraphWriter().graphToGraphic(graph, "test.png");
            }
        }

        for (AbstractNode node : graph.getNodes().values()) {
            assertNotEquals(Address.undefined(), node.getAddress());
        }
    }

    public static String getRandomNodeId(int nodeCount) {

        int random = 0;
        while (random == 0) {
            random = (int) (Math.random() * nodeCount);
        }
        return String.valueOf(random);
    }
}

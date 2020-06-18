package de.joschal.amp.sim;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.core.entities.network.addressing.AddressPool;
import de.joschal.amp.core.entities.network.routing.Route;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.io.NetworkInterface;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.logic.utils.Dijkstra;
import de.joschal.amp.sim.core.logic.utils.Scheduler;
import de.joschal.amp.sim.outbound.GraphReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;

import static de.joschal.amp.sim.core.logic.utils.Dijkstra.dijkstra;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class RoutingVsDijkstra {

    Graph graph;

    @BeforeEach
    void setUp() {
        // read graph from file
        graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        // assign initial address pool to forst node in the network
        AddressPool addressPool = new AddressPool(new Address(1024), 1024);
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
                log.info("assignment finished after {} ticks", i);
                break;
            }
        }

        // clear all buffers after finished assignment
        for (AbstractNode node : graph.getNodes().values()) {
            for (NetworkInterface networkInterface : node.getNetworkInterfaces()) {
                networkInterface.getDataLink().getASendQueue().clear();
                networkInterface.getDataLink().getBSendQueue().clear();
            }
        }

        log.info("-------------------- Test starts here ---------------------");
    }

    @Test
    void routingVsDijkstra() {

        sendDatagramToNodeById("2");
        sendDatagramToNodeById("3");
        sendDatagramToNodeById("4");
        sendDatagramToNodeById("5");
        sendDatagramToNodeById("6");
        sendDatagramToNodeById("7");
        sendDatagramToNodeById("8");
        sendDatagramToNodeById("9");

        HashMap<String, Dijkstra.DistanceVector> distances = dijkstra(new LinkedList<>(graph.getNodes().values()), graph.getNodebyId("1"));

        // Assert that the node has routes to every other node
        assertEquals(8, graph.getNodebyId("1").getRouter().getRoutingTable().size());

        for (Route route : graph.getNodebyId("1").getRouter().getRoutingTable()) {
            for (Dijkstra.DistanceVector distanceVector : distances.values()) {

                if (route.getAddress().getValue() == distanceVector.getNode().getAddress().getValue()) {
                    log.info("Comparing route to {}", distanceVector.getNode().getId());
                    assertEquals(distanceVector.getDistance(), route.getHops());
                }
            }
        }

    }

    private void sendDatagramToNodeById(String id) {
        SimpleNode startNode = (SimpleNode) graph.getNodebyId("1");

        startNode.sendDatagram("Hello World", graph.getNodebyId(id).getAddress());

        for (int i = 0; i < 20; i++) {
            Scheduler.tick(graph);
        }
    }
}

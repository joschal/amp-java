package de.joschal.amp;

import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.Route;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.sim.core.Dijkstra;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.outbound.GraphReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static de.joschal.amp.sim.core.Dijkstra.dijkstra;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class RoutingAlgorithmVsDijkstra {

    @Test
    void routingAlgorithmVsDijkstra() {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraphLarge.dot").getFile());
        SimpleNode source = (SimpleNode) graph.getNodebyId("1");
        List<AbstractNode> nodes = new LinkedList<>(graph.getNodes().values());
        HashMap<String, Dijkstra.DistanceVector> distances = dijkstra(nodes, source);

        AddressPool addressPool = new AddressPool(1, 256);
        source.getAddressManager().addAddressPool(addressPool);

        int bootCounter = 0;

        while (bootCounter < nodes.size()) {

            for (AbstractNode node : nodes) {
                node.bootSequence();

                // this checks, if a valid address is assigned
                if (node.getAddress().getValue() != 0) {
                    log.info("Node {} booted with address {}", node.getId(), node.getAddress().getValue());
                    bootCounter++;
                }
            }
        }

        log.info("all nodes booted");

        for (AbstractNode node : nodes) {
            // Send message to trigger route discovery
            log.info("Will invoke action on {}", node.getId());
            source.action("message", node.getAddress());
        }

        for (Route route : source.getRouter().getRoutingTable()) {
            for (Dijkstra.DistanceVector distanceVector : distances.values()) {

                if (route.getAddress().getValue() == distanceVector.getNode().getAddress().getValue()) {
                    log.info("Comparing route to {}", distanceVector.getNode().getId());
                    assertEquals(distanceVector.getDistance(), route.getHops());
                }
            }
        }
    }
}

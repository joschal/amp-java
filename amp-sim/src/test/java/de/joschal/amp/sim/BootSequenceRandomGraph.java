package de.joschal.amp.sim;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.core.entities.network.addressing.AddressPool;
import de.joschal.amp.performance.GraphGenerator;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.logic.utils.Scheduler;
import de.joschal.amp.sim.outbound.GraphReader;
import de.joschal.amp.sim.outbound.GraphWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class BootSequenceRandomGraph {

    @Test
    void bootSequenceRandomGraph() {

        // read graph from file
        Graph graph = GraphGenerator.getRandomGraph(32);
        performAddressAsignment(graph);

    }

    @Test
    void bootSequenceGraphFromFile() {

        // read graph from file
        Graph graph = new GraphReader().readGraph(getClass().getResource("/randomBootTestExport.dot").getFile());
        performAddressAsignment(graph);

    }


    private void performAddressAsignment(Graph graph) {
        // assign initial address pool to forst node in the network
        AddressPool addressPool = new AddressPool(new Address(1024), (long) Math.pow(2.0, 32.0));
        graph.getNodes().get("1").getAddressManager().addAddressPool(addressPool);

        // All nodes start up
        for (AbstractNode node : graph.getNodes().values()) {
            node.bootSequence();
        }


        for (int i = 0; i < 1000; i++) {
            log.info("---------------- TICK -----------------");
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

        for (AbstractNode node : graph.getNodes().values()) {
            log.info("Node {} has address {}", node.getId(), node.getAddress());

            assertEquals(node.getNeighbours().size(), node.getNetworkInterfaces().size());

            if (node.getAddress().equals(Address.undefined())) {
                new GraphWriter().graphToDot(graph, "randomBootTestExport.dot");
                new GraphWriter().graphToGraphic(graph, "randomBootTestExport.png");
            }

        }

        for (AbstractNode node : graph.getNodes().values()) {
            log.info("checking node {}", node.getId());
            assertNotEquals(Address.undefined(), node.getAddress());
        }
    }
}

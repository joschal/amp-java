package de.joschal.amp.sim.core.logic.utils;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.core.entities.network.addressing.AddressPool;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.outbound.GraphReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
class ZALAQ {

    @Test
    void bootTest() {

        // read graph from file
        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

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

        for (AbstractNode node : graph.getNodes().values()) {
            assertNotEquals(Address.undefined(), node.getAddress());
        }

    }
}
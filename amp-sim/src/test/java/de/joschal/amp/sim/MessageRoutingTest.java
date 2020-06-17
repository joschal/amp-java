package de.joschal.amp.sim;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.NetworkInterface;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.logic.utils.Scheduler;
import de.joschal.amp.sim.outbound.GraphReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class MessageRoutingTest {

    Graph graph;

    @BeforeEach
    void setUp() {
        // read graph from file
        graph = new GraphReader().readGraph(getClass().getResource("/randomTestFail.dot").getFile());

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
    void sendDatagram() {

        SimpleNode startNode = (SimpleNode) graph.getNodebyId("1");

        startNode.sendDatagram("Hello World", graph.getNodebyId("9").getAddress());

        for (int i = 0; i < 500; i++) {
            Scheduler.tick(graph);
        }

        assertEquals(3, graph.getNodebyId("9").getRouter().getRoute(graph.getNodebyId("1").getAddress()).get().hops);

    }
}

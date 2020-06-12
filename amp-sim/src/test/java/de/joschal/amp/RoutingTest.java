package de.joschal.amp;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.outbound.GraphReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoutingTest {


    Graph graph;

    @BeforeEach
    void setUp() {
        graph = null;
        graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());
    }

    @Test
    public void routingTest() {

        SimpleNode node1 = (SimpleNode) graph.getNodebyId("1");
        SimpleNode node2 = (SimpleNode) graph.getNodebyId("2");
        SimpleNode node3 = (SimpleNode) graph.getNodebyId("3");
        SimpleNode node4 = (SimpleNode) graph.getNodebyId("4");
        SimpleNode node5 = (SimpleNode) graph.getNodebyId("5");

        List<AddressPool> pools = new LinkedList<>();
        pools.add(new AddressPool(new Address(1), 64));
        node1.getAddressManager().addAddressPools(pools);

        node1.bootSequence();
        node3.bootSequence();
        node2.bootSequence();
        node4.bootSequence();
        node5.bootSequence();

        node1.action("Foo", node5.getAddress());

    }

    @Test
    public void routeDiscoveryTest() {

        SimpleNode node1 = (SimpleNode) graph.getNodebyId("1");
        SimpleNode node2 = (SimpleNode) graph.getNodebyId("2");
        SimpleNode node3 = (SimpleNode) graph.getNodebyId("3");
        SimpleNode node4 = (SimpleNode) graph.getNodebyId("4");
        SimpleNode node5 = (SimpleNode) graph.getNodebyId("5");

        List<AddressPool> pools = new LinkedList<>();
        pools.add(new AddressPool(new Address(1), 64));
        node1.getAddressManager().addAddressPools(pools);

        node1.bootSequence();
        node2.bootSequence();
        node3.bootSequence();
        node4.bootSequence();
        node5.bootSequence();

        // Route from node1 to node2 shoud be ohe hop
        node1.action("message0", node2.getAddress());
        checkRoutingTable(1, node1, node2);


        // Route from node1 to node4 shoud be two hops
        node1.action("message1", node4.getAddress());
        checkRoutingTable(2, node1, node4);


        // Route from node1 to node5 shoud be three hops
        node1.action("message2", node5.getAddress());
        checkRoutingTable(3, node1, node5);

        // This should already be in the routing table from a previous operation
        checkRoutingTable(3, node5, node1);
    }

    private void checkRoutingTable(int hops, AbstractNode source, AbstractNode destination) {
        assertEquals(hops, source.getRouter().getRoutingTable().stream()
                .filter(r -> r.getAddress().equals(destination.getAddress()))
                .findFirst()
                .get().hops);
    }

}

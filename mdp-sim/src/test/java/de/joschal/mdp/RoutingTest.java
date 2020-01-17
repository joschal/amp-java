package de.joschal.mdp;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.logic.nodes.SimpleNode;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.outbound.GraphReader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoutingTest {


    Graph graph;

    @Before
    public void setUp() {
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

        node1.getAddressManager().addAddressPool(new AddressPool(
                new Address(1),
                new Address(32)));

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

        node1.getAddressManager().addAddressPool(new AddressPool(
                new Address(1),
                new Address(32)));

        node1.bootSequence();
        node2.bootSequence();
        node3.bootSequence();
        node4.bootSequence();
        node5.bootSequence();

        node1.action("message", node2.getAddress());

        // Route from node1 to node2 shoud be ohe hop
        assertEquals(1, node1.getRouter().getRoutingTable().stream()
                .filter(r -> r.getAddress().equals(node2.getAddress()))
                .findFirst()
                .get().hops);

        node1.action("message", node4.getAddress());

        System.out.println("1 -> " + node1.getRouter().getRoutingTable().toString());
        System.out.println("2 -> " + node2.getRouter().getRoutingTable().toString());
        System.out.println("3 -> " + node3.getRouter().getRoutingTable().toString());
        System.out.println("4 -> " + node4.getRouter().getRoutingTable().toString());
        System.out.println("5 -> " + node5.getRouter().getRoutingTable().toString());

        // Route from node1 to node4 shoud be two hops
        assertEquals(2, node1.getRouter().getRoutingTable().stream()
                .filter(r -> r.getAddress().equals(node4.getAddress()))
                .findFirst()
                .get().hops);

    }

}

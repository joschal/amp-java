package de.joschal.mdp;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.logic.nodes.SimpleNode;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.outbound.GraphReader;
import org.junit.Before;
import org.junit.Test;

public class RoutingTest {


    Graph graph;

    @Before
    public void setUp() {
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

}

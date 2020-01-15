package de.joschal.mdp.simulaiton;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.logic.simple.SimpleNode;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.outbound.GraphReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Slf4j
public class BootTest {

    Graph graph;

    @Before
    public void setUp() {
        graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());
    }

    @Test
    public void bootTest() {

        SimpleNode node1 = (SimpleNode) graph.getNodebyId("1");
        SimpleNode node2 = (SimpleNode) graph.getNodebyId("2");
        SimpleNode node3 = (SimpleNode) graph.getNodebyId("3");
        SimpleNode node4 = (SimpleNode) graph.getNodebyId("4");

        node1.getAddressManager().addAddressPool(new AddressPool(
                new Address(1),
                new Address(16)));

        node1.bootSequence();
        node2.bootSequence();
        node3.bootSequence();
        node4.bootSequence();

        assertEquals(1, node1.getAddress().getValue());
        assertEquals(9, node2.getAddress().getValue());
        assertEquals(13, node3.getAddress().getValue());
        assertEquals(15, node4.getAddress().getValue());

    }
}

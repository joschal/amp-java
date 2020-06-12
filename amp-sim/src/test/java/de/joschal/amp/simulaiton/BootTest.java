package de.joschal.amp.simulaiton;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.outbound.GraphReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Slf4j
public class BootTest {

    Graph graph;

    @BeforeEach
    public void setUp() {
        graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());
    }

    @Test
    public void bootTest() {

        SimpleNode node1 = (SimpleNode) graph.getNodebyId("1");
        SimpleNode node2 = (SimpleNode) graph.getNodebyId("2");
        SimpleNode node3 = (SimpleNode) graph.getNodebyId("3");
        SimpleNode node4 = (SimpleNode) graph.getNodebyId("4");

        List<AddressPool> pools = new LinkedList<>();
        pools.add(new AddressPool(new Address(1), 32));
        node1.getAddressManager().addAddressPools(pools);

        node1.bootSequence();
        node2.bootSequence();
        node3.bootSequence();
        node4.bootSequence();

        assertEquals(1, node1.getAddress().getValue());
        assertEquals(18, node2.getAddress().getValue());
        assertEquals(10, node3.getAddress().getValue());
        assertEquals(15, node4.getAddress().getValue());

    }
}

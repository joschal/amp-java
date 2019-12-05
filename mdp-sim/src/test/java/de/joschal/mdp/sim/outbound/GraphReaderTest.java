package de.joschal.mdp.sim.outbound;

import de.joschal.mdp.sim.core.entities.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GraphReaderTest {

    /**
     * This test asserts that a graph can be properly read and parsed from file
     */
    @Test
    public void readGraph() {

        GraphReader graphReader = new GraphReader();

        Graph graph = graphReader.readGraph(getClass().getResource("/testGraph.dot").getFile());

        assertEquals(3, graph.getEdges().size());
        assertEquals(3, graph.getNodes().size());

        assertNotNull(graph.getNodebyId("1"));
        assertNotNull(graph.getNodebyId("2"));
        assertNotNull(graph.getNodebyId("3"));
    }
}
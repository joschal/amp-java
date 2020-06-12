package de.joschal.amp.sim.outbound;

import de.joschal.amp.sim.core.entities.Graph;
import org.junit.jupiter.api.Test;

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

        assertEquals(11, graph.getEdges().size());
        assertEquals(9, graph.getNodes().size());

        assertNotNull(graph.getNodebyId("1"));
        assertNotNull(graph.getNodebyId("2"));
        assertNotNull(graph.getNodebyId("3"));
        assertNotNull(graph.getNodebyId("4"));
        assertNotNull(graph.getNodebyId("5"));
        assertNotNull(graph.getNodebyId("6"));
        assertNotNull(graph.getNodebyId("7"));
        assertNotNull(graph.getNodebyId("8"));
        assertNotNull(graph.getNodebyId("9"));
    }
}
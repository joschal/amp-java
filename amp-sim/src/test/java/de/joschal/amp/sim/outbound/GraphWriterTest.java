package de.joschal.amp.sim.outbound;

import de.joschal.amp.sim.core.entities.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Actual writing is commented out to eliminate side effects during tests
 */
public class GraphWriterTest {

    private String tempDir;

    @BeforeEach
    public void setUp() throws Exception {
        this.tempDir = Files.createTempFile("", "").getParent().toString();
    }

    @Test
    public void graphToGraphic() {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        // new GraphWriter().graphToGraphic(graph, tempDir + "/graph.png");

    }

    @Test
    void graphToDot() throws IOException {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        // new GraphWriter().graphToDot(graph, tempDir + "/testGraph.dot");
    }
}
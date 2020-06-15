package de.joschal.amp.sim.outbound;

import de.joschal.amp.sim.core.entities.Graph;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

public class GraphWriterTest {

    private String tempDir;

    @Before
    public void setUp() throws Exception {
        this.tempDir = Files.createTempFile("", "").getParent().toString();
    }

    @Test
    public void graphToGraphic() {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        new GraphWriter().graphToGraphic(graph, tempDir + "/graph.png");

    }

    @Test
    void graphToDot() throws IOException {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        // new GraphWriter().graphToDot(graph, tempDir + "/testGraph.dot");

        //TODO read graph back in and compare to original
    }
}
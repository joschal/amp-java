package de.joschal.mdp.sim.outbound;

import de.joschal.mdp.sim.core.entities.Graph;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

public class GraphWriterTest {

    private String tempDir;

    @Before
    public void setUp() throws Exception {
        this.tempDir = Files.createTempFile("", "").getParent().toString();
    }

    @Test
    @Ignore
    public void graphToGraphic() throws IOException {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        new GraphWriter().graphToGraphic(graph, tempDir + "/graph.png");

    }

    @Test
    public void graphToDot() throws IOException {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        new GraphWriter().graphToDot(graph, tempDir + "/testGraph.dot");

        //TODO read graph back in and compare to original
    }
}
package de.joschal.mdp.sim.outbound;

import de.joschal.mdp.sim.core.entities.Graph;
import org.junit.Test;

public class GraphWriterTest {

    @Test
    public void graphToGraphic() {

        Graph graph = new GraphReader().readGraph("/graph.dot");

        new GraphWriter().graphToGraphic(graph, "/Users/joschal/Desktop/graph.png");
    }

    @Test
    public void graphToDot() {

        Graph graph = new GraphReader().readGraph("/graph.dot");

        new GraphWriter().graphToDot(graph, "/Users/joschal/Desktop/graph.dot");

    }
}
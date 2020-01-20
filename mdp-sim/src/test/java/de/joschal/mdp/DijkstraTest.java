package de.joschal.mdp;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.sim.core.Dijkstra;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.outbound.GraphReader;
import de.joschal.mdp.sim.outbound.GraphWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;

import static de.joschal.mdp.sim.core.Dijkstra.dijkstra;

@Slf4j
public class DijkstraTest {

    @Test
    void dijkstraTest() throws IOException {
        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraphLarge.dot").getFile());

        AbstractNode source = graph.getNodebyId("1");

        HashMap<String, Dijkstra.DistanceVector> distances =
                dijkstra(new LinkedList<>(graph.getNodes().values()), source);

        distances.forEach((s, distanceVector) ->
                log.info("Distance to {} via {} is {}",
                        distanceVector.getNode().getId(),
                        distanceVector.getPrevious().getId(),
                        distanceVector.getDistance()));

        new GraphWriter().graphToGraphic(graph, Files.createTempFile("", "").getParent().toString() + "/graph.png");

    }


}

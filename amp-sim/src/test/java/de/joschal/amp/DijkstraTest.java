package de.joschal.amp;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.sim.core.logic.utils.Dijkstra;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.outbound.GraphReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import static de.joschal.amp.sim.core.logic.utils.Dijkstra.dijkstra;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DijkstraTest {

    @Test
    void dijkstraTest() {
        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());
        //new GraphWriter().graphToGraphic(graph, Files.createTempFile("", "").getParent().toString() + "/graph.png");

        AbstractNode source = graph.getNodebyId("1");

        // invoke algorithm
        HashMap<String, Dijkstra.DistanceVector> distances =
                dijkstra(new LinkedList<>(graph.getNodes().values()), source);

        // Print
        distances.forEach((s, distanceVector) ->
                log.info("Distance to {} via {} is {}",
                        distanceVector.getNode().getId(),
                        distanceVector.getPrevious().getId(),
                        distanceVector.getDistance()));

        // assert
        assertEquals(1, distances.get("2").getDistance());
        assertEquals(1, distances.get("3").getDistance());
        assertEquals(2, distances.get("4").getDistance());
        assertEquals(2, distances.get("5").getDistance());
        assertEquals(1, distances.get("6").getDistance());
        assertEquals(2, distances.get("7").getDistance());
        assertEquals(3, distances.get("8").getDistance());
        assertEquals(3, distances.get("9").getDistance());
    }


}

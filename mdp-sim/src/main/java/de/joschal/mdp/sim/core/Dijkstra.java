package de.joschal.mdp.sim.core;

import de.joschal.mdp.core.entities.network.AbstractNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Dijkstra {

    public static HashMap<String, DistanceVector> dijkstra(List<AbstractNode> graph, AbstractNode source) {

        HashMap<String, DistanceVector> distances = init(graph, source);

        List<AbstractNode> notVisited = new LinkedList<>();
        notVisited.add(source);
        List<AbstractNode> visited = new LinkedList<>();

        while (!notVisited.isEmpty()) {

            AbstractNode currentNode = notVisited.get(0);

            // Normally, the neighbour with the shortest distance would be choosen next.
            // But since all edges have the same weight, we need to keep track, which nodes we already visited
            for (AbstractNode neighbour : currentNode.getNeighbours()) {
                if (!visited.contains(neighbour)) {
                    notVisited.add(neighbour);
                    break;
                }
            }

            notVisited.remove(currentNode);
            visited.add(currentNode);

            for (AbstractNode neighbour : currentNode.getNeighbours()) {

                int currentdistance = distances.get(neighbour.getId()).getDistance();
                int newDistance = distances.get(currentNode.getId()).getDistance() + 1;

                if (newDistance < currentdistance) {
                    distances.get(neighbour.getId()).setDistance(newDistance);
                    distances.get(neighbour.getId()).setPrevious(currentNode);
                }
            }
        }
        return distances;
    }


    private static HashMap<String, DistanceVector> init(List<AbstractNode> graph, AbstractNode source) {

        HashMap<String, DistanceVector> distances = new HashMap<>();

        graph.forEach((node) -> distances.put(node.getId(), new DistanceVector(node)));

        distances.get(source.getId()).setDistance(0);
        distances.get(source.getId()).setPrevious(source);

        return distances;

    }

    @Getter
    @Setter
    @ToString
    public static class DistanceVector {

        public DistanceVector(AbstractNode node) {
            this.node = node;
        }

        private AbstractNode node;
        private AbstractNode previous;
        private int distance = Integer.MAX_VALUE;

    }
}

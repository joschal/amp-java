package de.joschal.amp.sim.core.entities;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.DataLink;

import java.util.HashMap;
import java.util.Map;

public class Graph {

    public Graph() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
    }

    private Map<String, AbstractNode> nodes;
    private Map<String, DataLink> edges;

    public boolean addNode(AbstractNode node) {
        if (nodes.containsKey(node.getId())) {
            return false;
        } else {
            nodes.put(node.getId(), node);
            return true;
        }
    }

    public boolean addEdge(DataLink dataLink) {
        if (edges.containsKey(dataLink.getName())) {
            return false;
        } else {
            edges.put(dataLink.getName(), dataLink);
            return true;
        }
    }

    public AbstractNode getNodebyId(String id) {
        return nodes.get(id);
    }

    public DataLink getEdge(String name) {
        return edges.get(name);
    }

    public Map<String, AbstractNode> getNodes() {
        return nodes;
    }

    public Map<String, DataLink> getEdges() {
        return edges;
    }

}

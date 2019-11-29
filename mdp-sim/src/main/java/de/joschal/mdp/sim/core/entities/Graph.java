package de.joschal.mdp.sim.core.entities;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.protocol.Address;

import java.util.HashMap;

public class Graph {

    private HashMap<Address, AbstractNode> nodes = new HashMap<>();
    private HashMap<String, DataLink> edges = new HashMap<>();

    public boolean addNode(AbstractNode node) {
        if (nodes.containsKey(node.getAddress())) {
            return false;
        } else {
            nodes.put(node.getAddress(), node);
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

    public AbstractNode getNode(Address address) {
        return nodes.get(address);
    }

    public DataLink getEdge(String name) {
        return edges.get(name);
    }

    public Iterable<DataLink> getEdges() {
        return edges.values();
    }

}

package de.joschal.amp.sim.core.inbound;

import de.joschal.amp.core.entities.network.AbstractNode;

public interface INodeController {

    boolean addNode(AbstractNode node);

    boolean removeNode(String nodeId);

    boolean linkNodes(String source, String destination);

    String nodeInfo(String node);

}

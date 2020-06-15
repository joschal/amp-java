package de.joschal.amp.sim.core.logic;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.DataLink;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.inbound.INodeController;
import de.joschal.amp.sim.core.logic.utils.Linker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class NodeController implements INodeController {

    Graph graph;

    @Override
    public boolean addNode(AbstractNode node) {

        return this.graph.addNode(node);

    }

    @Override
    public boolean removeNode(String node) {
        // TODO Remove node and all associated Links
        // This will trigger the routing algorithm
        return false;
    }

    @Override
    public boolean linkNodes(String source, String destination) {

        DataLink dataLink = Linker.linkNodes(graph.getNodebyId(source), graph.getNodebyId(destination));

        this.graph.addEdge(dataLink);
        return true;
    }

    @Override
    public String nodeInfo(String node) {
        return node;
    }

}

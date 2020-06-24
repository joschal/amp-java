package de.joschal.amp.sim.core.logic;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.io.DataLink;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.inbound.INetworkController;
import de.joschal.amp.sim.core.logic.utils.Scheduler;
import de.joschal.amp.sim.core.outbound.IGraphReader;
import de.joschal.amp.sim.core.outbound.IGraphWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class NetController implements INetworkController {

    @Autowired
    private Graph network;

    public NetController(Scheduler scheduler, IGraphReader graphReader, IGraphWriter graphWriter) {
        this.scheduler = scheduler;
        this.graphReader = graphReader;
        this.graphWriter = graphWriter;
    }

    private Scheduler scheduler;
    private IGraphReader graphReader;
    private IGraphWriter graphWriter;

    @Override
    public void readNetworkFromFile(String name) {
        Graph graph = graphReader.readGraph(name);

        for (AbstractNode node : graph.getNodes().values()) {
            this.network.addNode(node);
        }

        for (DataLink dataLink : graph.getEdges().values()) {
            this.network.addEdge(dataLink);
        }
    }

    @Override
    public void persistNetworkToFile(String outputName) {
        graphWriter.graphToDot(this.network, outputName);
    }

    @Override
    public void showNet() {
        graphWriter.graphToGraphic(this.network, UUID.randomUUID().toString());
    }

    @Override
    public void networkTick() {
        scheduler.tick(this.network);
    }
}

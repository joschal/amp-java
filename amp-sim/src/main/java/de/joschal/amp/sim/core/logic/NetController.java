package de.joschal.amp.sim.core.logic;

import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.inbound.INetworkController;
import de.joschal.amp.sim.core.logic.utils.Scheduler;
import de.joschal.amp.sim.core.outbound.IGraphReader;
import de.joschal.amp.sim.core.outbound.IGraphWriter;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class NetController implements INetworkController {

    private Graph network;
    private Scheduler scheduler;
    private IGraphReader graphReader;
    private IGraphWriter graphWriter;

    @Override
    public void readNetworkFromFile(String name) {
        this.network = graphReader.readGraph(name);
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

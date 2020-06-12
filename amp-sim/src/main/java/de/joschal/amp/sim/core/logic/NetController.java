package de.joschal.amp.sim.core.logic;

import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.inbound.INetController;
import de.joschal.amp.sim.core.outbound.IGraphReader;
import de.joschal.amp.sim.core.outbound.IGraphWriter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NetController implements INetController {

    private Graph network;
    private IGraphReader graphReader;
    private IGraphWriter graphWriter;

    @Override
    public void readNet(String name) {
        this.network = graphReader.readGraph(name);
    }

    @Override
    public void persistNet(String outputName) {
        graphWriter.graphToDot(this.network, outputName);
    }

    @Override
    public void showNet(String outputName) {
        graphWriter.graphToGraphic(this.network, outputName);
    }
}

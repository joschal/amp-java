package de.joschal.amp.sim.core.inbound;

public interface INetController {

    void readNet(String name);

    void persistNet(String outputName);

    void showNet(String outputName);

}

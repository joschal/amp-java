package de.joschal.amp.sim.core.outbound;

import de.joschal.amp.sim.core.entities.Graph;

public interface IGraphReader {

    Graph readGraph(String filename);
}

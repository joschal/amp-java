package de.joschal.mdp.sim.core.outbound;

import de.joschal.mdp.sim.core.entities.Graph;

public interface IGraphReader {

    public Graph readGraph(String filename);
}

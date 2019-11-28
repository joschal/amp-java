package de.joschal.mdp.sim.core.outbound;

import de.joschal.mdp.sim.core.entities.Graph;

public interface IGraphWriter {

    void graphToGraphic(Graph graph, String filename);

    void graphToDot(Graph graph, String filename);

}

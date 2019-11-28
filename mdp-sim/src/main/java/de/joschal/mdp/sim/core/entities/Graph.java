package de.joschal.mdp.sim.core.entities;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.protocol.Address;

import java.util.HashMap;

public class Graph {

    HashMap<Address, AbstractNode> nodes;
    HashMap<String, DataLink> edges;
}

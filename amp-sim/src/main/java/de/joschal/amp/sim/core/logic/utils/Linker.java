package de.joschal.amp.sim.core.logic.utils;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.DataLink;
import de.joschal.amp.core.entities.network.NetworkInterface;

import java.util.UUID;

/**
 * This class links two nodes on layer 2
 */
public class Linker {

    public static DataLink linkNodes(AbstractNode node1, AbstractNode node2) {

        if (node1.getId() == node2.getId()){
            throw new RuntimeException("Invalid Data Link. A node should not be linked to itself");
        }

        NetworkInterface networkInterface1 = new NetworkInterface("to " + node2.getId(), node1);
        node1.addNetworkInterface(networkInterface1);

        NetworkInterface networkInterface2 = new NetworkInterface("to " + node1.getId(), node2);
        node2.addNetworkInterface(networkInterface2);

        return linkInterfaces(networkInterface1, networkInterface2);
    }

    public static DataLink linkInterfaces(NetworkInterface networkInterface1, NetworkInterface networkInterface2) {

        return new DataLink(
                UUID.randomUUID().toString(),
                networkInterface1,
                networkInterface2);
    }
}

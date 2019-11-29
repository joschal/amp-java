package de.joschal.mdp.sim;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.network.NetworkInterface;

import java.util.UUID;

public class Setup {

    public static DataLink linkNodes(AbstractNode node1, AbstractNode node2) {

        NetworkInterface networkInterface1 = new NetworkInterface("to " + node2.getAddress());
        node1.addNetworkInterface(networkInterface1);

        NetworkInterface networkInterface2 = new NetworkInterface("to " + node1.getAddress());
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

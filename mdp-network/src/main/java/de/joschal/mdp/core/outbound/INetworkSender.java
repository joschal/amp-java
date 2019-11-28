package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.protocol.Address;

public interface INetworkSender {

    // Via default route
    boolean sendToNetwork(String message, Address destination);

    boolean sendToNetwork(String message, Address destination, NetworkInterface networkInterface);

}

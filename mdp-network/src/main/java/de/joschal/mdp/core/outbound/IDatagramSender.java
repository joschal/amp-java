package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.Address;

// Invoked by routing logic
public interface IDatagramSender {

    // Via default route
    boolean sendDatagram(String message, Address destination);

    // Via specified interface
    boolean sendDatagram(String message, Address destination, NetworkInterface networkInterface);

}

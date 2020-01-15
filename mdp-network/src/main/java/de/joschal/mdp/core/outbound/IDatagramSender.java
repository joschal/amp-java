package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.network.NetworkInterface;

import java.util.Optional;

// Invoked by routing logic
public interface IDatagramSender {

    // Via default route
    Optional<AbstractMessage> sendDatagram(String message, Address destination);

    // Via specified interface
    Optional<AbstractMessage> sendDatagram(String message, Address destination, NetworkInterface networkInterface);

}

package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.network.NetworkInterface;

import java.util.List;

// Invoked by routing logic
public interface IDatagramSender {


    List<AbstractMessage> sendDatagram(String message, Address destination);

    // Via specified interface
    List<AbstractMessage> sendDatagram(String message, Address destination, NetworkInterface networkInterface);

}

package de.joschal.amp.core.outbound.layer3;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.network.NetworkInterface;

import java.util.List;

// Invoked by routing logic
public interface IDataMessageSender {


    List<AbstractMessage> sendDatagram(String message, Address destination);

    // Via specified interface
    List<AbstractMessage> sendDatagram(String message, Address destination, NetworkInterface networkInterface);

}

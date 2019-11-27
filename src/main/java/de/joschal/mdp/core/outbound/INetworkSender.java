package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.protocol.Address;

public interface INetworkSender {

    boolean sendToNetwork(String message, Address destination);

}

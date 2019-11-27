package de.joschal.mdp.core.inbound;

import de.joschal.mdp.core.entities.protocol.Address;

public interface INetworkReceiver {

    boolean receiveFromNetwork(String message, Address source);

}

package de.joschal.mdp.inbund;

import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.inbound.INetworkReceiver;

public class Receiver implements INetworkReceiver {

    @Override
    public boolean receiveFromNetwork(String message, Address source) {
        return false;
    }
}

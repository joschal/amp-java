package de.joschal.mdp.core.inbound;

import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.AbstractMessage;

public interface INetworkReceiver {

    void handleMessage(AbstractMessage message, NetworkInterface networkInterface);

}

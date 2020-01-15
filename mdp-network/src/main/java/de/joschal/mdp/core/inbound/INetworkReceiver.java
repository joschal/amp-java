package de.joschal.mdp.core.inbound;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.network.NetworkInterface;

import java.util.Optional;

public interface INetworkReceiver {

    Optional<AbstractMessage> handleMessage(AbstractMessage message, NetworkInterface networkInterface);

}

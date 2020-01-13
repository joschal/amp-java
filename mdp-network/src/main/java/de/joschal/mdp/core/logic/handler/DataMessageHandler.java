package de.joschal.mdp.core.logic.handler;

import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.inbound.INetworkReceiver;

public class DataMessageHandler implements INetworkReceiver {

    @Override
    public void handleMessage(AbstractMessage message, NetworkInterface networkInterface) {
    }
}

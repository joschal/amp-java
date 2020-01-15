package de.joschal.mdp.core.logic.handler;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.inbound.INetworkReceiver;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DataMessageHandler implements INetworkReceiver {

    @Override
    public Optional<AbstractMessage> handleMessage(AbstractMessage message, NetworkInterface source) {
        log.info("Received message {} from interface {}", message, source);
        return Optional.empty();
    }
}

package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.inbound.INetworkReceiver;
import de.joschal.amp.core.entities.network.NetworkInterface;
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

package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.messages.addressing.PoolAccepted;
import de.joschal.amp.core.entities.messages.addressing.PoolAssigned;
import de.joschal.amp.core.inbound.INetworkReceiver;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.NetworkInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class AddressingMessageHandler implements INetworkReceiver {

    private AbstractNode node;

    @Override
    public Optional<AbstractMessage> handleMessage(AbstractMessage message, NetworkInterface source) {

        if (message instanceof PoolAccepted) {
            return handlePoolAccepted(message);
        }

        log.error("Not Implemented");
        return Optional.empty();
    }

    private Optional<AbstractMessage> handlePoolAccepted(AbstractMessage message) {
        PoolAccepted accepted = (PoolAccepted) message;
        return Optional.of(new PoolAssigned(accepted, true));
    }
}

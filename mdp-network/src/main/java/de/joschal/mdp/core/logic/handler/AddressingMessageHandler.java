package de.joschal.mdp.core.logic.handler;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.messages.addressing.PoolAccepted;
import de.joschal.mdp.core.entities.messages.addressing.PoolAssigned;
import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.inbound.INetworkReceiver;
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
        PoolAssigned response;
        if (this.node.getAddressManager().isPoolAvailable(accepted.getAddressPool())) {
            response = new PoolAssigned(accepted, true);
        } else {
            log.error("Pool is not available anymore and could therefore not be assigned to requesting node");
            response = new PoolAssigned(accepted, false);
        }

        return Optional.of(response);
    }
}

package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.messages.addressing.AbstractAddressingMessage;
import de.joschal.mdp.core.entities.messages.control.AbstractControlMessage;
import de.joschal.mdp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.mdp.core.entities.messages.routing.AbstractRoutingMessage;
import de.joschal.mdp.core.inbound.IDataLinkReceiver;
import de.joschal.mdp.core.outbound.IDataLinkSender;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Getter
@Setter
public class NetworkInterface implements IDataLinkReceiver, IDataLinkSender {

    public NetworkInterface(String name) {
        this.name = name;
    }

    private String name;
    private DataLink dataLink;
    private AbstractNode node;

    @Override
    public Optional<AbstractMessage> sendMessage(AbstractMessage message) {
        return dataLink.exchange(message, this);
    }

    @Override
    public Optional<AbstractMessage> receiveMessage(AbstractMessage message) {

        this.node.router.updateRoutingTable(this, message);
        message.hop();

        if (message.getDestinationAddress().equals(this.node.getAddress())) {
            log.debug("[{}] Received a message to handle locally {}", this.node.getId(), message);
            return handleMessageLocally(message);
        } else if (message instanceof AbstractDataMessage) {
            log.debug("[{}] Received a message to forward {}", this.node.getId(), message);
            return this.node.router.forwardMessage(message);
        } else {
            log.error("[{}] Received an unknown message {}", this.node.getId(), message);
            return Optional.empty();
        }
    }

    /**
     * Filters for message type and invokes the appropriate handler
     *
     * @param message Message to resolve
     * @return Optional response message
     */
    private Optional<AbstractMessage> handleMessageLocally(AbstractMessage message) {

        if (message instanceof AbstractAddressingMessage) {
            return node.addressingMessageHandler.handleMessage(message, this);
        } else if (message instanceof AbstractControlMessage) {
            return node.controlMessageHandler.handleMessage(message, this);
        } else if (message instanceof AbstractRoutingMessage) {
            return node.routingMessageHandler.handleMessage(message, this);
        } else if (message instanceof AbstractDataMessage) {
            return node.dataMessageHandler.handleMessage(message, this);
        } else {
            log.error("Received a message of unknown type");
            return Optional.empty();
        }

    }
}

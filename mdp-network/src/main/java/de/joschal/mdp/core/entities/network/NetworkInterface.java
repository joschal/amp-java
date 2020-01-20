package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.messages.Forwardable;
import de.joschal.mdp.core.entities.messages.addressing.AbstractAddressingMessage;
import de.joschal.mdp.core.entities.messages.control.AbstractControlMessage;
import de.joschal.mdp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.mdp.core.entities.messages.routing.AbstractRoutingMessage;
import de.joschal.mdp.core.inbound.IDataLinkReceiver;
import de.joschal.mdp.core.outbound.IDataLinkSender;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode
public class NetworkInterface implements IDataLinkReceiver, IDataLinkSender {

    public NetworkInterface(String name) {
        this.name = name;
    }

    private String name;
    private DataLink dataLink;
    private AbstractNode node;

    @Override
    public List<AbstractMessage> sendMessage(AbstractMessage message) {
        return dataLink.exchange(message, this);
    }

    @Override
    public List<AbstractMessage> receiveMessage(AbstractMessage message) {

        List<AbstractMessage> returnMessages = new LinkedList<>();

        // if the message looped, immediately drop it
        if (message.getSourceAddress().equals(this.node.getAddress())) {
            return returnMessages;
        }

        message.hop(this.node);
        this.node.router.updateRoutingTable(this, message);

        if (message.getDestinationAddress().equals(this.node.getAddress()) ||
                message.getDestinationAddress().getValue() == 0) {

            log.trace("[{}] Received a message to handle locally {}", this.node.getId(), message);
            handleMessageLocally(message).ifPresent(returnMessages::add);

        } else if (message instanceof Forwardable) {

            log.trace("[{}] Received a message to forward {}", this.node.getId(), message);
            returnMessages.addAll(this.node.router.forwardMessage((Forwardable) message, this));

        } else {
            log.error("[{}] Received an unknown message {}", this.node.getId(), message);
        }
        return returnMessages;
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

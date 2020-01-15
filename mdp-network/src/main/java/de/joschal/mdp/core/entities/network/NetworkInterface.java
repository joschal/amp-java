package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.messages.addressing.AbstractAddressingMessage;
import de.joschal.mdp.core.entities.messages.control.AbstractControlMessage;
import de.joschal.mdp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.mdp.core.entities.messages.routing.AbstractRoutingMessage;
import de.joschal.mdp.core.inbound.IDataLinkReceiver;
import de.joschal.mdp.core.outbound.IDataLinkSender;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class NetworkInterface implements IDataLinkReceiver, IDataLinkSender {

    public NetworkInterface(String name) {
        this.name = name;
    }

    private String name;
    private DataLink dataLink;
    private AbstractNode node;

    @Override
    public Optional<AbstractMessage> receiveMessage(AbstractMessage message) {

        log.info("[{}] Received a message {}", this.node.getId(), message);
        message.hop();


        if (message instanceof AbstractAddressingMessage) {
            return node.addressingMessageHandler.handleMessage(message, this);
        } else if (message instanceof AbstractControlMessage) {
            return node.controlMessageHandler.handleMessage(message, this);
        } else if (message instanceof AbstractDataMessage) {
            return node.dataMessageHandler.handleMessage(message, this);
        } else if (message instanceof AbstractRoutingMessage) {
            return node.routingMessageHandler.handleMessage(message, this);
        } else {
            log.error("Received a message of unknown type");
            return Optional.empty();
        }
    }


    @Override
    public Optional<AbstractMessage> sendMessage(AbstractMessage datagram) {
        return dataLink.exchange(datagram, this);
    }

    void setDataLink(DataLink dataLink) {
        this.dataLink = dataLink;
    }

    void setNode(AbstractNode node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public AbstractNode getNode() {
        return node;
    }
}

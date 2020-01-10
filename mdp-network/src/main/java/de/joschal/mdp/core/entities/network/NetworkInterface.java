package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.protocol.AbstractMessage;
import de.joschal.mdp.core.entities.protocol.addressing.AbstractAddressingMessage;
import de.joschal.mdp.core.entities.protocol.control.AbstractControlMessage;
import de.joschal.mdp.core.entities.protocol.data.AbstractDataMessage;
import de.joschal.mdp.core.entities.protocol.routing.AbstractRoutingMessage;
import de.joschal.mdp.core.inbound.IDataLinkReceiver;
import de.joschal.mdp.core.outbound.IDataLinkSender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetworkInterface implements IDataLinkReceiver, IDataLinkSender {

    public NetworkInterface(String name) {
        this.name = name;
    }

    private String name;
    private DataLink dataLink;
    private AbstractNode node;

    @Override
    public boolean receiveMessage(AbstractMessage message) {

        log.info("[{}] Received a message {}", this.node.getId(), message);
        if (message.hop()) {

            log.info("Message expired: {}", message);
            return false;

        } else {

            // Distinguish between message types
            /*
            if (node.getAddress().equals(message.getDestinationAddress()) && message instanceof Datagram) {
                return node.receiveFromNetwork(((Datagram) message).getPayload(), message.getSourceAddress());
            } else {
                return node.router.forwardDatagram(message);
            }*/

            if (message instanceof AbstractAddressingMessage) {
                node.receiveAddressingMessage((AbstractAddressingMessage) message);
            } else if (message instanceof AbstractControlMessage) {
                node.receiveControlMessage((AbstractControlMessage) message);
            } else if (message instanceof AbstractDataMessage) {
                node.receiveDataMessage((AbstractDataMessage) message);
            } else if (message instanceof AbstractRoutingMessage) {
                node.receiveRoutingMessage((AbstractRoutingMessage) message);
            } else {
                log.error("Received a message of unknown type");
            }
            return true;
        }
    }

    @Override
    public boolean sendMessage(AbstractMessage datagram) {
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

package de.joschal.amp.io;

import de.joschal.amp.core.entities.messages.AbstractForwardableMessage;
import de.joschal.amp.core.entities.messages.AbstractMessage;
import de.joschal.amp.core.entities.messages.addressing.AbstractAddressingMessage;
import de.joschal.amp.core.entities.messages.control.AbstractControlMessage;
import de.joschal.amp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.amp.core.entities.messages.routing.AbstractRoutingMessage;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.core.inbound.layer2.IDataLinkReceiver;
import de.joschal.amp.core.outbound.layer2.IDataLinkSender;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode
public class NetworkInterface implements IDataLinkReceiver, IDataLinkSender {

    public NetworkInterface(String name, AbstractNode node) {
        this.name = name;
        this.node = node;
        this.nodeId = node.getId();
    }

    private AbstractNode node;
    private String nodeId;
    private String name;
    private DataLink dataLink;

    @Override
    public void sendMessage(AbstractMessage message) {
        dataLink.addToSendQueue(message, this);
    }

    @Override
    public void receiveMessage(AbstractMessage message) {

        // if the message looped, immediately drop it
        if (message.getSourceAddress().equals(this.node.getAddress()) &&
                !message.getSourceAddress().equals(Address.undefined())) {
            log.info("[{} - {}] Message looped. Will drop message. : [{}]", this.nodeId, this.node.getAddress(), message);
            return;
        }

        // Handle forwardable message
        if (message instanceof AbstractForwardableMessage) {

            // Forwardable messages must always have fully populated headers
            if (message.getSourceAddress().getValue() == 0 || message.getDestinationAddress().getValue() == 0) {
                // Drop message
                log.info("[{} - {}] message dropped : [{}]", this.nodeId, this.node.getAddress(), message);
                return;
            }

            //  process valid message
            handleForwardableMessage((AbstractForwardableMessage) message);
        } else {
            // Handle message locally
            handleLinkLocalMessage(message);
        }
    }


    private void handleLinkLocalMessage(AbstractMessage message) {
        // handle message by type -> hand up to control plane
        if (message instanceof AbstractAddressingMessage) {
            node.getAddressingMessageHandler().handleMessage(message, this);
        } else if (message instanceof AbstractControlMessage) {
            node.getControlMessageHandler().handleMessage(message, this);
        } else {
            log.error("Received a message of unknown type");
        }
    }

    private void handleForwardableMessage(AbstractForwardableMessage message) {

        // This needs to be done for every forwardable message
        message.hop(this.nodeId);
        this.node.getRouter().updateRoutingTable(this, message);

        if (message.getDestinationAddress().getValue() == this.node.getAddress().getValue()) {
            // Message is addressed to this node
            // handle message by type -> hand up to control plane
            if (message instanceof AbstractRoutingMessage) {
                node.getRoutingMessageHandler().handleMessage(message, this);
            } else if (message instanceof AbstractDataMessage) {
                node.getDataMessageHandler().handleMessage(message, this);
            }
        } else {
            // message is not indented for this node
            // it needs to be forwarded -> keep in data plane
            this.node.getMessageForwarder().forwardMessage(message, this);
        }
    }
}

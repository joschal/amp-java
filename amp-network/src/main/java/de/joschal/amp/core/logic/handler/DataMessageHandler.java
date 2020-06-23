package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.messages.AbstractForwardableMessage;
import de.joschal.amp.core.entities.messages.data.AcknowledgedDatagram;
import de.joschal.amp.core.entities.messages.data.Datagram;
import de.joschal.amp.core.entities.messages.data.DatagramAcknowledgement;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.inbound.layer3.IForwardableMessageReceiver;
import de.joschal.amp.core.logic.controlplane.MessageSender;
import de.joschal.amp.io.NetworkInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class DataMessageHandler implements IForwardableMessageReceiver {

    private AbstractNode node;
    private MessageSender messageSender;

    @Override
    public void handleMessage(AbstractForwardableMessage message, NetworkInterface source) {

        if (message instanceof Datagram) {

            // hand message up to business logic
            node.receiveDatagram(((Datagram) message).getPayload(), message.getSourceAddress());

        } else if (message instanceof AcknowledgedDatagram) {


            // hand message up to business logic
            boolean ack = node.receiveAcknowledgedDatagram(((AcknowledgedDatagram) message).getPayload(), message.getSourceAddress());

            // process acknowledgement if wanted by business logic
            if (ack) {
                DatagramAcknowledgement datagramAcknowledgement = new DatagramAcknowledgement((AcknowledgedDatagram) message);
                messageSender.sendMessageViaKnownRoute(datagramAcknowledgement);
            }

        } else if (message instanceof DatagramAcknowledgement) {
            log.info("Received Datagram Acknowledgement for {}", ((DatagramAcknowledgement) message).getUuid());
        }

    }
}

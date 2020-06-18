package de.joschal.amp.core.outbound.layer3;

import de.joschal.amp.core.entities.messages.AbstractMessage;
import de.joschal.amp.io.NetworkInterface;

public interface IMessageSender {

    boolean sendMessageViaKnownRoute(AbstractMessage abstractMessage);

    boolean sendMessageToNeighbor(AbstractMessage message, NetworkInterface networkInterface);

    void floodMessage(AbstractMessage message, NetworkInterface source);
}

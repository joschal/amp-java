package de.joschal.amp.core.inbound.layer3;

import de.joschal.amp.core.entities.messages.AbstractForwardableMessage;
import de.joschal.amp.io.NetworkInterface;

/**
 * Receives messages, which may be forwarded: Routing and Data mesages
 */
public interface IForwardableMessageReceiver {

    void handleMessage(AbstractForwardableMessage message, NetworkInterface source);
}

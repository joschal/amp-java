package de.joschal.amp.core.inbound.layer3;

import de.joschal.amp.core.entities.messages.AbstractMessage;
import de.joschal.amp.io.NetworkInterface;

/**
 * Receives messages, whiy must not be forwarded: Addressing and Control messages
 */
public interface ILinkLocalMessageReceiver {

    void handleMessage(AbstractMessage message, NetworkInterface source);

}

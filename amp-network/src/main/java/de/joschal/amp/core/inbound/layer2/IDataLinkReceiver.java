package de.joschal.amp.core.inbound.layer2;

import de.joschal.amp.core.entities.messages.AbstractMessage;

/**
 * Receives layer 2 messages
 */
public interface IDataLinkReceiver {

    void receiveMessage(AbstractMessage message);

}

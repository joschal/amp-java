package de.joschal.amp.core.outbound.layer2;

import de.joschal.amp.core.entities.messages.AbstractMessage;

public interface IDataLinkSender {

    void sendMessage(AbstractMessage message);

}

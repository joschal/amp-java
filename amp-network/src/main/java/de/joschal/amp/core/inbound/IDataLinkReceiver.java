package de.joschal.amp.core.inbound;

import de.joschal.amp.core.entities.AbstractMessage;

public interface IDataLinkReceiver {

    void receiveMessage(AbstractMessage message);

}

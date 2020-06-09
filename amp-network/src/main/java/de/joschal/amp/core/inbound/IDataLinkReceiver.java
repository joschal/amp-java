package de.joschal.amp.core.inbound;

import de.joschal.amp.core.entities.AbstractMessage;

import java.util.List;

public interface IDataLinkReceiver {

    List<AbstractMessage> receiveMessage(AbstractMessage message);

}

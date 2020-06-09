package de.joschal.amp.core.outbound;

import de.joschal.amp.core.entities.AbstractMessage;

import java.util.List;

public interface IDataLinkSender {

    List<AbstractMessage> sendMessage(AbstractMessage message);

}

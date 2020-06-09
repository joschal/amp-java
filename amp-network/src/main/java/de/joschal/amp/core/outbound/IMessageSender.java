package de.joschal.amp.core.outbound;

import de.joschal.amp.core.entities.AbstractMessage;

import java.util.List;

public interface IMessageSender {

    List<AbstractMessage> sendMessage(AbstractMessage abstractMessage);
}

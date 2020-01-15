package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.AbstractMessage;

import java.util.List;

public interface IMessageSender {

    List<AbstractMessage> sendMessage(AbstractMessage abstractMessage);
}

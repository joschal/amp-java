package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.AbstractMessage;

import java.util.Optional;

public interface IMessageSender {

    Optional<AbstractMessage> sendMessage(AbstractMessage abstractMessage);
}

package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.AbstractMessage;

import java.util.Optional;

public interface IDataLinkSender {

    Optional<AbstractMessage> sendMessage(AbstractMessage message);

}

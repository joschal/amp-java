package de.joschal.mdp.core.inbound;

import de.joschal.mdp.core.entities.AbstractMessage;

import java.util.Optional;

public interface IDataLinkReceiver {

    Optional<AbstractMessage> receiveMessage(AbstractMessage message);

}

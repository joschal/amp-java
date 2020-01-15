package de.joschal.mdp.core.inbound;

import de.joschal.mdp.core.entities.AbstractMessage;

import java.util.List;

public interface IDataLinkReceiver {

    List<AbstractMessage> receiveMessage(AbstractMessage message);

}

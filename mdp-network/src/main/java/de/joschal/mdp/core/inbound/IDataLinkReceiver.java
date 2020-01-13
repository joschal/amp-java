package de.joschal.mdp.core.inbound;

import de.joschal.mdp.core.entities.AbstractMessage;

public interface IDataLinkReceiver {

    boolean receiveMessage(AbstractMessage message);

}

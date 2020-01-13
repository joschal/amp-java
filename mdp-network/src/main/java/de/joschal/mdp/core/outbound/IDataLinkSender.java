package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.AbstractMessage;

public interface IDataLinkSender {

    boolean sendMessage(AbstractMessage message);

}

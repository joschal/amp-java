package de.joschal.mdp.sim.core.inbound;

import de.joschal.mdp.core.entities.AbstractMessage;

public interface IDatagramController {

    void sendDatagram(AbstractMessage datagram);

}

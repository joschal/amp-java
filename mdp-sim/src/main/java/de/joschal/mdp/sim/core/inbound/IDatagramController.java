package de.joschal.mdp.sim.core.inbound;

import de.joschal.mdp.core.entities.protocol.AbstractMessage;

public interface IDatagramController {

    void sendDatagram(AbstractMessage datagram);

}

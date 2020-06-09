package de.joschal.amp.sim.core.inbound;

import de.joschal.amp.core.entities.AbstractMessage;

public interface IDatagramController {

    void sendDatagram(AbstractMessage datagram);

}

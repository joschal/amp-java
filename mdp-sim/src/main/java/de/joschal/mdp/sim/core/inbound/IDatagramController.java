package de.joschal.mdp.sim.core.inbound;

import de.joschal.mdp.core.entities.protocol.Datagram;

public interface IDatagramController {

    void sendDatagram(Datagram datagram);

}

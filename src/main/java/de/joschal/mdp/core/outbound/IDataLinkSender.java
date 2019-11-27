package de.joschal.mdp.core.outbound;

import de.joschal.mdp.core.entities.protocol.Datagram;

public interface IDataLinkSender {

    boolean sendDatagram(Datagram datagram);
}

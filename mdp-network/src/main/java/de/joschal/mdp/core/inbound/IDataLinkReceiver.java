package de.joschal.mdp.core.inbound;

import de.joschal.mdp.core.entities.protocol.Datagram;

public interface IDataLinkReceiver {

    boolean receiveDatagram(Datagram datagram);

}

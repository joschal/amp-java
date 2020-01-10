package de.joschal.mdp.core.inbound;

import de.joschal.mdp.core.entities.protocol.addressing.AbstractAddressingMessage;
import de.joschal.mdp.core.entities.protocol.control.AbstractControlMessage;
import de.joschal.mdp.core.entities.protocol.data.AbstractDataMessage;
import de.joschal.mdp.core.entities.protocol.routing.AbstractRoutingMessage;

// Distinguish between message types
public interface INetworkReceiver {

    boolean receiveAddressingMessage(AbstractAddressingMessage message);

    boolean receiveControlMessage(AbstractControlMessage message);

    boolean receiveDataMessage(AbstractDataMessage message);

    boolean receiveRoutingMessage(AbstractRoutingMessage message);

}

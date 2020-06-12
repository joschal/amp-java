package de.joschal.amp.core.inbound;

import de.joschal.amp.core.entities.AbstractForwardableMessage;
import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.network.NetworkInterface;

import java.util.Optional;

public interface IForwardableMessageReceiver {

        void handleMessage(AbstractForwardableMessage message, NetworkInterface source);
}

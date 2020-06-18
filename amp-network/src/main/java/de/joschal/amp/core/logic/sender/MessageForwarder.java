package de.joschal.amp.core.logic.sender;

import de.joschal.amp.core.entities.messages.AbstractForwardableMessage;
import de.joschal.amp.core.entities.network.routing.AbstractRouter;
import de.joschal.amp.io.NetworkInterface;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageForwarder {

    private AbstractRouter router;
    private MessageSender messageSender;

    public void forwardMessage(AbstractForwardableMessage message, NetworkInterface source) {

        // Drop message if hop limit is reached
        if (message.getHopCounter() >= message.getHopLimit()) {
            return;
        }

        // special case for route discoveries

        // forward with known route
        router.getRoute(message).ifPresentOrElse(
                // Route is found
                route -> route.getNetworkInterface().sendMessage(message),

                // No route found -> Flooding
                () -> messageSender.floodMessage(message, source)
        );

    }
}

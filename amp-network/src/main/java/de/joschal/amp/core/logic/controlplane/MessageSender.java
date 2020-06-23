package de.joschal.amp.core.logic.controlplane;

import de.joschal.amp.core.entities.messages.AbstractForwardableMessage;
import de.joschal.amp.core.entities.messages.AbstractMessage;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.routing.AbstractRouter;
import de.joschal.amp.core.entities.network.routing.Route;
import de.joschal.amp.core.outbound.layer3.IMessageSender;
import de.joschal.amp.io.NetworkInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class MessageSender implements IMessageSender {

    private AbstractNode node;
    private AbstractRouter router;
    private List<NetworkInterface> networkInterfaces;

    /**
     * Sends message via best known route
     *
     * @param message message to send
     * @return true if message was send, false if no route exists
     */
    @Override
    public boolean sendMessageViaKnownRoute(AbstractMessage message) {
        log.debug("Sending message: {}", message);

        Optional<Route> route = router.getRoute(message);

        if (route.isPresent()) {
            route.get().getNetworkInterface().sendMessage(message);
            return true;
        } else {
            log.error("No route found");
            return false;
        }
    }

    /**
     * Sends message to direct neighbor
     *
     * @param message          message to send
     * @param networkInterface network interface to neighbor
     * @return true if message was send, false of not
     */
    @Override
    public boolean sendMessageToNeighbor(AbstractMessage message, NetworkInterface networkInterface) {

        // There are unspecified addresses -> Setup process
        // direct communicaiton over interface is allowed
        if (message.getDestinationAddress().getValue() == 0 || message.getSourceAddress().getValue() == 0) {
            networkInterface.sendMessage(message);
            return true;
        }

        // link local messages are only allowed if the source is the node itself
        if (message.getSourceAddress().getValue() == this.node.getAddress().getValue()) {

            Optional<Route> route = router.getRoute(message);

            // Validate the network interface to a known route
            if (route.isPresent()) {
                if (route.get().networkInterface.equals(networkInterface)) {
                    networkInterface.sendMessage(message);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Floods message via all interfaces, except the source
     * Messages are cloned before flooding, so that they are individual objects, instead of the same entity
     *
     * @param message message to flood
     * @param source  source interface is omitted in flooding. If null, message is flooded via all interfaces
     */
    @Override
    public void floodMessage(AbstractMessage message, NetworkInterface source) {
        networkInterfaces.stream()
                // Do not send the message to it's source
                .filter(networkInterface -> !networkInterface.equals(source))
                .filter(networkInterface -> {
                    if (message instanceof AbstractForwardableMessage) {
                        /*AbstractForwardableMessage abstractForwardableMessage = (AbstractForwardableMessage) message;
                        if (Collections.frequency(abstractForwardableMessage.getTracerouteList(), this.nodeId) > 1) {
                            return false;
                        }*/
                        Optional<Route> optionalRoute = router.getRoute(message.getSourceAddress());

                        if (optionalRoute.isPresent()) {
                            //route is known
                            Route route = optionalRoute.get();
                            AbstractForwardableMessage forwardableMessage = (AbstractForwardableMessage) message;

                            if (forwardableMessage.getHopCounter() <= route.hops) {
                                // taken path is better than known route. Flood message
                                return true;
                            } else {
                                // Message should not be forwarded. Taken path is inefficient;
                                return false;
                            }
                        } else {
                            // no route is known
                            return true;
                        }
                    }
                    // message is not forwardable -> can be flooded
                    return true;
                })
                .forEach(networkInterface -> networkInterface.sendMessage(SerializationUtils.clone(message)));
    }
}

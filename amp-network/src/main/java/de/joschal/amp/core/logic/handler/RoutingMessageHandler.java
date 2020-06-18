package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.messages.AbstractForwardableMessage;
import de.joschal.amp.core.entities.messages.routing.RouteDiscovery;
import de.joschal.amp.core.entities.messages.routing.RouteReply;
import de.joschal.amp.core.inbound.layer3.IForwardableMessageReceiver;
import de.joschal.amp.core.logic.jobs.JobManager;
import de.joschal.amp.core.logic.jobs.RouteDiscoveryJob;
import de.joschal.amp.core.logic.sender.MessageSender;
import de.joschal.amp.io.NetworkInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RoutingMessageHandler implements IForwardableMessageReceiver {

    private MessageSender messageSender;
    private JobManager jobManager;

    @Override
    public void handleMessage(AbstractForwardableMessage message, NetworkInterface source) {

        if (message instanceof RouteDiscovery) {
            handleRouteDiscovery((RouteDiscovery) message);
        } else if (message instanceof RouteReply) {
            handleRouteReply((RouteReply) message);
        } else {
            log.error("not implemented");
        }


    }

    private void handleRouteReply(RouteReply message) {
        log.info("Received route reply message: {}", (message));
        RouteDiscoveryJob routeDiscoveryJob = jobManager.getRouteDiscoveryJobs().get(message.getSourceAddress());

        if (routeDiscoveryJob != null) {
            routeDiscoveryJob.tearDown();
            jobManager.getRouteDiscoveryJobs().remove(message.getSourceAddress());
        }

    }

    private void handleRouteDiscovery(RouteDiscovery message) {
        log.info("received a route discovery message {}", message);
        RouteReply routeReply = new RouteReply(message);
        messageSender.sendMessageViaKnownRoute(routeReply);
    }
}

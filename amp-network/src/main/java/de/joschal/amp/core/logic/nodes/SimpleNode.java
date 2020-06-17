package de.joschal.amp.core.logic.nodes;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.amp.core.entities.messages.data.Datagram;
import de.joschal.amp.core.entities.messages.routing.RouteDiscovery;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.AbstractRouter;
import de.joschal.amp.core.logic.jobs.RouteDiscoveryJob;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ToString
public class SimpleNode extends AbstractNode {

    public SimpleNode(String id, AddressPool... addressPools) {
        super(id, addressPools);
    }

    private String receivedMessage = null;

    @Override
    public void receiveDatagram(String payload, Address source) {
        log.info("Business logic received data message: {}", payload);
        this.receivedMessage = payload;
    }

    @Override
    public boolean receiveAcknowledgedDatagram(String payload, Address source) {
        log.info("Business logic received data message: {}", payload);
        return true;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    // For Testing
    public void sendDatagram(String message, Address destination) {
        log.info("Action triggered in node {}", this.id);

        Datagram datagram = new Datagram(this.address, destination, 42, message);
        boolean send = this.messageSender.sendMessageViaKnownRoute(datagram);

        if (!send) {
            RouteDiscovery routeDiscovery = new RouteDiscovery(this.address, destination, 10);
            messageSender.floodMessage(routeDiscovery, null);

            RouteDiscoveryJob routeDiscoveryJob = new RouteDiscoveryJob(datagram, this.messageSender);
            this.jobManager.getRouteDiscoveryJobs().put(routeDiscovery.getDestinationAddress(), routeDiscoveryJob);
        }
    }
}

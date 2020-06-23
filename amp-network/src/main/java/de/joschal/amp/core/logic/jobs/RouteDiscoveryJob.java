package de.joschal.amp.core.logic.jobs;

import de.joschal.amp.core.entities.messages.AbstractMessage;
import de.joschal.amp.core.entities.messages.data.AbstractDataMessage;
import de.joschal.amp.core.logic.controlplane.MessageSender;
import de.joschal.amp.io.NetworkInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This job waits for a successful route discovery and sends a message when a route was found
 */
@Slf4j
@AllArgsConstructor
public class RouteDiscoveryJob implements IJob {

    private AbstractDataMessage dataMessage;
    private MessageSender messageSender;

    @Override
    public void init() {

    }

    @Override
    public void tick() {
    }

    @Override
    public void tearDown() {
        log.info("Received route reply from {}", dataMessage.getDestinationAddress());
        messageSender.sendMessageViaKnownRoute(dataMessage);

    }

    @Override
    public void receiveMessage(AbstractMessage message, NetworkInterface source) {

    }
}

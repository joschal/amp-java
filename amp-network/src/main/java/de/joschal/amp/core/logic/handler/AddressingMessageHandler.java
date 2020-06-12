package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.messages.addressing.PoolAccepted;
import de.joschal.amp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.amp.core.entities.messages.addressing.PoolAssigned;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.NetworkInterface;
import de.joschal.amp.core.inbound.ILinkLocalMessageReceiver;
import de.joschal.amp.core.logic.jobs.JobManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AddressingMessageHandler implements ILinkLocalMessageReceiver {

    private JobManager jobManager;

    @Override
    public void handleMessage(AbstractMessage message, NetworkInterface source) {

        if (message instanceof PoolAdvertisement) {
            handlePoolAdvertisement((PoolAdvertisement) message);
        } else if (message instanceof PoolAccepted) {
            handlePoolAccepted((PoolAccepted) message);
        } else if (message instanceof PoolAssigned) {
            handlePoolAssigned((PoolAssigned) message);
        }

        // Not all messages implemented for simple simulaiton
        log.error("Not Implemented");
    }


    private void handlePoolAdvertisement(PoolAdvertisement message) {
        jobManager.getAddressAcquisitionJob().receiveMessage(message);
    }

    private void handlePoolAccepted(PoolAccepted message) {
        jobManager.getAddressAcquisitionJob().receiveMessage(message);
    }

    private void handlePoolAssigned(PoolAssigned message) {
        jobManager.getAddressAcquisitionJob().receiveMessage(message);
    }
}

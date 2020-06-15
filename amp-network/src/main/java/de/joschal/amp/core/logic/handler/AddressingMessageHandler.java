package de.joschal.amp.core.logic.handler;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.messages.addressing.PoolAccepted;
import de.joschal.amp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.amp.core.entities.messages.addressing.PoolAssigned;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.NetworkInterface;
import de.joschal.amp.core.inbound.ILinkLocalMessageReceiver;
import de.joschal.amp.core.logic.AddressManager;
import de.joschal.amp.core.logic.jobs.JobManager;
import de.joschal.amp.core.logic.sender.MessageSender;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class AddressingMessageHandler implements ILinkLocalMessageReceiver {

    private JobManager jobManager;
    private AddressManager addressManager;
    private MessageSender messageSender;

    @Override
    public void handleMessage(AbstractMessage message, NetworkInterface source) {

        if (message instanceof PoolAdvertisement) {
            handlePoolAdvertisement((PoolAdvertisement) message, source);
        } else if (message instanceof PoolAccepted) {
            handlePoolAccepted((PoolAccepted) message, source);
        } else if (message instanceof PoolAssigned) {
            handlePoolAssigned((PoolAssigned) message, source);
        } else {
            // Not all messages implemented for simple simulaiton
            log.error("Not Implemented {}", message.getClass());
        }
    }


    private void handlePoolAdvertisement(PoolAdvertisement message, NetworkInterface source) {
        jobManager.getAddressAcquisitionJob().receiveMessage(message, source);
    }

    private void handlePoolAccepted(PoolAccepted message, NetworkInterface source) {

        AddressPool addressPool = this.addressManager.getAssignedRanges().get(source);

        if (addressPool != null) {
            List<AddressPool> poolList = new LinkedList<>();
            poolList.add(addressPool);
            this.messageSender.sendMessageToNeighbor(new PoolAssigned(message, poolList), source);
        }

    }

    private void handlePoolAssigned(PoolAssigned message, NetworkInterface source) {
        jobManager.getAddressAcquisitionJob().receiveMessage(message, source);
    }
}

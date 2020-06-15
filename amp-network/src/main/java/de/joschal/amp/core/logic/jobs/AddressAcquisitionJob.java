package de.joschal.amp.core.logic.jobs;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.messages.addressing.AbstractAddressingMessage;
import de.joschal.amp.core.entities.messages.addressing.PoolAccepted;
import de.joschal.amp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.amp.core.entities.messages.addressing.PoolAssigned;
import de.joschal.amp.core.entities.messages.control.Hello;
import de.joschal.amp.core.entities.network.NetworkInterface;
import de.joschal.amp.core.logic.AddressManager;
import de.joschal.amp.core.logic.sender.MessageSender;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * This class implements the asynchronous ZAL/AQ algorithm
 */
@Slf4j
public class AddressAcquisitionJob implements IJob {

    public AddressAcquisitionJob(JobManager jobManager, List<NetworkInterface> networkInterfaces, int maxTicks, MessageSender messageSender, AddressManager addressManager) {
        this.jobManager = jobManager;
        this.networkInterfaces = networkInterfaces;
        this.maxTicks = maxTicks;
        this.messageSender = messageSender;
        this.addressManager = addressManager;
    }

    // Populated by constructor
    private JobManager jobManager;
    private List<NetworkInterface> networkInterfaces;
    private int maxTicks;
    private MessageSender messageSender;
    private AddressManager addressManager;

    // used internally by algorithm
    private HashMap<PoolAdvertisement, NetworkInterface> advertisements = new HashMap<>();
    private PoolAdvertisement acceptedPoolAdvertisement = null;
    private PoolAssigned poolAssigned = null;

    // Used to keep track of the number of invocations
    private int waitForAdvertisementsTickCounter = 0;
    private int waitForAssignmentTickCounter = 0;

    @Override
    public void init() {
        this.waitForAdvertisementsTickCounter = 0;
        this.waitForAssignmentTickCounter = 0;
        this.advertisements = new HashMap<>();
        this.acceptedPoolAdvertisement = null;
        this.poolAssigned = null;
    }

    @Override
    public void tick() {

        // pools were assigned. Process them locally
        // algorithm is finished. Job is torn down
        if (poolAssigned != null) {
            processPoolAssigned();
            tearDown();
            return;
        }


        // Waiting for assignment timed out
        if (waitForAssignmentTickCounter >= maxTicks) {
            // remove pool which was requested but not assigned
            this.advertisements.remove(this.acceptedPoolAdvertisement);
            this.acceptedPoolAdvertisement = null;
            waitForAssignmentTickCounter = 0;

            // if no advertisements received, reset job
            if (advertisements.isEmpty()) {
                init();
                messageSender.floodMessage(new Hello(Address.undefined()), null);
                return;
            }

            // if other advertisements available, select another pool
            selectPoolAndAccept();
            return;

        }

        // Wait for assignment of requested pools
        if (acceptedPoolAdvertisement != null) {
            waitForAssignmentTickCounter++;
            return;
        }

        processPoolAdvertisements();
    }

    private void processPoolAdvertisements() {
        // check if all neigbors send an advertisement
        // OR
        // expiry of predefined waiting time
        if (advertisements.size() == networkInterfaces.size() || waitForAdvertisementsTickCounter >= maxTicks) {

            // Timer timed out, no messages were received -> reset job
            if (advertisements.size() == 0) {
                log.error("Received no address advertisements. Will reset and restart.");
                init();
                messageSender.floodMessage(new Hello(Address.undefined()), null);
                return;
            }

            // messages were received. Select and assign the best pool;
            selectPoolAndAccept();
            return;
        }

        // nothing happened...
        waitForAdvertisementsTickCounter++;
    }

    private void selectPoolAndAccept() {
        // select best
        List<PoolAdvertisement> advList = new LinkedList<>(this.advertisements.keySet());
        advList.sort(Comparator.reverseOrder());

        this.acceptedPoolAdvertisement = advList.get(0);

        // Send pool accepted message
        PoolAccepted poolAccepted = new PoolAccepted(this.acceptedPoolAdvertisement);
        messageSender.sendMessageToNeighbor(poolAccepted, this.advertisements.get(this.acceptedPoolAdvertisement));
    }

    private void processPoolAssigned() {

        // Use address pools locally and assign an address to self
        this.addressManager.addAddressPools(this.poolAssigned.getAddressPools());
        this.addressManager.assignAddressToSelf();
    }

    public void receiveMessage(AbstractAddressingMessage message, NetworkInterface source) {
        if (message instanceof PoolAdvertisement) {
            // add advertisement to local cache
            advertisements.put((PoolAdvertisement) message, source);
        } else if (message instanceof PoolAssigned) {
            this.poolAssigned = (PoolAssigned) message;
        }
    }

    @Override
    public void tearDown() {
        this.jobManager.addressAcquisitionJob = null;
    }
}

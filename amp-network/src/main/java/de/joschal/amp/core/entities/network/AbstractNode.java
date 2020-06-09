package de.joschal.amp.core.entities.network;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.messages.addressing.PoolAccepted;
import de.joschal.amp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.amp.core.entities.messages.addressing.PoolAssigned;
import de.joschal.amp.core.entities.messages.control.Hello;
import de.joschal.amp.core.logic.AddressManager;
import de.joschal.amp.core.logic.handler.AddressingMessageHandler;
import de.joschal.amp.core.logic.handler.RoutingMessageHandler;
import de.joschal.amp.core.logic.sender.AddressingMessageSender;
import de.joschal.amp.core.logic.handler.ControlMessageHandler;
import de.joschal.amp.core.logic.handler.DataMessageHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Getter
public abstract class AbstractNode {

    public AbstractNode(String id, AbstractRouter router, AddressPool... addressPools) {

        // Basics
        this.id = id;
        this.address = new Address(0);
        this.router = router;
        this.router.setNode(this); // Set reference to self
        this.addressManager = new AddressManager(this, addressPools);

        // Message Handler
        this.addressingMessageHandler = new AddressingMessageHandler(this);
        this.controlMessageHandler = new ControlMessageHandler(this);
        this.dataMessageHandler = new DataMessageHandler();
        this.routingMessageHandler = new RoutingMessageHandler(this);

        // Message Sender
        this.addressingMessageSender = new AddressingMessageSender(this);
    }

    // Basics
    protected String id;
    protected Address address;
    protected List<NetworkInterface> networkInterfaces = new ArrayList<>();
    protected AbstractRouter router;
    protected AddressManager addressManager;

    // Message Handler
    public AddressingMessageHandler addressingMessageHandler;
    public ControlMessageHandler controlMessageHandler;
    public DataMessageHandler dataMessageHandler;
    public RoutingMessageHandler routingMessageHandler;

    // MessageSender
    public AddressingMessageSender addressingMessageSender;

    public void addNetworkInterface(NetworkInterface networkInterface) {
        networkInterface.setNode(this);
        this.networkInterfaces.add(networkInterface);
    }

    public void addRoute(Route route) {
        this.router.routingTable.add(route);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void bootSequence() {

        // if there is an addressPool available, use an address from this pool
        if (addressManager.isAPoolAvailable()) {
            this.addressManager.assignAddressToSelf();

            // if there is no pool available, request some from adjacent nodes
        } else {

            List<PoolAdvertisement> advertisements = requestPoolAdvertisements();
            List<AddressPool> assignedPools = getBestAddressPools(advertisements);

            if (assignedPools != null) {
                this.addressManager.addAddressPools(assignedPools);
                this.addressManager.assignAddressToSelf();
                return;
            }

            if (advertisements.size() == 0) {
                log.info("No advertisements received");
                return;
            }
            throw new RuntimeException("Could not assign address pools");
        }
    }

    /**
     * Requests Address Pool Advertisements from adjacent nodes
     *
     * @return List of all received PoolAdvertisement messages
     */
    private List<PoolAdvertisement> requestPoolAdvertisements() {

        List<PoolAdvertisement> advertisements = new LinkedList<>();

        for (NetworkInterface networkInterface : this.networkInterfaces) {
            List<AbstractMessage> responses = networkInterface.sendMessage(new Hello());

            for (AbstractMessage message : responses) {
                advertisements.add((PoolAdvertisement) message);
                Route route = new Route(networkInterface, message.getSourceAddress(), 1);
                this.router.addRoute(route);
                log.info("Received address pool advertisement: {}", responses);
            }

        }

        advertisements.sort(Comparator.reverseOrder());
        return advertisements;
    }

    /**
     * @param advertisements List of all received PoolAdvertisement messages
     * @return Largest available AddressPool
     */
    private List<AddressPool> getBestAddressPools(List<PoolAdvertisement> advertisements) {
        for (PoolAdvertisement advertisement : advertisements) {
            PoolAccepted accepted = new PoolAccepted(advertisement);
            List<AbstractMessage> responses = this.router.sendMessage(accepted);

            for (AbstractMessage response : responses) {
                PoolAssigned assigned = (PoolAssigned) response;
                if (assigned.isAssigned()) {
                    return assigned.getAddressPools();
                }
            }
        }
        log.error("None of the desired address pools could be assigned");
        return null;
    }

    public List<AbstractNode> getNeighbours() {

        List<AbstractNode> neighbors = new ArrayList<>();

        for (NetworkInterface networkInterface : this.networkInterfaces) {

            AbstractNode A = networkInterface.getDataLink().getA().getNode();
            AbstractNode B = networkInterface.getDataLink().getB().getNode();

            if (A.equals(this)) {
                neighbors.add(B);
            } else if (B.equals(this)) {
                neighbors.add(A);
            } else {
                throw new RuntimeException("Something went horribly wrong");
            }
        }

        return neighbors;

    }

}

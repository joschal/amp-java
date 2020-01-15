package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.AbstractMessage;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.entities.messages.addressing.PoolAccepted;
import de.joschal.mdp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.mdp.core.entities.messages.addressing.PoolAssigned;
import de.joschal.mdp.core.entities.messages.control.Hello;
import de.joschal.mdp.core.logic.AddressManager;
import de.joschal.mdp.core.logic.handler.AddressingMessageHandler;
import de.joschal.mdp.core.logic.handler.ControlMessageHandler;
import de.joschal.mdp.core.logic.handler.DataMessageHandler;
import de.joschal.mdp.core.logic.handler.RoutingMessageHandler;
import de.joschal.mdp.core.logic.sender.AddressingMessageSender;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public abstract class AbstractNode {

    public AbstractNode(String id, AbstractRouter router, AddressPool ... addressPools) {

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
        this.routingMessageHandler = new RoutingMessageHandler();

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


    public List<NetworkInterface> getInterfaces() {
        return networkInterfaces;
    }

    public String getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    @Deprecated // Should not be used, since address should be acquired automatically
    public void setAddress(Address address) {
        this.address = address;
    }

    // Must be overridden by runtime class
    public boolean acquireAddress() {
        log.error("Address acquisition not Implemented");
        return false;
    }

    public AddressManager getAddressManager() {
        return addressManager;
    }

    public void bootSequence() {

        // Get address from
        Address selfAssigned = this.addressManager.assignAddressToSelf();

        if (selfAssigned == null) {

            List<PoolAdvertisement> advertisements = requestPoolAdvertisements();

            AddressPool assignedPool = getBestAddressPool(advertisements);

            if (assignedPool != null) {
                this.addressManager.addAddressPool(assignedPool);
                this.setAddress(this.addressManager.assignAddressToSelf());
                return;
            }
            log.error("Could not assign address");
            return;
        }
        this.setAddress(selfAssigned);
    }

    /**
     * Requests Address Pool Advertisements from adjacent nodes
     *
     * @return List of all received PoolAdvertisement messages
     */
    private List<PoolAdvertisement> requestPoolAdvertisements() {

        List<PoolAdvertisement> advertisements = new LinkedList<>();

        for (NetworkInterface networkInterface : this.networkInterfaces) {
            Optional<AbstractMessage> response = networkInterface.sendMessage(new Hello());

            response.ifPresent(message -> {
                advertisements.add((PoolAdvertisement) message);
                this.router.addRoute(new Route(networkInterface, message.getSourceAddress(), 1));
                log.info("Received address pool advertisement: {}");
            });

        }

        advertisements.sort(Comparator.reverseOrder());
        return advertisements;
    }

    /**
     * @param advertisements List of all received PoolAdvertisement messages
     * @return Largest available AddressPool
     */
    private AddressPool getBestAddressPool(List<PoolAdvertisement> advertisements) {
        for (PoolAdvertisement advertisement : advertisements) {
            PoolAccepted accepted = new PoolAccepted(advertisement);
            Optional<AbstractMessage> response = this.router.sendMessage(accepted);

            if (response.isPresent()) {
                PoolAssigned assigned = (PoolAssigned) response.get();
                if (assigned.isAssigned()) {
                    return assigned.getAddressPool();
                }
            }
        }
        log.error("None of the desired address pools could be assigned");
        return null;
    }

}

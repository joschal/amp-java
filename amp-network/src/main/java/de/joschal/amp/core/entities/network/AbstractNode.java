package de.joschal.amp.core.entities.network;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.messages.control.Hello;
import de.joschal.amp.core.logic.AddressManager;
import de.joschal.amp.core.logic.handler.AddressingMessageHandler;
import de.joschal.amp.core.logic.handler.ControlMessageHandler;
import de.joschal.amp.core.logic.handler.DataMessageHandler;
import de.joschal.amp.core.logic.handler.RoutingMessageHandler;
import de.joschal.amp.core.logic.jobs.AddressAcquisitionJob;
import de.joschal.amp.core.logic.jobs.JobManager;
import de.joschal.amp.core.logic.router.Router;
import de.joschal.amp.core.logic.sender.MessageForwarder;
import de.joschal.amp.core.logic.sender.MessageSender;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
public abstract class AbstractNode {

    public AbstractNode(String id, AddressPool... addressPools) {

        // Basics
        this.id = id;
        this.address = Address.undefined();
        this.router = new Router(this.address, this.networkInterfaces);
        this.addressManager = new AddressManager(this, addressPools);
        this.messageSender = new MessageSender(this.address, this.router, this.networkInterfaces, this.getId());
        this.messageForwarder = new MessageForwarder(this.router, this.messageSender);
        this.jobManager = new JobManager();

        // Message Handler
        this.addressingMessageHandler = new AddressingMessageHandler(this.jobManager, this.addressManager, this.messageSender);
        this.controlMessageHandler = new ControlMessageHandler(this.address, this.addressManager, this.messageSender, this.router);
        this.dataMessageHandler = new DataMessageHandler(this, this.messageSender);
        this.routingMessageHandler = new RoutingMessageHandler(this.address, this.messageSender, this.jobManager);
    }

    // Basics
    protected String id;
    protected Address address;
    protected List<NetworkInterface> networkInterfaces = new ArrayList<>();
    protected AbstractRouter router;
    protected AddressManager addressManager;
    protected MessageForwarder messageForwarder;

    // Message Handler
    protected AddressingMessageHandler addressingMessageHandler;
    protected ControlMessageHandler controlMessageHandler;
    protected DataMessageHandler dataMessageHandler;
    protected RoutingMessageHandler routingMessageHandler;

    // MessageSender
    protected MessageSender messageSender;

    // Aynchonous Job Handling
    protected JobManager jobManager;

    public void addNetworkInterface(NetworkInterface networkInterface) {
        networkInterface.setNode(this);
        this.networkInterfaces.add(networkInterface);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Either assigns an address to itself or requests addresses from neigbors
     */
    public void bootSequence() {

        // if there is an addressPool available, use an address from this pool
        if (addressManager.isAPoolAvailable()) {
            this.addressManager.assignAddressToSelf();

        } else {

            // if there is no pool available, request some from adjacent nodes
            // by flooding an empty hello message to all neigbors
            // processing is done asyncronous by the AddressAcquisitionJob
            Hello hello = new Hello();
            messageSender.floodMessage(hello, null);

            AddressAcquisitionJob addressAcquisitionJob = new AddressAcquisitionJob(this.jobManager, this.networkInterfaces, 5, this.messageSender, this.addressManager);
            this.jobManager.setAddressAcquisitionJob(addressAcquisitionJob);

        }
    }

    /**
     * This method provides a common interface for the business logic to receive datagrams
     *
     * @param payload The payload of the receoved message
     * @param source  The source address of the received message
     */
    public abstract void receiveDatagram(String payload, Address source);

    /**
     * This method provides a common interface for the business logic to receive acknowledged datagrams
     *
     * @param payload The payload of the receoved message
     * @param source  The source address of the received message
     * @return true if message should be acknowledged, false if not
     */
    public abstract boolean receiveAcknowledgedDatagram(String payload, Address source);

    /**
     * Utility Method
     *
     * @return Returns a list of references to all its immediate neigbors
     */
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

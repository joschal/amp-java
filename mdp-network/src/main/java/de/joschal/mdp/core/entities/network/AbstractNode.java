package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.logic.AddressManager;
import de.joschal.mdp.core.logic.handler.AddressingMessageHandler;
import de.joschal.mdp.core.logic.handler.ControlMessageHandler;
import de.joschal.mdp.core.logic.handler.DataMessageHandler;
import de.joschal.mdp.core.logic.handler.RoutingMessageHandler;
import de.joschal.mdp.core.logic.sender.AddressingMessageSender;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractNode {

    public AbstractNode(String id, AbstractRouter router, AddressPool ... addressPools) {

        // Basics
        this.id = id;
        this.router = router;
        this.router.setNode(this); // Set reference to self
        this.addressManager = new AddressManager(addressPools);

        // Message Handler
        this.addressingMessageHandler = new AddressingMessageHandler();
        this.controlMessageHandler = new ControlMessageHandler(this);
        this.dataMessageHandler = new DataMessageHandler();
        this.routingMessageHandler = new RoutingMessageHandler();

        // Message Sender
        this.addressingMessageSender = new AddressingMessageSender(this);
    }

    // Basics
    protected String id;
    protected Address address;
    private List<NetworkInterface> dataNetworkInterfaces = new ArrayList<>();
    protected AbstractRouter router;
    public AddressManager addressManager;

    // Message Handler
    public AddressingMessageHandler addressingMessageHandler;
    public ControlMessageHandler controlMessageHandler;
    public DataMessageHandler dataMessageHandler;
    public RoutingMessageHandler routingMessageHandler;

    // MessageSender
    public AddressingMessageSender addressingMessageSender;

    public void addNetworkInterface(NetworkInterface networkInterface) {
        networkInterface.setNode(this);
        this.dataNetworkInterfaces.add(networkInterface);
    }

    public void addRoute(Route route) {
        this.router.routingTable.add(route);
    }


    public List<NetworkInterface> getInterfaces() {
        return dataNetworkInterfaces;
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


}

package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.inbound.INetworkReceiver;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractNode implements INetworkReceiver {

    public AbstractNode(String id, AbstractRouter router) {
        this.id = id;
        this.router = router;
        this.router.setNode(this); // Set reference to self
    }

    @Deprecated // Should not be used, since address should be acquired automatically
    public AbstractNode(String id, Address address, AbstractRouter router) {
        this.id = id;
        this.address = address;
        this.router = router;
        this.router.setNode(this); // Set reference to self
    }

    protected String id;

    protected Address address;

    private List<NetworkInterface> dataNetworkInterfaces = new ArrayList<>();

    protected AbstractRouter router;

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

    @Deprecated // Should not be used, since address should be acquired automatically
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

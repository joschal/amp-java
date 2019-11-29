package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.inbound.INetworkReceiver;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNode implements INetworkReceiver {

    public AbstractNode(String id, AbstractRouter router) {
        this.id = id;
        this.router = router;
        this.router.setNode(this); // Set reference to self
    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

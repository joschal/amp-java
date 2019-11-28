package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.inbound.INetworkReceiver;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNode implements INetworkReceiver {

    public AbstractNode(Address address, AbstractRouter router) {
        this.address = address;
        this.router = router;
        this.router.setNode(this); // Set reference to self
    }

    protected Address address;

    private List<NetworkInterface> dataNetworkInterfaces = new ArrayList<>();

    protected AbstractRouter router;

    public void addNetworkInterface(NetworkInterface networkInterface) {
        networkInterface.setNode(this);
        this.dataNetworkInterfaces.add(networkInterface);
    }

    public void addRoute(Route route){
        this.router.routingTable.add(route);
    }

    public Address getAddress() {
        return address;
    }

    public List<NetworkInterface> getInterfaces() {
        return dataNetworkInterfaces;
    }
}

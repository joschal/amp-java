package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.inbound.INetworkReceiver;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNode implements INetworkReceiver {

    public AbstractNode(Address address, AbstractRouter router) {
        this.address = address;
        this.router = router;
        this.router.setNode(this); // Set reference to self
    }

    protected Address address;

    private Set<NetworkInterface> dataNetworkInterfaces = new HashSet<>();

    protected AbstractRouter router;

    public void addNetworkInterface(NetworkInterface networkInterface) {
        networkInterface.setNode(this);
        this.dataNetworkInterfaces.add(networkInterface);
    }

    public Address getAddress() {
        return address;
    }

    public Set<NetworkInterface> getDataNetworkInterfaces() {
        return dataNetworkInterfaces;
    }
}

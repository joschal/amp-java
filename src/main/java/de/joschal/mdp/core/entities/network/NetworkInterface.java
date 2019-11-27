package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.protocol.Datagram;
import de.joschal.mdp.core.inbound.IDataLinkReceiver;
import de.joschal.mdp.core.outbound.IDataLinkSender;

public class NetworkInterface implements IDataLinkReceiver, IDataLinkSender {

    public NetworkInterface(String name) {
        this.name = name;
    }

    public NetworkInterface(String name, DataLink dataLink) {
        this.name = name;
        this.dataLink = dataLink;
    }

    private String name;
    private DataLink dataLink;
    private AbstractNode node;

    @Override
    public boolean receiveDatagram(Datagram datagram) {

        if (node.getAddress().equals(datagram.getDestinationAddress())) {
            return node.receiveFromNetwork(datagram.getPayload(), datagram.getSourceAddress());
        } else {
            return node.router.forwardDatagram(datagram);
        }
    }

    @Override
    public boolean sendDatagram(Datagram datagram) {
        return dataLink.exchange(datagram, this);
    }

    void setDataLink(DataLink dataLink) {
        this.dataLink = dataLink;
    }

    void setNode(AbstractNode node) {
        this.node = node;
    }
}

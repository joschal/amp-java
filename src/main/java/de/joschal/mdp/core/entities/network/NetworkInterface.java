package de.joschal.mdp.core.entities.network;

import de.joschal.mdp.core.entities.protocol.Datagram;
import de.joschal.mdp.core.inbound.IDataLinkReceiver;
import de.joschal.mdp.core.outbound.IDataLinkSender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetworkInterface implements IDataLinkReceiver, IDataLinkSender {

    public NetworkInterface(String name) {
        this.name = name;
    }

    private String name;
    private DataLink dataLink;
    private AbstractNode node;

    @Override
    public boolean receiveDatagram(Datagram datagram) {

        log.info("[{}] Received a datagram {}", this.node.getAddress(), datagram);
        if (datagram.triggerHopCounter()) {

            log.info("Datagram expired: {}", datagram);
            return false;

        } else {

            if (node.getAddress().equals(datagram.getDestinationAddress())) {
                return node.receiveFromNetwork(datagram.getPayload(), datagram.getSourceAddress());
            } else {
                return node.router.forwardDatagram(datagram);
            }
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

    public String getName() {
        return name;
    }
}

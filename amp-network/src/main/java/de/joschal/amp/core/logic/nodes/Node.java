package de.joschal.amp.core.logic.nodes;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.core.entities.network.addressing.AddressPool;

public class Node extends AbstractNode {

    public Node(String id, AddressPool... addressPools) {
        super(id, addressPools);
    }

    @Override
    public void receiveDatagram(String payload, Address source) {

    }

    @Override
    public boolean receiveAcknowledgedDatagram(String payload, Address source) {
        return false;
    }
}

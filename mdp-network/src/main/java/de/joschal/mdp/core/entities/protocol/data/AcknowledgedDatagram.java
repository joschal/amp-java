package de.joschal.mdp.core.entities.protocol.data;

import de.joschal.mdp.core.entities.protocol.Address;

public class AcknowledgedDatagram extends AbstractDataMessage {

    public AcknowledgedDatagram(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}

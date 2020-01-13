package de.joschal.mdp.core.entities.messages.data;

import de.joschal.mdp.core.entities.Address;

public class AcknowledgedDatagram extends AbstractDataMessage {

    public AcknowledgedDatagram(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}

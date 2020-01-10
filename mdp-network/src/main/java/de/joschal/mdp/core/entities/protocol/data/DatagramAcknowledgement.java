package de.joschal.mdp.core.entities.protocol.data;

import de.joschal.mdp.core.entities.protocol.Address;

public class DatagramAcknowledgement extends AbstractDataMessage {

    public DatagramAcknowledgement(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }

}

package de.joschal.mdp.core.entities.messages.data;

import de.joschal.mdp.core.entities.Address;
import lombok.ToString;

@ToString
public class DatagramAcknowledgement extends AbstractDataMessage {

    public DatagramAcknowledgement(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }

}

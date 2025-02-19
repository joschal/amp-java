package de.joschal.amp.core.entities.messages.control;

import de.joschal.amp.core.entities.network.addressing.Address;
import lombok.ToString;

@ToString
public class GoodbyeAck extends AbstractControlMessage {

    public GoodbyeAck(Address sourceAddress, Address destinationAddress) {
        super(sourceAddress, destinationAddress);
    }

}

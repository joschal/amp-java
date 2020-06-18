package de.joschal.amp.core.entities.messages.addressing;

import de.joschal.amp.core.entities.network.addressing.Address;
import lombok.ToString;

@ToString
public class BinCapacityRequest extends AbstractAddressingMessage {
    public BinCapacityRequest(Address sourceAddress, Address destinationAddress) {
        super(sourceAddress, destinationAddress);
    }
}

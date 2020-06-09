package de.joschal.amp.core.entities.messages.control;

import de.joschal.amp.core.entities.Address;
import lombok.ToString;

@ToString
public class Goodbye extends AbstractControlMessage {
    public Goodbye(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }
}

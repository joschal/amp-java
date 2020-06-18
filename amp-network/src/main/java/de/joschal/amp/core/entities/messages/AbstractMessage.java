package de.joschal.amp.core.entities.messages;

import de.joschal.amp.core.entities.network.addressing.Address;
import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class AbstractMessage implements Serializable {

    public AbstractMessage(Address sourceAddress, Address destinationAddress) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
    }

    private Address sourceAddress;

    private Address destinationAddress;

}


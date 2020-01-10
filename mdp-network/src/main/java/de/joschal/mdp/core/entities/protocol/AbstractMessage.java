package de.joschal.mdp.core.entities.protocol;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class AbstractMessage {

    public AbstractMessage(Address sourceAddress, Address destinationAddress, int hopLimit) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.hopLimit = hopLimit;
    }

    private Address sourceAddress;

    private Address destinationAddress;

    private int hopLimit = 0;

    private int hopCounter;


    public boolean hop() {
        hopCounter++;
        hopLimit--;
        return hopLimit == 0;
    }
}


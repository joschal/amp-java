package de.joschal.mdp.core.entities.protocol;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Datagram {

    public Datagram(Address sourceAddress, Address destinationAddress, int maxHops, String payload) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.maxHops = maxHops;
        this.payload = payload;
    }

    private Address sourceAddress;

    private Address destinationAddress;

    private int maxHops = 0;

    private int hopCounter;

    private String identification;

    private String payload;

    public boolean triggerHopCounter() {
        hopCounter++;
        maxHops--;
        return maxHops == 0;
    }

}

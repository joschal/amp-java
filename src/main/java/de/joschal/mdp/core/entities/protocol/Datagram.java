package de.joschal.mdp.core.entities.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Datagram {

    private Address sourceAddress;

    private Address destinationAddress;

    private int maxHops;

    private int hopCounter;

    private String identification;

    private String payload;

    public boolean decrementHopCounter(){
        maxHops--;
        return maxHops == 0;
    }

}

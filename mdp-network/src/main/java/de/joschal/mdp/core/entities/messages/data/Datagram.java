package de.joschal.mdp.core.entities.messages.data;

import de.joschal.mdp.core.entities.Address;
import lombok.Getter;

@Getter
public class Datagram extends AbstractDataMessage {

    public Datagram(Address sourceAddress, Address destinationAddress, int hopLimit) {
        super(sourceAddress, destinationAddress, hopLimit);
    }

    private String payload;

    public Datagram(Address sourceAddress, Address destinationAddress, int hopLimit, String payload) {
        super(sourceAddress, destinationAddress, hopLimit);
        this.payload = payload;
    }
}

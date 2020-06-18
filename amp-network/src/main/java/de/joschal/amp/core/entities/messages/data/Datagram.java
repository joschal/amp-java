package de.joschal.amp.core.entities.messages.data;

import de.joschal.amp.core.entities.network.addressing.Address;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Datagram extends AbstractDataMessage {

    private String payload;

    public Datagram(Address sourceAddress, Address destinationAddress, int hopLimit, String payload) {
        super(sourceAddress, destinationAddress, hopLimit);
        this.payload = payload;
    }

}

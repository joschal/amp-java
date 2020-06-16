package de.joschal.amp.core.entities.messages.addressing;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

public class PoolAccepted extends AbstractAddressingMessage {
    public PoolAccepted(PoolAdvertisement poolAdvertisement) {
        super(Address.undefined(), poolAdvertisement.getSourceAddress());
    }

    @Override
    public String toString() {
        return "[PoolAccepted " + this.getSourceAddress() + " -> " + this.getDestinationAddress() + "]";
    }
}

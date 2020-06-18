package de.joschal.amp.core.entities.messages.addressing;

import de.joschal.amp.core.entities.network.addressing.Address;

public class PoolAccepted extends AbstractAddressingMessage {
    public PoolAccepted(PoolAdvertisement poolAdvertisement) {
        super(Address.undefined(), poolAdvertisement.getSourceAddress());
    }

    @Override
    public String toString() {
        return "[PoolAccepted " + this.getSourceAddress() + " -> " + this.getDestinationAddress() + "]";
    }
}

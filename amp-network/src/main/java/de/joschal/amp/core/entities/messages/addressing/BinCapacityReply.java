package de.joschal.amp.core.entities.messages.addressing;

import de.joschal.amp.core.entities.network.addressing.Address;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BinCapacityReply extends AbstractAddressingMessage {

    // 64-bit
    private long binCapacity;

    public BinCapacityReply(Address sourceAddress, Address destinationAddress, long binCapacity) {
        super(sourceAddress, destinationAddress);
        this.binCapacity = binCapacity;
    }
}

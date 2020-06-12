package de.joschal.amp.core.entities.messages.addressing;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
public class PoolAssigned extends AbstractAddressingMessage {

    private List<AddressPool> addressPools = new LinkedList<>();

    public PoolAssigned(Address sourceAddress, Address destinationAddress, List<AddressPool> addressPools) {
        super(sourceAddress, destinationAddress);
        this.addressPools = addressPools;
    }

    public PoolAssigned(PoolAccepted accepted, List<AddressPool> addressPools) {
        super(accepted.getDestinationAddress(), Address.undefined());
        this.addressPools.addAll(addressPools);
    }
}

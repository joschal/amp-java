package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;

public class PoolAssigned extends AbstractAddressingMessage {

    private AddressPool addressPool;

    public PoolAssigned(Address sourceAddress, Address destinationAddress, AddressPool addressPool) {
        super(sourceAddress, destinationAddress, 1);
        this.addressPool = addressPool;
    }
}

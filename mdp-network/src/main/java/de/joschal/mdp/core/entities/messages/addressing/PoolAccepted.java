package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;

public class PoolAccepted extends AbstractAddressingMessage {

    private AddressPool addressPool;

    public PoolAccepted(Address sourceAddress, Address destinationAddress, AddressPool addressPool) {
        super(sourceAddress, destinationAddress, 1);
        this.addressPool = addressPool;
    }
}

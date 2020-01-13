package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.entities.Address;
import lombok.Getter;

@Getter
public class PoolAdvertisement extends AbstractAddressingMessage {

    private AddressPool addressPool;

    public PoolAdvertisement(Address sourceAddress, Address destinationAddress, AddressPool addressPool) {
        super(sourceAddress, destinationAddress, 1);
        this.addressPool = addressPool;
    }
}

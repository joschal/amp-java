package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PoolAdvertisement extends AbstractAddressingMessage implements Comparable<PoolAdvertisement> {

    private AddressPool addressPool;

    public PoolAdvertisement(Address sourceAddress, AddressPool addressPool) {
        super(sourceAddress, new Address(0), 1);
        this.addressPool = addressPool;
    }

    @Override
    public int compareTo(PoolAdvertisement poolAdvertisement) {
        return this.addressPool.compareTo(poolAdvertisement.addressPool);
    }
}

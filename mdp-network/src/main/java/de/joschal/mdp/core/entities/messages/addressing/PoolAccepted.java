package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PoolAccepted extends AbstractAddressingMessage {

    private AddressPool addressPool;

    public PoolAccepted(PoolAdvertisement poolAdvertisement) {
        super(new Address(0), poolAdvertisement.getSourceAddress(), 1);
        this.addressPool = poolAdvertisement.getAddressPool();
    }
}

package de.joschal.amp.core.entities.messages.addressing;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
public class PoolAccepted extends AbstractAddressingMessage {

    private List<AddressPool> addressPools = new LinkedList<>();

    public PoolAccepted(PoolAdvertisement poolAdvertisement) {
        super(new Address(0), poolAdvertisement.getSourceAddress(), 1);
        this.addressPools.addAll(poolAdvertisement.getAddressPools());
    }
}

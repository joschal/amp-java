package de.joschal.mdp.core.entities.messages.addressing;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
public class PoolAdvertisement extends AbstractAddressingMessage implements Comparable<PoolAdvertisement> {

    private List<AddressPool> addressPools = new LinkedList<>();

    private int totalSize = 0;

    public PoolAdvertisement(Address sourceAddress, AddressPool... addressPools) {
        super(sourceAddress, new Address(0), 1);
        this.addressPools.addAll(Arrays.asList(addressPools));
        this.addressPools.sort(Comparator.reverseOrder());

        for (AddressPool addressPool : addressPools) {
            this.totalSize += addressPool.getSize();
        }

    }

    @Override
    public int compareTo(PoolAdvertisement poolAdvertisement) {

        if (this.totalSize > poolAdvertisement.getTotalSize()) {
            return 1;
        } else if (this.totalSize < poolAdvertisement.getTotalSize()) {
            return -1;
        } else if (this.totalSize == poolAdvertisement.getTotalSize()) {
            return 0;
        }

        throw new RuntimeException("Something went wrong");

    }
}

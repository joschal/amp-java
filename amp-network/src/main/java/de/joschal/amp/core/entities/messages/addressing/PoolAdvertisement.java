package de.joschal.amp.core.entities.messages.addressing;

import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.core.entities.network.addressing.AddressPool;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
public class PoolAdvertisement extends AbstractAddressingMessage implements Comparable<PoolAdvertisement> {

    private List<AddressPool> addressPools = new LinkedList<>();


    public PoolAdvertisement(Address sourceAddress, Address destinationAddress, List<AddressPool> pools) {
        super(sourceAddress, destinationAddress);

        this.addressPools.addAll(pools);
        this.addressPools.sort(Comparator.reverseOrder());

        for (AddressPool addressPool : addressPools) {
            this.totalSize += addressPool.getSize();
        }

    }

    // total numer of advertised addresses
    // used for in compareTo method
    private int totalSize = 0;

    /**
     * Utility for ZAL/AQ algorithm
     */
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

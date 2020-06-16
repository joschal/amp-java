package de.joschal.amp.core.entities;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class AddressPool implements Comparable<AddressPool>, Serializable {

    public AddressPool(Address lowest, long size) {

        if (lowest.getValue() <= 0 || size <= 0) {
            throw new RuntimeException("invalid address range start:" + lowest.getValue() + " size:" + size);
        }

        this.lowest = lowest;
        this.size = size;
        this.highest = new Address(lowest.getValue() + size - 1);

    }

    private Address highest;
    private Address lowest;
    private long size;

    /**
     * Checks, which pool has the highest address
     *
     * @param addressPool Pool to compare to
     */
    @Override
    public int compareTo(AddressPool addressPool) {

        if (this.highest.getValue() > addressPool.highest.getValue()) {
            return 1;
        } else if (this.highest.getValue() < addressPool.highest.getValue()) {
            return -1;
        }

        throw new RuntimeException("Something went wrong");
    }


}

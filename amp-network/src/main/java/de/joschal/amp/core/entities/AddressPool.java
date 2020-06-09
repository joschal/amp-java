package de.joschal.amp.core.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AddressPool implements Comparable<AddressPool> {

    public AddressPool(int lowest, int highest) {
        this(new Address(lowest), new Address(highest));
    }

    public AddressPool(Address lowest, Address highest) {

        // Check if range is valid
        if (lowest.getValue() == 0) {
            throw new RuntimeException("Invalid Address range " + lowest + " -> " + highest + ". 0 is a reserved address");
        }
        if (highest.getValue() >= lowest.getValue()) {
            this.highest = highest;
            this.lowest = lowest;
            this.size = highest.getValue() - lowest.getValue() + 1;

        } else {
            throw new RuntimeException("Invalid Address range " + lowest + " -> " + highest);
        }
    }

    private Address highest;
    private Address lowest;
    private int size;

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

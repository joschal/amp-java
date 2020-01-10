package de.joschal.mdp.core.entities;

import de.joschal.mdp.core.entities.protocol.Address;
import lombok.Getter;

// This implementation effectively halves the address space, since only even ranges can be assigned.
@Getter
public class AddressRange implements Comparable<AddressRange> {

    public AddressRange(Address lowest, Address highest) {

        // Check if range is valid
        if (lowest.getValue() == 0){
            throw new RuntimeException("Invalid Address range " + lowest + " -> " + highest + ". 0 is a reserved address");
        }
        if (highest.getValue() > lowest.getValue()) {
            this.highest = highest;
            this.lowest = lowest;
            this.size = highest.getValue() - lowest.getValue() + 1;

            if (size % 2 != 0) {
                throw new RuntimeException("Invalid Address range " + lowest + " -> " + highest + ". Range size must be even!");
            }
        } else {
            throw new RuntimeException("Invalid Address range " + lowest + " -> " + highest);
        }
    }

    private Address highest;
    private Address lowest;
    private int size;

    @Override
    public int compareTo(AddressRange addressRange) {

        if (this.size > addressRange.size) {
            return 1;
        } else if (this.size < addressRange.size) {
            return -1;
        } else if (this.size == addressRange.size) {
            return 0;
        }

        throw new RuntimeException("Something went wrong");
    }
}

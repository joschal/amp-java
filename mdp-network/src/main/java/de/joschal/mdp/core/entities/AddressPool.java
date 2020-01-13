package de.joschal.mdp.core.entities;

import lombok.Getter;

// This implementation effectively halves the address space, since only even ranges can be assigned.
@Getter
public class AddressPool implements Comparable<AddressPool> {

    public AddressPool(Address lowest, Address highest) {

        // Check if range is valid
        if (lowest.getValue() == 0){
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

    @Override
    public int compareTo(AddressPool addressPool) {

        if (this.size > addressPool.size) {
            return 1;
        } else if (this.size < addressPool.size) {
            return -1;
        } else if (this.size == addressPool.size) {

            if (this.getHighest().getValue() > addressPool.getHighest().getValue()){
                return 1;
            } else {
                return -1;
            }
        }

        throw new RuntimeException("Something went wrong");
    }
}

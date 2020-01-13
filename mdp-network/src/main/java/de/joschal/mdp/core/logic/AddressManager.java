package de.joschal.mdp.core.logic;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.entities.network.NetworkInterface;

import java.util.*;

public class AddressManager {

    // Supports multiple disjointed address ranges
    public AddressManager(AddressPool... ranges) {
        unassignedRanges.addAll(Arrays.asList(ranges));
        unassignedRanges.sort(Comparator.reverseOrder());
    }

    // Range of available addresses
    List<AddressPool> unassignedRanges = new ArrayList<>();

    // Range of assigned addresses
    Map<NetworkInterface, AddressPool> assignedRanges = new HashMap<>();


    // Select the largest available address range, and set it's state to "assigned"
    public AddressPool assignAddressPool(NetworkInterface networkInterface) {

        AddressPool largestRange = unassignedRanges.remove(0);
        int halfwayPoint = largestRange.getSize() / 2;


        AddressPool unAssigned = new AddressPool(
                new Address(largestRange.getLowest().getValue()),
                new Address(halfwayPoint));

        AddressPool assigned = new AddressPool(
                new Address(halfwayPoint + 1),
                new Address(largestRange.getHighest().getValue()));

        unassignedRanges.add(unAssigned);
        unassignedRanges.sort(Comparator.reverseOrder());

        assignedRanges.put(networkInterface, assigned);
        return assigned;
    }

    public void revokeAddressPool(NetworkInterface networkInterface) {

        AddressPool removed = assignedRanges.remove(networkInterface);
        unassignedRanges.add(removed);

        defragment();
    }

    // defragment ranges. This could probably be optimized
    private void defragment() {

        unassignedRanges.sort(Comparator.reverseOrder());

        for (int i = 0; i < unassignedRanges.size() - 1; i++) {

            AddressPool current = unassignedRanges.get(i);
            AddressPool next = unassignedRanges.get(i + 1);

            if (current.getLowest().getValue() - 1 == next.getHighest().getValue()) {
                AddressPool combinedRange = new AddressPool(
                        new Address(next.getLowest().getValue()),
                        new Address(current.getHighest().getValue()));

                unassignedRanges.remove(current);
                unassignedRanges.remove(next);
                unassignedRanges.add(combinedRange);
                defragment();
                break;
            }
        }
    }
}

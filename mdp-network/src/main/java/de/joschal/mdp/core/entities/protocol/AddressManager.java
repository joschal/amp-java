package de.joschal.mdp.core.entities.protocol;

import de.joschal.mdp.core.entities.AddressRange;
import de.joschal.mdp.core.entities.network.NetworkInterface;

import java.util.*;

public class AddressManager {

    // Supports multiple disjointed address ranges
    public AddressManager(AddressRange... ranges) {
        unassignedRanges.addAll(Arrays.asList(ranges));
        unassignedRanges.sort(Comparator.reverseOrder());
    }

    // Range of available addresses
    List<AddressRange> unassignedRanges = new ArrayList<>();

    // Range of assigned addresses
    Map<NetworkInterface, AddressRange> assignedRanges = new HashMap<>();


    // Select the largest available address range, and set it's state to "assigned"
    AddressRange assignAddressRange(NetworkInterface networkInterface) {

        AddressRange largestRange = unassignedRanges.remove(0);
        int halfwayPoint = largestRange.getSize() / 2;


        AddressRange unAssigned = new AddressRange(
                new Address(largestRange.getLowest().getValue()),
                new Address(halfwayPoint));

        AddressRange assigned = new AddressRange(
                new Address(halfwayPoint + 1),
                new Address(largestRange.getHighest().getValue()));

        unassignedRanges.add(unAssigned);
        unassignedRanges.sort(Comparator.reverseOrder());

        assignedRanges.put(networkInterface, assigned);
        return assigned;
    }

    void unassignAddressRange(NetworkInterface networkInterface) {

        AddressRange removed = assignedRanges.remove(networkInterface);
        unassignedRanges.add(removed);

        defragment();
    }

    // defragment ranges. This could probably be optimized
    private void defragment() {

        unassignedRanges.sort(Comparator.reverseOrder());

        for (int i = 0; i < unassignedRanges.size() - 1; i++) {

            AddressRange current = unassignedRanges.get(i);
            AddressRange next = unassignedRanges.get(i + 1);

            if (current.getLowest().getValue() - 1 == next.getHighest().getValue()) {
                AddressRange combinedRange = new AddressRange(
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

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
    List<AddressRange> unassignedRanges = new LinkedList<>();

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

        defragment(removed);
    }

    // defragment ranges. This could probably be optimized
    private void defragment(AddressRange removed) {
        for (AddressRange range : unassignedRanges) {

            if (range.getHighest().getValue() + 1 == removed.getLowest().getValue()) {
                AddressRange newRange = new AddressRange(
                        new Address(range.getLowest().getValue()),
                        new Address(removed.getHighest().getValue()));

                unassignedRanges.remove(range);
                unassignedRanges.add(newRange);
                unassignedRanges.sort(Comparator.reverseOrder());
                break;

            } else if (removed.getHighest().getValue() + 1 == range.getLowest().getValue()) {
                AddressRange newRange = new AddressRange(
                        new Address(removed.getLowest().getValue()),
                        new Address(range.getHighest().getValue()));

                unassignedRanges.remove(range);
                unassignedRanges.add(newRange);
                unassignedRanges.sort(Comparator.reverseOrder());
                break;
            }
        }
    }
}

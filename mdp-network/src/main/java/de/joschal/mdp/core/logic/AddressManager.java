package de.joschal.mdp.core.logic;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.NetworkInterface;

import java.util.*;

public class AddressManager {

    // Supports multiple disjointed address ranges
    public AddressManager(AbstractNode node, AddressPool... ranges) {
        this.node = node;

        unassignedRanges.addAll(Arrays.asList(ranges));
        unassignedRanges.sort(Comparator.reverseOrder());
    }

    // Referece to node, used to assign address
    AbstractNode node;

    // Range of available addresses
    List<AddressPool> unassignedRanges = new ArrayList<>();

    // Range of assigned addresses
    Map<NetworkInterface, AddressPool> assignedRanges = new HashMap<>();

    public Address assignAddressToSelf() {

        // Node has an assigned address already
        if (node.getAddress().getValue() == 0) {


            // No addresses available
            if (unassignedRanges.isEmpty()) {
                return null;
            }

            AddressPool pool = unassignedRanges.remove(0);
            AddressPool newPool = new AddressPool(
                    new Address(pool.getLowest().getValue() + 1),
                    pool.getHighest());
            unassignedRanges.add(newPool);
            unassignedRanges.sort(Comparator.reverseOrder());

            return pool.getLowest();
        } else {
            throw new RuntimeException("Node already has an assigned address");
        }

    }

    // Select the largest available address range, and set it's state to "assigned"
    // Future work: combine address ranges to counter fragmentation
    public AddressPool assignAddressPool(NetworkInterface networkInterface) {

        AddressPool largestRange = unassignedRanges.remove(0);

        // Halfway point needs to be offset by the start addresses value
        int halfwayPoint = (largestRange.getSize() / 2) + (largestRange.getLowest().getValue() - 1);


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

    public void addAddressPool(AddressPool... addressPools) {
        unassignedRanges.addAll(Arrays.asList(addressPools));
        unassignedRanges.sort(Comparator.reverseOrder());
    }

    public boolean isPoolAvailable() {
        return !unassignedRanges.isEmpty();
    }

    public boolean isPoolAvailable(AddressPool pool) {
        return !unassignedRanges.contains(pool);
    }
}

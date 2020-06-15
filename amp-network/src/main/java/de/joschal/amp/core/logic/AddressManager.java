package de.joschal.amp.core.logic;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.NetworkInterface;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Getter
public class AddressManager {

    // Supports multiple disjointed address ranges
    public AddressManager(AbstractNode node, AddressPool... ranges) {
        this.node = node;

        this.unassignedRanges.addAll(Arrays.asList(ranges));
        this.unassignedRanges.sort(Comparator.reverseOrder());
    }

    // Referece to node, used to assign address
    private AbstractNode node;

    // Range of available addresses
    private List<AddressPool> unassignedRanges = new ArrayList<>();

    // Range of assigned addresses
    private Map<NetworkInterface, AddressPool> assignedRanges = new HashMap<>();

    /**
     * Reserve address pools
     * TODO this is not fully ZAL/AL compliant. May be optimized to prevent severe address leakage
     *
     * @param networkInterface interface, the pools are associated with
     * @return the list of reserved address pools
     */
    public List<AddressPool> reserveAddressPools(NetworkInterface networkInterface) {

        // Sort ranges, to make sure the pool with the highest address comes first
        this.unassignedRanges.sort(Comparator.reverseOrder());

        // Calculate how much address space is available for assignment
        long totalRemainingAddressSpace = unassignedRanges.stream().mapToLong(AddressPool::getSize).sum();
        // This much space should be assigned (value is rounded down to the nearest integer)
        final long desiredAssignableAddressSpace = totalRemainingAddressSpace / 2; // this is the value proposed by ZAL
        long addressSpaceStillNeeded = desiredAssignableAddressSpace;

        // List of pools to be assigned -> return value
        List<AddressPool> assignablePools = new LinkedList<>();


        for (AddressPool pool : unassignedRanges) {

            // Pool is no longer assignable
            unassignedRanges.remove(pool);

            // Pool size is not enough to fill the desired space
            if (pool.getSize() < addressSpaceStillNeeded) {

                assignablePools.add(pool);
                addressSpaceStillNeeded -= pool.getSize();

                assignedRanges.put(networkInterface, pool);


            } else if (pool.getSize() > addressSpaceStillNeeded) {

                List<AddressPool> splitPools = splitAddressPool(pool, addressSpaceStillNeeded);

                assignablePools.add(splitPools.get(0));
                assignedRanges.put(networkInterface, splitPools.get(0));

                unassignedRanges.add(splitPools.get(1));

                return assignablePools;

            } else if (pool.getSize() == addressSpaceStillNeeded) {
                assignablePools.add(pool);
                assignedRanges.put(networkInterface, pool);
                return assignablePools;
            }
        }
        return assignablePools;
    }

    public static List<AddressPool> splitAddressPool(AddressPool pool, long desiredSize) {

        List<AddressPool> pools = new ArrayList<>(2);

        AddressPool poolWithDesiredSize = new AddressPool(pool.getLowest(), desiredSize);

        AddressPool poolWithRemainingSize = new AddressPool(
                new Address(poolWithDesiredSize.getHighest().getValue() + 1),
                pool.getSize() - desiredSize);

        pools.add(0, poolWithDesiredSize);
        pools.add(1, poolWithRemainingSize);

        return pools;
    }

    /**
     * @return Address, which was selected and is now assigned to
     */
    public Address assignAddressToSelf() {

        // Node has an assigned address already
        if (node.getAddress().getValue() == 0) {


            // No addresses available
            if (unassignedRanges.isEmpty()) {
                return null;
            }

            AddressPool pool = unassignedRanges.remove(unassignedRanges.size() - 1);
            AddressPool newPool = new AddressPool(
                    new Address(pool.getLowest().getValue() + 1),
                    pool.getSize()-1);
            unassignedRanges.add(newPool);
            unassignedRanges.sort(Comparator.reverseOrder());

            this.node.setAddress(pool.getLowest());
            log.info("Assigned address {} to node {}", this.node.getAddress(), this.node.getId());
            return this.node.getAddress();
        } else {
            throw new RuntimeException("Node already has an assigned address");
        }

    }

    /**
     * Mark address pool as available
     *
     * @param networkInterface Interface associated with the pools which are to be revoked
     */
    public void revokeAddressPool(NetworkInterface networkInterface) {

        AddressPool removed = assignedRanges.remove(networkInterface);
        unassignedRanges.add(removed);

        defragment();
    }

    /**
     * Checks if adjacent address pools can be merged and does so if possible.
     * This could probably be optimized to reduce runtime complexity.
     */
    private void defragment() {

        unassignedRanges.sort(Comparator.reverseOrder());

        for (int i = 0; i < unassignedRanges.size() - 1; i++) {

            AddressPool current = unassignedRanges.get(i);
            AddressPool next = unassignedRanges.get(i + 1);

            if (current.getLowest().getValue() - 1 == next.getHighest().getValue()) {
                AddressPool combinedRange = new AddressPool(
                        new Address(next.getLowest().getValue()),
                        next.getSize() + current.getSize());

                unassignedRanges.remove(current);
                unassignedRanges.remove(next);
                unassignedRanges.add(combinedRange);
                defragment();
                break;
            }
        }
    }

    /**
     * Add Pools to list of available pools
     *
     * @param addressPools Available pools for future assignment
     */
    public void addAddressPools(List<AddressPool> addressPools) {
        unassignedRanges.addAll(addressPools);
        unassignedRanges.sort(Comparator.reverseOrder());
    }

    public void addAddressPool(AddressPool addressPool) {
        unassignedRanges.add(addressPool);
        unassignedRanges.sort(Comparator.reverseOrder());
    }


    /**
     * Checks if there is any pool available for assignment
     */
    public boolean isAPoolAvailable() {
        return !unassignedRanges.isEmpty();
    }

}

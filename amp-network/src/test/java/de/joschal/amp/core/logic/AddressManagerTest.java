package de.joschal.amp.core.logic;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.addressing.Address;
import de.joschal.amp.core.entities.network.addressing.AddressPool;
import de.joschal.amp.core.logic.nodes.Node;
import de.joschal.amp.io.NetworkInterface;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the ZAL/AL algorithm
 */
public class AddressManagerTest {

    @Test
    public void addAndRemoveEven() {

        // Arrange
        AbstractNode node = new Node("node");
        AddressPool range = new AddressPool(new Address(1), 4);
        NetworkInterface networkInterface = new NetworkInterface("interface", node);

        AddressManager addressManager = new AddressManager(null, range);

        assertEquals(4, addressManager.getUnassignedRanges().get(0).getSize());

        // Act
        List<AddressPool> assigned = addressManager.reserveAddressPools(networkInterface);

        assertEquals(range.getSize() / 2, assigned.stream().mapToLong(AddressPool::getSize).sum());

        addressManager.revokeAddressPool(networkInterface);

        // Assert
        assertEquals(0, addressManager.getAssignedRanges().size());
        assertEquals(1, addressManager.getUnassignedRanges().size());

        assertEquals(4, addressManager.getUnassignedRanges().get(0).getSize());

    }

    @Test
    public void addAndRemoveOdd() {

        // Arrange
        AbstractNode node = new Node("node");
        AddressPool range = new AddressPool(new Address(1), 5);
        NetworkInterface networkInterface = new NetworkInterface("interface", node);

        AddressManager addressManager = new AddressManager(null, range);

        assertEquals(5, addressManager.getUnassignedRanges().get(0).getSize());

        // Act
        List<AddressPool> assigned = addressManager.reserveAddressPools(networkInterface);

        assertEquals(range.getSize() / 2, assigned.stream().mapToLong(AddressPool::getSize).sum());

        addressManager.revokeAddressPool(networkInterface);

        // Assert
        assertEquals(0, addressManager.getAssignedRanges().size());
        assertEquals(1, addressManager.getUnassignedRanges().size());

        assertEquals(5, addressManager.getUnassignedRanges().get(0).getSize());

    }


    @Test
    public void multiAssignementUnassignement() {

        AbstractNode node = new Node("node");
        AddressPool originAddressPool = new AddressPool(new Address(1), 4);
        NetworkInterface networkInterface1 = new NetworkInterface("1", node);
        NetworkInterface networkInterface2 = new NetworkInterface("2", node);

        AddressManager addressManager = new AddressManager(null, originAddressPool);

        List<AddressPool> addressPool1 = addressManager.reserveAddressPools(networkInterface1);
        List<AddressPool> addressPool2 = addressManager.reserveAddressPools(networkInterface2);

        assertEquals(2, addressPool1.stream().mapToLong(AddressPool::getSize).sum());
        assertEquals(1, addressPool2.stream().mapToLong(AddressPool::getSize).sum());

        addressManager.revokeAddressPool(networkInterface1);
        assertEquals(2, addressManager.getUnassignedRanges().size());
        assertEquals(1, addressManager.getUnassignedRanges().get(0).getSize());
        assertEquals(2, addressManager.getUnassignedRanges().get(1).getSize());

        addressManager.revokeAddressPool(networkInterface2);
        assertEquals(1, addressManager.getUnassignedRanges().size());
        assertEquals(4, addressManager.getUnassignedRanges().get(0).getSize());
    }

    @Test
    public void poolSplitTestOdd() {

        AddressPool addressPool = new AddressPool(new Address(4), 7);

        List<AddressPool> addressPoolsEven = AddressManager.splitAddressPool(addressPool, 4);

        assertEquals(4, addressPoolsEven.get(0).getSize());
        assertEquals(3, addressPoolsEven.get(1).getSize());

        List<AddressPool> addressPoolsOdd = AddressManager.splitAddressPool(addressPool, 3);

        assertEquals(3, addressPoolsOdd.get(0).getSize());
        assertEquals(4, addressPoolsOdd.get(1).getSize());
    }

    @Test
    public void poolSplitTestEven() {

        AddressPool addressPool = new AddressPool(new Address(3), 8);

        List<AddressPool> addressPoolsEven = AddressManager.splitAddressPool(addressPool, 4);

        assertEquals(4, addressPoolsEven.get(0).getSize());
        assertEquals(4, addressPoolsEven.get(1).getSize());

        List<AddressPool> addressPoolsOdd = AddressManager.splitAddressPool(addressPool, 3);

        assertEquals(3, addressPoolsOdd.get(0).getSize());
        assertEquals(5, addressPoolsOdd.get(1).getSize());
    }
}
package de.joschal.mdp.core.logic;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AddressManagerTest {

    @Test
    public void addAndRemoveEven() {

        // Arrange
        AddressPool range = new AddressPool(new Address(1), new Address(4));
        NetworkInterface networkInterface = new NetworkInterface("interface");

        AddressManager addressManager = new AddressManager(null, range);

        assertEquals(4, addressManager.getUnassignedRanges().get(0).getSize());

        // Act
        List<AddressPool> assigned = addressManager.getAssignableAddressPools(networkInterface);

        assertEquals(range.getSize() / 2, assigned.stream().mapToInt(AddressPool::getSize).sum());

        addressManager.revokeAddressPool(networkInterface);

        // Assert
        assertEquals(0, addressManager.getAssignedRanges().size());
        assertEquals(1, addressManager.getUnassignedRanges().size());

        assertEquals(4, addressManager.getUnassignedRanges().get(0).getSize());

    }

    @Test
    public void addAndRemoveOdd() {

        // Arrange
        AddressPool range = new AddressPool(new Address(1), new Address(5));
        NetworkInterface networkInterface = new NetworkInterface("interface");

        AddressManager addressManager = new AddressManager(null, range);

        assertEquals(5, addressManager.getUnassignedRanges().get(0).getSize());

        // Act
        List<AddressPool> assigned = addressManager.getAssignableAddressPools(networkInterface);

        assertEquals(range.getSize() / 2, assigned.stream().mapToInt(AddressPool::getSize).sum());

        addressManager.revokeAddressPool(networkInterface);

        // Assert
        assertEquals(0, addressManager.getAssignedRanges().size());
        assertEquals(1, addressManager.getUnassignedRanges().size());

        assertEquals(5, addressManager.getUnassignedRanges().get(0).getSize());

    }


    @Test
    public void multiAssignementUnassignement() {

        AddressPool originAddressPool = new AddressPool(new Address(1), new Address(4));
        NetworkInterface networkInterface1 = new NetworkInterface("1");
        NetworkInterface networkInterface2 = new NetworkInterface("2");

        AddressManager addressManager = new AddressManager(null, originAddressPool);

        List<AddressPool> addressPool1 = addressManager.getAssignableAddressPools(networkInterface1);
        List<AddressPool> addressPool2 = addressManager.getAssignableAddressPools(networkInterface2);

        assertEquals(2, addressPool1.stream().mapToInt(AddressPool::getSize).sum());
        assertEquals(1, addressPool2.stream().mapToInt(AddressPool::getSize).sum());

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

        AddressPool addressPool = new AddressPool(
                new Address(4),
                new Address(10));

        List<AddressPool> addressPoolsEven = AddressManager.splitAddressPool(addressPool, 4);

        assertEquals(4, addressPoolsEven.get(0).getSize());
        assertEquals(3, addressPoolsEven.get(1).getSize());

        List<AddressPool> addressPoolsOdd = AddressManager.splitAddressPool(addressPool, 3);

        assertEquals(3, addressPoolsOdd.get(0).getSize());
        assertEquals(4, addressPoolsOdd.get(1).getSize());
    }

    @Test
    public void poolSplitTestEven() {

        AddressPool addressPool = new AddressPool(
                new Address(3),
                new Address(10));

        List<AddressPool> addressPoolsEven = AddressManager.splitAddressPool(addressPool, 4);

        assertEquals(4, addressPoolsEven.get(0).getSize());
        assertEquals(4, addressPoolsEven.get(1).getSize());

        List<AddressPool> addressPoolsOdd = AddressManager.splitAddressPool(addressPool, 3);

        assertEquals(3, addressPoolsOdd.get(0).getSize());
        assertEquals(5, addressPoolsOdd.get(1).getSize());
    }
}
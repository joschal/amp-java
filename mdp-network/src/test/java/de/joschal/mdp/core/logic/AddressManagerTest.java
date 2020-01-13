package de.joschal.mdp.core.logic;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.AddressPool;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddressManagerTest {

    @Test
    public void addAndRemoveEven() {

        // Arrange
        AddressPool range = new AddressPool(new Address(1), new Address(4));
        NetworkInterface networkInterface = new NetworkInterface("interface");

        AddressManager addressManager = new AddressManager(null, range);

        assertEquals(4, addressManager.unassignedRanges.get(0).getSize());

        // Act
        AddressPool assigned = addressManager.assignAddressPool(networkInterface);

        assertEquals(range.getSize() / 2, assigned.getSize());

        addressManager.revokeAddressPool(networkInterface);

        // Assert
        assertEquals(0, addressManager.assignedRanges.size());
        assertEquals(1, addressManager.unassignedRanges.size());

        assertEquals(4, addressManager.unassignedRanges.get(0).getSize());

    }

    @Test
    public void addAndRemoveOdd() {

        // Arrange
        AddressPool range = new AddressPool(new Address(1), new Address(5));
        NetworkInterface networkInterface = new NetworkInterface("interface");

        AddressManager addressManager = new AddressManager(null, range);

        assertEquals(5, addressManager.unassignedRanges.get(0).getSize());

        // Act
        AddressPool assigned = addressManager.assignAddressPool(networkInterface);

        assertEquals((range.getSize() / 2) +1, assigned.getSize());

        addressManager.revokeAddressPool(networkInterface);

        // Assert
        assertEquals(0, addressManager.assignedRanges.size());
        assertEquals(1, addressManager.unassignedRanges.size());

        assertEquals(5, addressManager.unassignedRanges.get(0).getSize());

    }


    @Test
    public void multiAssignementUnassignement() {

        AddressPool originAddressPool = new AddressPool(new Address(1), new Address(4));
        NetworkInterface networkInterface1 = new NetworkInterface("1");
        NetworkInterface networkInterface2 = new NetworkInterface("2");

        AddressManager addressManager = new AddressManager(null, originAddressPool);

        AddressPool addressPool1 = addressManager.assignAddressPool(networkInterface1);
        AddressPool addressPool2 = addressManager.assignAddressPool(networkInterface2);

        assertEquals(2, addressPool1.getSize());
        assertEquals(1, addressPool2.getSize());

        addressManager.revokeAddressPool(networkInterface1);
        assertEquals(2, addressManager.unassignedRanges.size());
        assertEquals(2, addressManager.unassignedRanges.get(0).getSize());
        assertEquals(1, addressManager.unassignedRanges.get(1).getSize());

        addressManager.revokeAddressPool(networkInterface2);
        assertEquals(1, addressManager.unassignedRanges.size());
        assertEquals(4, addressManager.unassignedRanges.get(0).getSize());
    }
}
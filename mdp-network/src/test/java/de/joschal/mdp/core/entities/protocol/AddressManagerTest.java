package de.joschal.mdp.core.entities.protocol;

import de.joschal.mdp.core.entities.AddressRange;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddressManagerTest {

    @Test
    public void addAndRemoveEven() {

        // Arrange
        AddressRange range = new AddressRange(new Address(1), new Address(4));
        NetworkInterface networkInterface = new NetworkInterface("interface");

        AddressManager addressManager = new AddressManager(range);

        assertEquals(4, addressManager.unassignedRanges.get(0).getSize());

        // Act
        AddressRange assigned = addressManager.assignAddressRange(networkInterface);

        assertEquals(range.getSize() / 2, assigned.getSize());

        addressManager.unassignAddressRange(networkInterface);

        // Assert
        assertEquals(0, addressManager.assignedRanges.size());
        assertEquals(1, addressManager.unassignedRanges.size());

        assertEquals(4, addressManager.unassignedRanges.get(0).getSize());

    }

    @Test
    public void addAndRemoveOdd() {

        // Arrange
        AddressRange range = new AddressRange(new Address(1), new Address(5));
        NetworkInterface networkInterface = new NetworkInterface("interface");

        AddressManager addressManager = new AddressManager(range);

        assertEquals(5, addressManager.unassignedRanges.get(0).getSize());

        // Act
        AddressRange assigned = addressManager.assignAddressRange(networkInterface);

        assertEquals((range.getSize() / 2) +1, assigned.getSize());

        addressManager.unassignAddressRange(networkInterface);

        // Assert
        assertEquals(0, addressManager.assignedRanges.size());
        assertEquals(1, addressManager.unassignedRanges.size());

        assertEquals(5, addressManager.unassignedRanges.get(0).getSize());

    }


    @Test
    public void multiAssignementUnassignement() {

        AddressRange originAddressRange = new AddressRange(new Address(1), new Address(4));
        NetworkInterface networkInterface1 = new NetworkInterface("1");
        NetworkInterface networkInterface2 = new NetworkInterface("2");

        AddressManager addressManager = new AddressManager(originAddressRange);

        AddressRange addressRange1 = addressManager.assignAddressRange(networkInterface1);
        AddressRange addressRange2 = addressManager.assignAddressRange(networkInterface2);

        assertEquals(2, addressRange1.getSize());
        assertEquals(1, addressRange2.getSize());

        addressManager.unassignAddressRange(networkInterface1);
        assertEquals(2, addressManager.unassignedRanges.size());
        assertEquals(2, addressManager.unassignedRanges.get(0).getSize());
        assertEquals(1, addressManager.unassignedRanges.get(1).getSize());

        addressManager.unassignAddressRange(networkInterface2);
        assertEquals(1, addressManager.unassignedRanges.size());
        assertEquals(4, addressManager.unassignedRanges.get(0).getSize());
    }
}
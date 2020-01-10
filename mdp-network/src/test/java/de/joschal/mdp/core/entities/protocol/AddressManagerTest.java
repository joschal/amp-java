package de.joschal.mdp.core.entities.protocol;

import de.joschal.mdp.core.entities.AddressRange;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddressManagerTest {

    @Test(expected = RuntimeException.class)
    public void uneven() {

        // Arrange
        AddressRange range = new AddressRange(new Address(1), new Address(1025));

        //Act
        AddressManager addressManager = new AddressManager(range);

    }

    @Test
    public void even() {

        // Arrange
        AddressRange range = new AddressRange(new Address(1), new Address(1024));
        NetworkInterface networkInterface = new NetworkInterface("interface");

        AddressManager addressManager = new AddressManager(range);

        AddressRange assigned = addressManager.assignAddressRange(networkInterface);

        assertEquals(range.getSize() / 2 , assigned.getSize());

    }

    @Test
    public void addAndRemove() {

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
}
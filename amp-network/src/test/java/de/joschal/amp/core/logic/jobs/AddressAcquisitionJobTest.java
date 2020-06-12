package de.joschal.amp.core.logic.jobs;

import de.joschal.amp.core.entities.AbstractMessage;
import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.AddressPool;
import de.joschal.amp.core.entities.messages.addressing.PoolAccepted;
import de.joschal.amp.core.entities.messages.addressing.PoolAdvertisement;
import de.joschal.amp.core.entities.messages.addressing.PoolAssigned;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.NetworkInterface;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.core.logic.sender.MessageSender;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AddressAcquisitionJobTest {

    @Test
    void assignPoolAtFirstTry() {

        /* --------------- ARRANGE ------------------ */

        // create environment
        AbstractNode node = new SimpleNode("node");
        List<NetworkInterface> networkInterfaces = new LinkedList<>();
        networkInterfaces.add(new NetworkInterface("1", node));
        networkInterfaces.add(new NetworkInterface("2", node));
        networkInterfaces.add(new NetworkInterface("3", node));

        // Mocks
        MessageSender messageSenderMock = mock(MessageSender.class);
        node.setMessageSender(messageSenderMock);

        // 20 addresses
        AddressPool ap11 = new AddressPool(new Address(100), 10);
        AddressPool ap12 = new AddressPool(new Address(200), 10);
        List<AddressPool> apl1 = new ArrayList<>();
        apl1.add(ap11);
        apl1.add(ap12);
        PoolAdvertisement advertisement1 = new PoolAdvertisement(new Address(1), Address.undefined(), apl1);

        // 30 addresses
        AddressPool ap21 = new AddressPool(new Address(300), 10);
        AddressPool ap22 = new AddressPool(new Address(400), 10);
        AddressPool ap23 = new AddressPool(new Address(500), 10);
        List<AddressPool> apl2 = new ArrayList<>();
        apl2.add(ap21);
        apl2.add(ap22);
        apl2.add(ap23);
        PoolAdvertisement advertisement2 = new PoolAdvertisement(new Address(1), Address.undefined(), apl2);

        // 20 addresses
        AddressPool ap31 = new AddressPool(new Address(600), 10);
        AddressPool ap32 = new AddressPool(new Address(700), 10);
        List<AddressPool> apl3 = new ArrayList<>();
        apl3.add(ap31);
        apl3.add(ap32);
        PoolAdvertisement advertisement3 = new PoolAdvertisement(new Address(1), Address.undefined(), apl1);

        // Address asignment
        PoolAssigned poolAssigned = new PoolAssigned(advertisement2.getSourceAddress(), Address.undefined(), advertisement2.getAddressPools());

        //create Job
        int maxTicks = 5;
        AddressAcquisitionJob addressAcquisitionJob = new AddressAcquisitionJob(node.getJobManager(), networkInterfaces, maxTicks, node.getMessageSender(), node.getAddressManager());

        /* --------------- ACT ------------------ */
        addressAcquisitionJob.init();

        addressAcquisitionJob.tick();

        addressAcquisitionJob.receiveMessage(advertisement1);
        addressAcquisitionJob.receiveMessage(advertisement2);

        addressAcquisitionJob.tick();

        addressAcquisitionJob.receiveMessage(advertisement3);

        addressAcquisitionJob.tick();

        /* --------------- ASSERT ------------------ */
        ArgumentCaptor<PoolAccepted> argument = ArgumentCaptor.forClass(PoolAccepted.class);
        verify(messageSenderMock, times(1)).sendMessageViaKnownRoute(argument.capture());
        assertEquals(argument.getValue().getSourceAddress(), Address.undefined());
        assertEquals(argument.getValue().getDestinationAddress(), poolAssigned.getSourceAddress());

        /* --------------- ACT ------------------ */

        addressAcquisitionJob.receiveMessage(poolAssigned);
        addressAcquisitionJob.tick();

        /* --------------- ASSERT ------------------ */
        assertEquals(300, node.getAddress().getValue());

    }

    @Test
    void assignPoolAfterRetry() {

        /* --------------- ARRANGE ------------------ */

        // create environment
        AbstractNode node = new SimpleNode("node");
        List<NetworkInterface> networkInterfaces = new LinkedList<>();
        networkInterfaces.add(new NetworkInterface("1", node));
        networkInterfaces.add(new NetworkInterface("2", node));
        networkInterfaces.add(new NetworkInterface("3", node));

        // Mocks
        MessageSender messageSenderMock = mock(MessageSender.class);
        node.setMessageSender(messageSenderMock);

        // 20 addresses
        AddressPool ap11 = new AddressPool(new Address(100), 10);
        AddressPool ap12 = new AddressPool(new Address(200), 10);
        List<AddressPool> apl1 = new ArrayList<>();
        apl1.add(ap11);
        apl1.add(ap12);
        PoolAdvertisement advertisement1 = new PoolAdvertisement(new Address(1), Address.undefined(), apl1);

        // 30 addresses
        AddressPool ap21 = new AddressPool(new Address(300), 10);
        AddressPool ap22 = new AddressPool(new Address(400), 10);
        AddressPool ap23 = new AddressPool(new Address(500), 10);
        List<AddressPool> apl2 = new ArrayList<>();
        apl2.add(ap21);
        apl2.add(ap22);
        apl2.add(ap23);
        PoolAdvertisement advertisement2 = new PoolAdvertisement(new Address(1), Address.undefined(), apl2);

        // 20 addresses
        AddressPool ap31 = new AddressPool(new Address(600), 10);
        AddressPool ap32 = new AddressPool(new Address(700), 10);
        List<AddressPool> apl3 = new ArrayList<>();
        apl3.add(ap31);
        apl3.add(ap32);
        PoolAdvertisement advertisement3 = new PoolAdvertisement(new Address(1), Address.undefined(), apl1);

        // Address asignment
        PoolAssigned poolAssigned = new PoolAssigned(advertisement2.getSourceAddress(), Address.undefined(), advertisement2.getAddressPools());

        //create Job
        int maxTicks = 3;
        AddressAcquisitionJob addressAcquisitionJob = new AddressAcquisitionJob(node.getJobManager(), networkInterfaces, maxTicks, node.getMessageSender(), node.getAddressManager());

        /* --------------- ACT ------------------ */
        addressAcquisitionJob.init();

        addressAcquisitionJob.tick();

        addressAcquisitionJob.receiveMessage(advertisement1);
        addressAcquisitionJob.receiveMessage(advertisement3);

        addressAcquisitionJob.tick();
        addressAcquisitionJob.tick();
        addressAcquisitionJob.tick();

        /* --------------- ASSERT ------------------ */
        ArgumentCaptor<PoolAccepted> argument = ArgumentCaptor.forClass(PoolAccepted.class);
        verify(messageSenderMock, times(1)).sendMessageViaKnownRoute(argument.capture());
        assertEquals(argument.getValue().getSourceAddress(), Address.undefined());
        assertEquals(argument.getValue().getDestinationAddress(), poolAssigned.getSourceAddress());

        /* --------------- ACT ------------------ */

        addressAcquisitionJob.receiveMessage(poolAssigned);
        addressAcquisitionJob.tick();

        /* --------------- ASSERT ------------------ */
        assertEquals(300, node.getAddress().getValue());

    }
}
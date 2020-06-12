package de.joschal.amp.core.logic.sender;

import de.joschal.amp.core.entities.Address;
import de.joschal.amp.core.entities.messages.data.Datagram;
import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.AbstractRouter;
import de.joschal.amp.core.entities.network.DataLink;
import de.joschal.amp.core.entities.network.NetworkInterface;
import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.core.logic.router.Router;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForwardingTest {

    /**
     * Verifies flooding via all interfaces
     */
    @Test
    void floodViaAllInterfaces() {

        AbstractNode node = new SimpleNode("1");
        AbstractRouter router = new Router(node.getAddress(), node.getNetworkInterfaces());
        MessageForwarder forwarder = new MessageForwarder(router, new MessageSender(new Address(42), router, node.getNetworkInterfaces()));

        NetworkInterface networkInterface1 = new NetworkInterface("interface1", node);
        NetworkInterface networkInterface2 = new NetworkInterface("interface2", node);
        NetworkInterface networkInterface3 = new NetworkInterface("interface3", node);
        NetworkInterface networkInterface4 = new NetworkInterface("interface4", node);
        NetworkInterface dummyNetworkInterface = new NetworkInterface("dummyInterface", node);

        DataLink dataLink1 = new DataLink("datalink1", networkInterface1, dummyNetworkInterface);
        DataLink dataLink2 = new DataLink("datalink2", networkInterface2, dummyNetworkInterface);
        DataLink dataLink3 = new DataLink("datalink3", networkInterface3, dummyNetworkInterface);
        DataLink dataLink4 = new DataLink("datalink4", networkInterface4, dummyNetworkInterface);

        networkInterface1.setDataLink(dataLink1);
        networkInterface2.setDataLink(dataLink2);
        networkInterface3.setDataLink(dataLink3);
        networkInterface4.setDataLink(dataLink4);

        node.addNetworkInterface(networkInterface1);
        node.addNetworkInterface(networkInterface2);
        node.addNetworkInterface(networkInterface3);
        node.addNetworkInterface(networkInterface4);


        Datagram datagram = new Datagram(new Address(1), new Address(2), 42, UUID.randomUUID().toString());
        forwarder.forwardMessage(datagram, null);

        assertEquals(datagram.getPayload(), ((Datagram) dataLink1.getASendQueue().poll()).getPayload());
        assertEquals(datagram.getPayload(), ((Datagram) dataLink2.getASendQueue().poll()).getPayload());
        assertEquals(datagram.getPayload(), ((Datagram) dataLink3.getASendQueue().poll()).getPayload());
        assertEquals(datagram.getPayload(), ((Datagram) dataLink4.getASendQueue().poll()).getPayload());

    }

    /**
     * Verifies that message is flooded to all interfaces except the source
     */
    @Test
    void forwardingOmitSource() {

        AbstractNode node = new SimpleNode("1");
        AbstractRouter router = new Router(node.getAddress(), node.getNetworkInterfaces());
        MessageForwarder forwarder = new MessageForwarder(router, new MessageSender(new Address(42), router, node.getNetworkInterfaces()));
        node.setMessageForwarder(forwarder);

        NetworkInterface networkInterface1 = new NetworkInterface("interface1", node);
        NetworkInterface networkInterface2 = new NetworkInterface("interface2", node);
        NetworkInterface networkInterface3 = new NetworkInterface("interface3", node);
        NetworkInterface networkInterface4 = new NetworkInterface("interface4", node);
        NetworkInterface dummyNetworkInterface = new NetworkInterface("dummyInterface", node);

        DataLink dataLink2 = new DataLink("datalink2", networkInterface2, dummyNetworkInterface);
        DataLink dataLink3 = new DataLink("datalink3", networkInterface3, dummyNetworkInterface);
        DataLink dataLink4 = new DataLink("datalink4", networkInterface4, dummyNetworkInterface);

        networkInterface2.setDataLink(dataLink2);
        networkInterface3.setDataLink(dataLink3);
        networkInterface4.setDataLink(dataLink4);
        node.addNetworkInterface(networkInterface1);
        node.addNetworkInterface(networkInterface2);
        node.addNetworkInterface(networkInterface3);
        node.addNetworkInterface(networkInterface4);

        Datagram datagram = new Datagram(new Address(1), new Address(2), 42, UUID.randomUUID().toString());
        networkInterface1.receiveMessage(datagram);

        assertEquals(datagram.getPayload(), ((Datagram) dataLink2.getASendQueue().poll()).getPayload());
        assertEquals(datagram.getPayload(), ((Datagram) dataLink3.getASendQueue().poll()).getPayload());
        assertEquals(datagram.getPayload(), ((Datagram) dataLink4.getASendQueue().poll()).getPayload());

    }
}

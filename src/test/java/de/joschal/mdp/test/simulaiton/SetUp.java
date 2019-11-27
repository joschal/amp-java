package de.joschal.mdp.test.simulaiton;

import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.Node;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static de.joschal.mdp.simulation.Setup.*;
import static org.junit.Assert.assertEquals;

public class SetUp {

    @Test
    public void setUp() {
        Address address1 = new Address("1");
        Address address2 = new Address("2");
        Address address3 = new Address("3");
        Address address4 = new Address("4");

        List<Node> nodes = setUpNodes(address1, address2, address3, address4);

        assertEquals(4, nodes.size());
    }

    @Test
    public void setUpWithInterface() {

        Node node1 = setUpNode(
                new Address("1"),
                new NetworkInterface("1a"),
                new NetworkInterface("1b"));

        Node node2 = setUpNode(
                new Address("2"),
                new NetworkInterface("2a"),
                new NetworkInterface("2b"));

        linkTwoNodes(node1, node2);

        node1.action("foo", node2.getAddress());
    }

    @Test
    public void linkInterfacesTest() {

        Node node1 = setUpNode(
                new Address("1"),
                new NetworkInterface("1a"),
                new NetworkInterface("1b"));

        Node node2 = setUpNode(
                new Address("2"),
                new NetworkInterface("2a"),
                new NetworkInterface("2b"));

        Node node3 = setUpNode(
                new Address("3"),
                new NetworkInterface("3a"),
                new NetworkInterface("3b"));

        // circular link
        LinkedList<DataLink> links = new LinkedList<>();
        links.add(linkInterfaces(node1.getInterfaces().get(0), node2.getInterfaces().get(0)));
        links.add(linkInterfaces(node2.getInterfaces().get(1), node3.getInterfaces().get(0)));
        links.add(linkInterfaces(node3.getInterfaces().get(1), node1.getInterfaces().get(1)));

        System.out.println(links);
    }
}

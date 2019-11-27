package de.joschal.mdp.simulation;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.Node;
import de.joschal.mdp.core.logic.simple.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Setup {

    public static List<Node> setUpNodes(Address... addresses) {

        ArrayList<Node> nodes = new ArrayList<>(addresses.length);

        for (Address address : addresses) {
            Node node = new Node(address, new Router());
            NetworkInterface networkInterface = new NetworkInterface(UUID.randomUUID().toString());
            node.addNetworkInterface(networkInterface);
            nodes.add(node);
        }

        return nodes;
    }

    public static Node setUpNode(Address address, NetworkInterface... interfaces) {

        Node node = new Node(address, new Router());
        for (NetworkInterface networkInterface : interfaces) {
            node.addNetworkInterface(networkInterface);
        }
        return node;
    }

    public static Node setUpNode(Address address, AbstractRouter router, NetworkInterface... interfaces) {

        Node node = new Node(address, router);
        for (NetworkInterface networkInterface : interfaces) {
            node.addNetworkInterface(networkInterface);
        }
        return node;
    }

    public static DataLink linkTwoNodes(AbstractNode node1, AbstractNode node2) {

        return new DataLink(
                UUID.randomUUID().toString(),
                node1.getInterfaces().get(0),
                node2.getInterfaces().get(0));
    }

    public static DataLink linkInterfaces(NetworkInterface networkInterface1, NetworkInterface networkInterface2) {

        return new DataLink(
                UUID.randomUUID().toString(),
                networkInterface1,
                networkInterface2);
    }
}

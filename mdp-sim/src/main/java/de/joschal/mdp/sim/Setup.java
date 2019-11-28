package de.joschal.mdp.sim;

import de.joschal.mdp.core.entities.network.AbstractNode;
import de.joschal.mdp.core.entities.network.AbstractRouter;
import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.network.NetworkInterface;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.Node;
import de.joschal.mdp.core.logic.simple.Router;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static DataLink linkInterfaces(NetworkInterface networkInterface1, NetworkInterface networkInterface2) {

        return new DataLink(
                UUID.randomUUID().toString(),
                networkInterface1,
                networkInterface2);
    }

    public static DataLink linkNodes(Address A, Address B, HashMap<Address, Node> nodes) {

        AbstractNode node1 = nodes.get(A);
        AbstractNode node2 = nodes.get(B);

        NetworkInterface networkInterface1 = new NetworkInterface("to " + node2.getAddress());
        node1.addNetworkInterface(networkInterface1);

        NetworkInterface networkInterface2 = new NetworkInterface("to " + node1.getAddress());
        node2.addNetworkInterface(networkInterface2);

        return linkInterfaces(networkInterface1, networkInterface2);
    }
}

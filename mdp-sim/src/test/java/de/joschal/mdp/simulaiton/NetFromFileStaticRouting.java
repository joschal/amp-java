package de.joschal.mdp.simulaiton;

import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.entities.network.Route;
import de.joschal.mdp.core.logic.nodes.SimpleNode;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.outbound.GraphReader;
import org.junit.Test;

public class NetFromFileStaticRouting {

    @Test
    public void netFromFileStaticRouting() {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        SimpleNode node1 = (SimpleNode) graph.getNodebyId("1");
        SimpleNode node2 = (SimpleNode) graph.getNodebyId("2");
        SimpleNode node3 = (SimpleNode) graph.getNodebyId("3");

        node1.setAddress(new Address(1));
        node2.setAddress(new Address(2));
        node3.setAddress(new Address(3));

        node1.addRoute(new Route(node1.getInterfaces().get(0), node2.getAddress(), 1));
        node1.addRoute(new Route(node1.getInterfaces().get(0), node3.getAddress(), 1));

        node2.addRoute(new Route(node2.getInterfaces().get(1), node3.getAddress(), 1));
        node2.addRoute(new Route(node2.getInterfaces().get(0), node1.getAddress(), 1));

        node3.addRoute(new Route(node2.getInterfaces().get(1), node1.getAddress(), 1));
        node3.addRoute(new Route(node2.getInterfaces().get(1), node1.getAddress(), 1));

        node1.action("1->3", node3.getAddress());
        System.out.println("-------");
        node3.action("3->1", node1.getAddress());
    }
}

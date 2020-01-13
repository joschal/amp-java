package de.joschal.mdp.simulaiton;

import de.joschal.mdp.core.entities.network.Route;
import de.joschal.mdp.core.entities.Address;
import de.joschal.mdp.core.logic.simple.SimpleNode;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.outbound.GraphReader;
import org.junit.Test;

public class NetFromFileStaticRouting {

    @Test
    public void netFromFileStaticRouting() {

        Graph graph = new GraphReader().readGraph(getClass().getResource("/testGraph.dot").getFile());

        SimpleNode loggingNode1 = (SimpleNode) graph.getNodebyId("1");
        SimpleNode loggingNode2 = (SimpleNode) graph.getNodebyId("2");
        SimpleNode loggingNode3 = (SimpleNode) graph.getNodebyId("3");

        loggingNode1.setAddress(new Address(1));
        loggingNode2.setAddress(new Address(2));
        loggingNode3.setAddress(new Address(3));

        loggingNode1.addRoute(new Route(loggingNode1.getInterfaces().get(0), loggingNode2.getAddress()));
        loggingNode1.addRoute(new Route(loggingNode1.getInterfaces().get(0), loggingNode3.getAddress()));

        loggingNode2.addRoute(new Route(loggingNode2.getInterfaces().get(1), loggingNode3.getAddress()));
        loggingNode2.addRoute(new Route(loggingNode2.getInterfaces().get(0), loggingNode1.getAddress()));

        loggingNode3.addRoute(new Route(loggingNode2.getInterfaces().get(1), loggingNode1.getAddress()));
        loggingNode3.addRoute(new Route(loggingNode2.getInterfaces().get(1), loggingNode1.getAddress()));

        loggingNode1.action("1->3", loggingNode3.getAddress());
        System.out.println("-------");
        loggingNode3.action("3->1", loggingNode1.getAddress());
    }
}

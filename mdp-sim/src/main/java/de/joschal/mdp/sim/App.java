package de.joschal.mdp.sim;

import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.core.inbound.IDatagramController;
import de.joschal.mdp.sim.core.inbound.IMessageController;
import de.joschal.mdp.sim.core.inbound.INetController;
import de.joschal.mdp.sim.core.inbound.INodeController;
import de.joschal.mdp.sim.core.logic.DatagramController;
import de.joschal.mdp.sim.core.logic.MessageController;
import de.joschal.mdp.sim.core.logic.NetController;
import de.joschal.mdp.sim.core.logic.NodeController;
import de.joschal.mdp.sim.core.outbound.IGraphReader;
import de.joschal.mdp.sim.core.outbound.IGraphWriter;
import de.joschal.mdp.sim.outbound.GraphReader;
import de.joschal.mdp.sim.outbound.GraphWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    public static final int DEFAULT_MAX_HOPS = 10;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    Graph graph() {
        return new Graph();
    }

    @Bean
    IGraphReader graphReader() {
        return new GraphReader();
    }

    @Bean
    IGraphWriter graphWriter() {
        return new GraphWriter();
    }


    @Bean
    INetController netController(Graph graph, IGraphReader graphReader, IGraphWriter graphWriter) {
        return new NetController(graph, graphReader, graphWriter);
    }

    @Bean
    INodeController nodeController(Graph graph) {
        return new NodeController(graph);
    }

    @Bean
    IDatagramController datagramController(Graph graph) {
        return new DatagramController(graph);
    }

    @Bean
    IMessageController messageController(Graph graph, IDatagramController datagramController) {
        return new MessageController(datagramController, graph);
    }

}

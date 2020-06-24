package de.joschal.amp.sim;

import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.inbound.IMessageController;
import de.joschal.amp.sim.core.inbound.INetworkController;
import de.joschal.amp.sim.core.inbound.INodeController;
import de.joschal.amp.sim.core.logic.MessageController;
import de.joschal.amp.sim.core.logic.NetController;
import de.joschal.amp.sim.core.logic.NodeController;
import de.joschal.amp.sim.core.logic.utils.Scheduler;
import de.joschal.amp.sim.core.outbound.IGraphReader;
import de.joschal.amp.sim.core.outbound.IGraphWriter;
import de.joschal.amp.sim.outbound.GraphReader;
import de.joschal.amp.sim.outbound.GraphWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

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
    Scheduler scheduler() {
        return new Scheduler();
    }

    @Bean
    INetworkController netController(Graph graph, IGraphReader graphReader, IGraphWriter graphWriter, Scheduler scheduler) {
        return new NetController(scheduler, graphReader, graphWriter);
    }

    @Bean
    INodeController nodeController(Graph graph) {
        return new NodeController();
    }

    @Bean
    IMessageController messageController(Graph graph) {
        return new MessageController();
    }

}

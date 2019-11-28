package de.joschal.mdp.sim;

import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.Node;
import de.joschal.mdp.core.logic.staticrouting.StaticRouter;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.parse.Parser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static guru.nidi.graphviz.model.Factory.mutNode;

@Slf4j
public class DotParsing {

    public static HashMap<Address, Node> readGraphFromFile(String filename, HashMap<Address, Node> nodes, HashMap<String, DataLink> edges) {

        try (InputStream dot = DotParsing.class.getResourceAsStream(filename)) {
            MutableGraph mutableGraph = new Parser().read(dot);


            // Create all Network Nodes on first pass
            for (MutableNode mutableNode : mutableGraph.nodes()) {
                Node node = new Node(new Address(mutableNode.name().toString()), new StaticRouter());
                nodes.put(node.getAddress(), node);
            }

            // Create Network Links on second pass
            for (MutableNode mutableNode : mutableGraph.nodes()) {
                for (Link link : mutableNode.links()) {
                    Address from = new Address(mutableNode.name().toString());
                    Address to = new Address(((PortNode) link.to()).name().toString());
                    DataLink dataLink = Setup.linkNodes(from, to, nodes);
                    edges.put(dataLink.getName(), dataLink);
                }
            }

            return nodes;
        } catch (Exception e) {
            log.error("Something went wrong while parsing a dot file", e);
            return null;
        }

    }

    public static void writeGraphToFile(HashMap<String, DataLink> edges) throws IOException {

        MutableGraph graph = Factory.mutGraph().setDirected(false);

        for (DataLink link : edges.values()) {
            graph.add(
                    mutNode(link.getA().getNode().getAddress().toString())
                            .addLink(mutNode(link.getB().getNode().getAddress().toString())));
        }

        Graphviz.fromGraph(graph).width(1000).render(Format.PNG).toFile(new File("/Users/joschal/Desktop/ex1m.png"));
    }
}

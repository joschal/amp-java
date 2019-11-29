package de.joschal.mdp.sim.outbound;

import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.core.entities.protocol.Address;
import de.joschal.mdp.core.logic.simple.SimpleNode;
import de.joschal.mdp.core.logic.staticrouting.StaticRouter;
import de.joschal.mdp.sim.Setup;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.core.outbound.IGraphReader;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.PortNode;
import guru.nidi.graphviz.parse.Parser;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class GraphReader implements IGraphReader {

    @Override
    public Graph readGraph(String filename) {
        Graph graph = new Graph();

        try (InputStream dot = getClass().getResourceAsStream(filename)) {
            MutableGraph dotGraph = new Parser().read(dot);

            // Create all Network Nodes on first pass
            for (MutableNode dotNode : dotGraph.nodes()) {
                SimpleNode simpleNode = new SimpleNode(dotNode.name().toString(), new StaticRouter());
                graph.addNode(simpleNode);
            }

            // Create Network Links on second pass
            for (MutableNode dotNode : dotGraph.nodes()) {
                // For each node, iterate over all links
                for (Link dotLink : dotNode.links()) {
                    // Extract names
                    Address from = new Address(dotNode.name().toString());
                    Address to = new Address(((PortNode) dotLink.to()).name().toString());
                    // Link nodes and store edge
                    DataLink dataLink = Setup.linkNodes(graph.getNode(from), graph.getNode(to));
                    graph.addEdge(dataLink);
                }
            }

            return graph;
        } catch (Exception e) {
            log.error("Something went wrong while parsing a dot file", e);
            return null;
        }

    }
}

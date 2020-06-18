package de.joschal.amp.sim.outbound;

import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.io.DataLink;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.outbound.IGraphReader;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.PortNode;
import guru.nidi.graphviz.parse.Parser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;

import static de.joschal.amp.sim.core.logic.utils.Linker.linkNodes;

@Slf4j
public class GraphReader implements IGraphReader {


    @Override
    public Graph readGraph(String filename) {
        Graph graph = new Graph();

        try (InputStream dot = FileUtils.openInputStream(new File(filename))) {
            MutableGraph dotGraph = new Parser().read(dot);

            // Create all Network Nodes on first pass
            for (MutableNode dotNode : dotGraph.nodes()) {
                SimpleNode loggingNode = new SimpleNode(dotNode.name().toString());
                graph.addNode(loggingNode);
            }

            // Create Network Links on second pass
            for (MutableNode dotNode : dotGraph.nodes()) {
                // For each node, iterate over all links
                for (Link dotLink : dotNode.links()) {
                    // Extract names
                    String from = dotNode.name().toString();
                    String to = ((PortNode) dotLink.to()).name().toString();
                    // Link nodes and store edge
                    DataLink dataLink = linkNodes(graph.getNodebyId(from), graph.getNodebyId(to));
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

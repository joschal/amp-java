package de.joschal.amp.sim.outbound;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.io.DataLink;
import de.joschal.amp.sim.core.entities.Graph;
import de.joschal.amp.sim.core.outbound.IGraphWriter;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static guru.nidi.graphviz.model.Factory.mutNode;

@Slf4j
public class GraphWriter implements IGraphWriter {

    @Override
    public void graphToGraphic(Graph networkGraph, String filename) {

        MutableGraph dotGraph = Factory.mutGraph().setDirected(false);

        // Linked nodes
        for (DataLink link : networkGraph.getEdges().values()) {
            dotGraph.add(
                    mutNode(link.getA().getNode().getId())
                            .addLink(mutNode(link.getB().getNode().getId())));
        }

        // Unlinked nodes
        for (AbstractNode abstractNode : networkGraph.getNodes().values()) {
            dotGraph.add(mutNode(abstractNode.getId()));
        }

        try {
            Graphviz.fromGraph(dotGraph).width(2048).render(Format.PNG).toFile(new File(filename));

            Runtime.getRuntime().exec(String.format("open %s", filename));
        } catch (IOException e) {
            log.error("Dot File could not be written", e);
        }

    }

    public static final String PREFIX = "graph {\n";
    public static final String POSTFIX = "}";
    public static final String SEPARATOR = " -- ";

    @Override
    public void graphToDot(Graph graph, String filename) {

        StringBuilder sb = new StringBuilder().append(PREFIX);

        // List all nodes explicitly to inlclude nodes which are not linked to anything
        for (AbstractNode node : graph.getNodes().values()) {
            sb.append("    " + node.getId() + "\n");
        }

        // List all individual connections
        for (DataLink edge : graph.getEdges().values()) {
            sb.append("    ");
            sb.append(edge.getA().getNode().getId());
            sb.append(SEPARATOR);
            sb.append(edge.getB().getNode().getId());
            sb.append("\n");
        }

        sb.append(POSTFIX);

        try {
            Files.write(Paths.get(filename), sb.toString().getBytes());
        } catch (IOException e) {
            log.error("Dot file could not be written to {}", filename, e);
        }

    }
}

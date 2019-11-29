package de.joschal.mdp.sim.outbound;

import de.joschal.mdp.core.entities.network.DataLink;
import de.joschal.mdp.sim.core.entities.Graph;
import de.joschal.mdp.sim.core.outbound.IGraphWriter;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.mutNode;

@Slf4j
public class GraphWriter implements IGraphWriter {

    @Override
    public void graphToGraphic(Graph networkGraph, String filename) {

        MutableGraph dotGraph = Factory.mutGraph().setDirected(false);

        for (DataLink link : networkGraph.getEdges()) {
            dotGraph.add(
                    mutNode(link.getA().getNode().getAddress().toString())
                            .addLink(mutNode(link.getB().getNode().getAddress().toString())));
        }

        try {
            Graphviz.fromGraph(dotGraph).width(1024).render(Format.PNG).toFile(new File(filename));
        } catch (IOException e) {
            log.error("Dot File could not be written", e);
        }

    }

    @Override
    public void graphToDot(Graph graph, String filename) {

        //TODO list all nodes in the beginning, to include unlinked nodes
        String prefix = "graph {\n";
        String postfix = "}";
        String seperator = " -- ";

        StringBuilder sb = new StringBuilder().append(prefix);

        for (DataLink edge : graph.getEdges()) {
            sb.append("\t");
            sb.append(edge.getA().getNode().getAddress().toString());
            sb.append(seperator);
            sb.append(edge.getB().getNode().getAddress().toString());
            sb.append("\n");
        }

        sb.append(postfix);

        String outputString = sb.toString();
        outputString.replace("[", "");
        outputString.replace("]", "");

        System.out.println(outputString);

    }
}

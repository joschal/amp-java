package de.joschal.mdp;

public class FileToNetworkToFile {

/*
    HashMap<Address, AbstractNode> nodes = new HashMap<>();

    HashMap<String, DataLink> edges = new HashMap<>();

    @Test
    public void fileToNetworkToFile() throws IOException {

        MutableGraph mutableGraph = DotParsing.readGraphFromFile("/color.dot");

        // Create all Network Nodes on first pass
        for (MutableNode mutableNode : mutableGraph.nodes()) {
            Node node = new Node(new Address(mutableNode.name().toString()), new StaticRouter());
            nodes.put(node.getAddress(), node);
        }

        assertEquals(mutableGraph.nodes().size(), nodes.size());

        // Create Network Links on second pass
        for (MutableNode mutableNode : mutableGraph.nodes()) {
            for (Link link : mutableNode.links()) {
                Address from = new Address(mutableNode.name().toString());
                Address to = new Address(((PortNode) link.to()).name().toString());
                DataLink dataLink = Setup.linkNodes(from , to,nodes);
                edges.put(dataLink.getName(), dataLink);
            }
        }


        DotParsing.writeGraphToFile(edges);

    }
*/
}

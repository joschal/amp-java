package de.joschal.amp.sim.inbound;

import de.joschal.amp.core.logic.nodes.SimpleNode;
import de.joschal.amp.sim.core.inbound.INodeController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@Slf4j
@ShellComponent
@AllArgsConstructor
public class NodeShell {

    private INodeController nodeController;

    @ShellMethod(value = "Add node to network", key = "node add")
    public boolean addNode(@ShellOption String id) {
        SimpleNode node = new SimpleNode(id);
        return nodeController.addNode(node);
    }

    @ShellMethod(value = "Remove node from network", key = "node remove")
    public boolean removeNode(@ShellOption String id) {
        return nodeController.removeNode(id);
    }

    @ShellMethod(value = "Add new Data Link between nodes", key = "node link")
    public boolean linkNode(
            @ShellOption String source,
            @ShellOption String destination) {
        return nodeController.linkNodes(source, destination);
    }

    @ShellMethod(value = "Show info about a specific node", key = "node info")
    public String infoNode(@ShellOption String id) {
        return nodeController.nodeInfo(id);
    }
}

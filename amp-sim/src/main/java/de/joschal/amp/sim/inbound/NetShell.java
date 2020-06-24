package de.joschal.amp.sim.inbound;

import de.joschal.amp.sim.core.inbound.INetworkController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@Slf4j
@AllArgsConstructor
@ShellComponent
@ShellCommandGroup("Network")
public class NetShell {

    private INetworkController netController;

    private static final String DEFAULT_PATH = "/Users/joschal/Desktop/";

    @ShellMethod(value = "Reads network graph from .odt file", key = "network read")
    public String readNetwork(
            @ShellOption String filename
    ) {
        netController.readNetworkFromFile(DEFAULT_PATH + filename);
        return "ok";
    }

    @ShellMethod(value = "Persist network graph to .odt file", key = "network persist")
    public String persistNetwork(
            @ShellOption String filename
    ) {
        netController.persistNetworkToFile(DEFAULT_PATH + filename);
        return "ok";
    }

    @ShellMethod(value = "Export network graph as PNG", key = "network show")
    public String showNetwork(
            @ShellOption String filename
    ) {
        // TODO make filename optional. User shuld be abke to export to a specific destination
        netController.showNet();
        return "ok";
    }

    @ShellMethod(value = "Triggers a scheduler tick", key = "network tick")
    public void tick() {
        netController.networkTick();
    }
}

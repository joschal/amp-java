package de.joschal.amp.sim.core.inbound;

public interface INetworkController {

    /**
     * Reads network from dot-file
     * @param name filename to read from
     */
    void readNetworkFromFile(String name);

    /**
     * Persists in memory network to dot-file
     * @param outputName filename to persist to
     */
    void persistNetworkToFile(String outputName);

    /**
     * Show network as png
     */
    void showNet();

    /**
     * Triggers network tick (data transfer and jobs)
     */
    void networkTick();


}

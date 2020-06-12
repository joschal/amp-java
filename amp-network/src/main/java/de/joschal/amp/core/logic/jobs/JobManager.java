package de.joschal.amp.core.logic.jobs;

import de.joschal.amp.core.entities.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * This class holds references to active jobs
 */
@Getter
@Setter
public class JobManager {

    // Only one of these at a time
    AddressAcquisitionJob addressAcquisitionJob = null;

    // Route discovery jobs, ordered by address
    Map<Address, RouteDiscoveryJob> routeDiscoveryJobs = new HashMap<>();

    // Datagram acknowledgement jobs, ordered by their UUID
    Map<UUID, DatagramAckJob> datagramAckJobs = new HashMap<>();

    // List of generic jobs
    List<IJob> genericJobs = new LinkedList<>();

}

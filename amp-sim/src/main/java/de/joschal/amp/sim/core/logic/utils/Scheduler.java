package de.joschal.amp.sim.core.logic.utils;

import de.joschal.amp.core.entities.network.AbstractNode;
import de.joschal.amp.core.entities.network.DataLink;
import de.joschal.amp.core.logic.jobs.DatagramAckJob;
import de.joschal.amp.core.logic.jobs.IJob;
import de.joschal.amp.core.logic.jobs.RouteDiscoveryJob;
import de.joschal.amp.sim.core.entities.Graph;

import java.util.Collection;

/**
 * This class schedules layer 2 data exchange and asynchronous jobs within the nodes
 */
public class Scheduler {

    public void tick(Graph graph) {

        // transfer one message on each link
        // On busy nodes, this might lead to long job queues
        networkTick(graph.getEdges().values());

        // tick on all jobs on all nodes
        jobTick(graph.getNodes().values());
    }

    private void networkTick(Collection<DataLink> dataLinks) {

        // transfers one message on the link, round robin style
        for (DataLink dataLink : dataLinks) {
            dataLink.transferMessage();
        }

    }

    private void jobTick(Collection<AbstractNode> nodes) {

        for (AbstractNode node : nodes) {

            // if ZAL/AQ job is active, this it the only one to be executed
            if (node.getJobManager().getAddressAcquisitionJob() != null) {
                node.getJobManager().getAddressAcquisitionJob().tick();
                return;
            }

            // Tick on all other jobs

            for (RouteDiscoveryJob routeDiscoveryJob : node.getJobManager().getRouteDiscoveryJobs().values()) {
                routeDiscoveryJob.tick();
            }

            for (DatagramAckJob datagramAckJob : node.getJobManager().getDatagramAckJobs().values()) {
                datagramAckJob.tick();
            }

            for (IJob genericJob : node.getJobManager().getGenericJobs()) {
                genericJob.tick();
            }

        }

    }

}

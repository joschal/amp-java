package de.joschal.amp.performance;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class RouteQualityLogger {
    public int rounds = 0; // count of delivered datagrams
    public int ticks = 0; // ticks within the delivery of a datagram
    public int totalHopsOfKnownRoutes = 0; // accumulated hops of all known routes of all nodes during the delivery of one datagram
    public int differenceToOptimalRoutes = 0; // accumulated difference to the optimal routes during the delivery of one datagram
    public int countOfRoutesPerNode = 0; //

    // The lists log the data over miltiple rounds of datagram delivery
    public LinkedList<Integer> totalHopsOfKnownRoutesOverTime = new LinkedList<>();
    public LinkedList<Integer> differenceToOptimalRouteoverTime = new LinkedList<>();
    public LinkedList<Double> averageOfRoutesPerNodeOverTime = new LinkedList<>();
    public LinkedList<Double> qualityOverTime = new LinkedList<>();
    public LinkedList<Boolean> routeToDestinationWasAlreadyKnownOverTime = new LinkedList<>();
    public LinkedList<Double> routeToDestinationWasKnownAverage = new LinkedList<>();

    /**
     * One network tick during the delivery of a datagram
     *
     * @param totalHops                 sum of hops of all routes known to a node
     * @param differenceToOptimalRoutes difference ot the known routes to the optimal ones of the current node
     * @param countOfRoutesPerNode      Number of known routes to the node
     */
    public void tick(int totalHops, int differenceToOptimalRoutes, int countOfRoutesPerNode) {
        this.ticks++;
        this.totalHopsOfKnownRoutes += totalHops;
        this.differenceToOptimalRoutes += differenceToOptimalRoutes;
        this.countOfRoutesPerNode += countOfRoutesPerNode;
    }

    /**
     * Datagram was delievred sucesfully. Data is logged and logger is reset for the next round
     *
     * @param routeToDestinaitonWasAlreadyKnown did the node need a route discovery?
     */
    public void roundFinished(boolean routeToDestinaitonWasAlreadyKnown) {
        this.totalHopsOfKnownRoutesOverTime.add(this.totalHopsOfKnownRoutes);
        this.differenceToOptimalRouteoverTime.add(this.differenceToOptimalRoutes);
        this.qualityOverTime.add((double) this.differenceToOptimalRoutes / this.totalHopsOfKnownRoutes);
        this.averageOfRoutesPerNodeOverTime.add((double) this.countOfRoutesPerNode / ticks);
        this.routeToDestinationWasAlreadyKnownOverTime.add(routeToDestinaitonWasAlreadyKnown);
        this.ticks = 0;
        this.totalHopsOfKnownRoutes = 0;
        this.differenceToOptimalRoutes = 0;
        this.countOfRoutesPerNode = 0;
        this.rounds++;
    }

    /**
     * Formats the logged data for external processing
     *
     * @return formatted csv String
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);

        sb.append("count;Hops of known routes;difference to optimal routes;average counts of routes per node;percentage of known routes;overall route quality\n");

        for (int i = 0; i < rounds; i++) {
            sb.
                    append(i).
                    append(";").
                    append(this.totalHopsOfKnownRoutesOverTime.get(i)).
                    append(";").
                    append(this.differenceToOptimalRouteoverTime.get(i)).
                    append(";").
                    append(df.format(this.averageOfRoutesPerNodeOverTime.get(i))).
                    append(";").
                    append(df.format(this.routeToDestinationWasKnownAverage.get(i))).
                    append(";").
                    append(df.format(this.qualityOverTime.get(i))).
                    append("\n");
        }
        return sb.toString();
    }
}

/*
 * EnergyCalculator.java
 */

package be.ugent.caagt.pg.embedder.energy;

import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.Graph;

/**
 * Calculates an energy for a given graph embedded with a given fundamental domain
 */
public interface EnergyCalculator {
    
    public double calculateEnergy(Graph graph, FundamentalDomain domain);

}

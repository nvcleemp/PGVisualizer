/*
 * EnergyCalculator.java
 */

package be.ugent.twi.pg.embedder.energy;

import be.ugent.twi.pg.graph.FundamentalDomain;
import be.ugent.twi.pg.graph.Graph;

/**
 * Calculates an energy for a given graph embedded with a given fundamental domain
 */
public interface EnergyCalculator {
    
    public double calculateEnergy(Graph graph, FundamentalDomain domain);

}

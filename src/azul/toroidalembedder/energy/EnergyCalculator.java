/*
 * EnergyCalculator.java
 */

package azul.toroidalembedder.energy;

import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.general.Graph;

/**
 * Calculates an energy for a given graph embedded with a given fundamental domain
 */
public interface EnergyCalculator {
    
    public double calculateEnergy(Graph graph, FundamentalDomain domain);

}

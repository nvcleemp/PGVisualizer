/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;

import azul.toroidalembedder.energy.EnergyCalculator;
import azul.toroidalembedder.graph.Edge;
import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Vertex;

/**
 *
 * @author nvcleemp
 */
public class FastDomainAngleEmbedder implements Embedder {
    
    private Graph graph;
    private double epsilon;
    private double k;
    private EnergyCalculator energyCalculator;

    public FastDomainAngleEmbedder(Graph graph, double epsilon, double k, EnergyCalculator energyCalculator) {
        this.graph = graph;
        this.epsilon = epsilon;
        this.k = k;
        this.energyCalculator = energyCalculator;
    }

    public void initialize() {
        //
    }

    public void embed() {
        double energy = energyCalculator.calculateEnergy(graph, graph.getFundamentalDomain());
        double angleE = graph.getFundamentalDomain().getAngle() - epsilon > 0 ? graph.getFundamentalDomain().getAngle() - epsilon : epsilon;
        double energy2 = energyCalculator.calculateEnergy(graph, new FundamentalDomain(angleE, graph.getFundamentalDomain().getHorizontalSide(), graph.getFundamentalDomain().getVerticalSide()));
        double angle = graph.getFundamentalDomain().getAngle() + (energy2 - energy)*epsilon*k;
        if(angle <=0)
            angle = epsilon;
        else if(angle >=Math.PI)
            angle = Math.PI - epsilon;
        graph.setFundamentalDomain(new FundamentalDomain(angle, graph.getFundamentalDomain().getHorizontalSide(), graph.getFundamentalDomain().getVerticalSide()));
    }
}

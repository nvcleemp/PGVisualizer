/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;

import azul.toroidalembedder.energy.EnergyCalculator;
import azul.toroidalembedder.graph.DefaultGraph;
import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.gui.GraphListModel;

/**
 *
 * @author nvcleemp
 */
public class FastDomainEdgeEmbedder extends AbstractEmbedder {
    
    private double epsilon;
    private double k;
    private EnergyCalculator energyCalculator;

    public FastDomainEdgeEmbedder(Graph graph, double epsilon, double k, EnergyCalculator energyCalculator) {
        super(graph);
        this.epsilon = epsilon;
        this.k = k;
        this.energyCalculator = energyCalculator;
    }

    public FastDomainEdgeEmbedder(GraphListModel graphModel, double epsilon, double k, EnergyCalculator energyCalculator) {
        super(graphModel);
        this.epsilon = epsilon;
        this.k = k;
        this.energyCalculator = energyCalculator;
    }

    public void initialize() {
        //
    }

    public void embed() {
        if(!(graph instanceof DefaultGraph))
            return;
        double energy = energyCalculator.calculateEnergy(graph, graph.getFundamentalDomain());
        double HSE = graph.getFundamentalDomain().getHorizontalSide() - epsilon > 0 ? graph.getFundamentalDomain().getHorizontalSide() - epsilon : epsilon;
        double energy2 = energyCalculator.calculateEnergy(graph, new FundamentalDomain(graph.getFundamentalDomain().getAngle(), HSE, graph.getFundamentalDomain().getVerticalSide()));
        double HS = graph.getFundamentalDomain().getHorizontalSide() + (energy2 - energy)*epsilon*k;
        if(HS <=0)
            HS = epsilon;
        int factor = 1;
        while(HS*factor<0.2)
            factor+=10;
        ((DefaultGraph)graph).getFundamentalDomain().setSides(HS*factor, graph.getFundamentalDomain().getVerticalSide()*factor);
    }

    @Override
    protected void resetEmbedder() {
        //
    }
}

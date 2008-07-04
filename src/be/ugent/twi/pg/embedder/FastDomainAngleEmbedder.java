/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.embedder;

import be.ugent.twi.pg.embedder.energy.EnergyCalculator;
import be.ugent.twi.pg.graph.DefaultGraph;
import be.ugent.twi.pg.graph.FundamentalDomain;
import be.ugent.twi.pg.graph.Graph;
import be.ugent.twi.pg.visualizer.gui.GraphListModel;

/**
 *
 * @author nvcleemp
 */
public class FastDomainAngleEmbedder extends AbstractEmbedder {
    
    private double epsilon;
    private double k;
    private EnergyCalculator energyCalculator;

    public FastDomainAngleEmbedder(Graph graph, double epsilon, double k, EnergyCalculator energyCalculator) {
        super(graph);
        this.epsilon = epsilon;
        this.k = k;
        this.energyCalculator = energyCalculator;
    }

    public FastDomainAngleEmbedder(GraphListModel graphModel, double epsilon, double k, EnergyCalculator energyCalculator) {
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
        double angleE = graph.getFundamentalDomain().getAngle() - epsilon > 0 ? graph.getFundamentalDomain().getAngle() - epsilon : epsilon;
        double energy2 = energyCalculator.calculateEnergy(graph, new FundamentalDomain(angleE, graph.getFundamentalDomain().getHorizontalSide(), graph.getFundamentalDomain().getVerticalSide()));
        double angle = graph.getFundamentalDomain().getAngle() + (energy2 - energy)*epsilon*k;
        if(angle <=0)
            angle = epsilon;
        else if(angle >=Math.PI)
            angle = Math.PI - epsilon;
        ((DefaultGraph)graph).getFundamentalDomain().setAngle(angle);
    }

    @Override
    protected void resetEmbedder() {
        //
    }
}

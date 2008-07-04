/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.embedder;

import be.ugent.twi.pg.embedder.energy.EnergyCalculator;
import be.ugent.twi.pg.graph.FundamentalDomain;
import be.ugent.twi.pg.graph.DefaultGraph;
import be.ugent.twi.pg.graph.Graph;
import be.ugent.twi.pg.visualizer.gui.GraphListModel;

/**
 *
 * @author nvcleemp
 */
public class DomainAngleEmbedder extends AbstractEmbedder{
    
    private int intervals;
    private int zoom;
    private double initialSearchAngle;
    private double currentSearchAngle;
    private boolean lock = false;
    private EnergyCalculator energyCalculator;

    public DomainAngleEmbedder(Graph graph, int intervals, int zoom, double initialSearchAngle, EnergyCalculator energyCalculator) {
        super(graph);
        this.intervals = intervals;
        this.zoom = zoom;
        this.initialSearchAngle = initialSearchAngle;
        this.energyCalculator = energyCalculator;
    }

    public DomainAngleEmbedder(GraphListModel graphModel, int intervals, int zoom, double initialSearchAngle, EnergyCalculator energyCalculator) {
        super(graphModel);
        this.intervals = intervals;
        this.zoom = zoom;
        this.initialSearchAngle = initialSearchAngle;
        this.energyCalculator = energyCalculator;
    }

    public void initialize() {
        currentSearchAngle = initialSearchAngle;
    }

    public void embed() {
        if(!(graph instanceof DefaultGraph))
            return;
        if(lock)
            return;
        lock = true;
        double minAngle = graph.getFundamentalDomain().getAngle();
        double minCost = energyCalculator.calculateEnergy(graph, graph.getFundamentalDomain());
        
        double startAngle = graph.getFundamentalDomain().getAngle() - currentSearchAngle/2;
        double endAngle = graph.getFundamentalDomain().getAngle() + currentSearchAngle/2;
        double increment = currentSearchAngle/intervals;
        if(startAngle<=0) startAngle = increment;
        if(endAngle>=Math.PI) endAngle = Math.PI - increment;
        
        FundamentalDomain domain = new FundamentalDomain(startAngle, graph.getFundamentalDomain().getHorizontalSide(), graph.getFundamentalDomain().getVerticalSide());
        while(domain.getAngle()<endAngle){
            double cost = energyCalculator.calculateEnergy(graph, domain);
            if(cost < minCost){
                minAngle = domain.getAngle();
                minCost = cost;
            }
            domain.addToAngle(increment);
        }
        if(graph.getFundamentalDomain().getAngle()!=minAngle)
            ((DefaultGraph)graph).getFundamentalDomain().setAngle(minAngle);
        currentSearchAngle /= zoom;
        lock = false;
    }

    @Override
    protected void resetEmbedder() {
        currentSearchAngle = initialSearchAngle;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;

import azul.toroidalembedder.energy.EnergyCalculator;
import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.gui.GraphModel;

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

    public DomainAngleEmbedder(GraphModel graphModel, int intervals, int zoom, double initialSearchAngle, EnergyCalculator energyCalculator) {
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
            graph.setFundamentalDomain(new FundamentalDomain(minAngle, graph.getFundamentalDomain().getHorizontalSide(), graph.getFundamentalDomain().getVerticalSide()));
        currentSearchAngle /= zoom;
        lock = false;
    }

    @Override
    protected void resetEmbedder() {
        currentSearchAngle = initialSearchAngle;
    }
}

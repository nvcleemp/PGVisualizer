/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 *
 * @author nvcleemp
 */
public class Polygon2D {
    
    private double[] xpoints;
    private double[] ypoints;
    private Line2D[] edges;

    public Polygon2D(double[] xpoints, double[] ypoints) {
        if(xpoints.length!=ypoints.length)
            throw new RuntimeException();
        this.xpoints = xpoints;
        this.ypoints = ypoints;
    }
    
    public void draw(Graphics2D g){
        if(edges==null)
            makeEdges();
        for (Line2D line : edges)
            g.draw(line);
    }
    
    private void makeEdges(){
        int n = xpoints.length;
        edges = new Line2D[n];
        for (int i = 0; i < n; i++) {
            edges[i]=new Line2D.Double(xpoints[i], ypoints[i], xpoints[(i+1)%n], ypoints[(i+1)%n]);
        }
    }

}

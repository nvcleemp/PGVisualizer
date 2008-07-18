/* FundamentalDomain.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.pg.graph;

import be.ugent.caagt.pg.visualizer.Polygon2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class FundamentalDomain {

    private double angle;
    private double horizontalSide;
    private double verticalSide;
    private double domainHeight;
    private Polygon2D edge;
    
    public FundamentalDomain() {
        this(Math.PI/2, 2.0, 2.0);
    }

    public FundamentalDomain(double angle, double horizontalSide, double verticalSide) {
        if(angle <= 0 || angle >= Math.PI)
            throw new IllegalArgumentException("Angle must be between 0 and Pi radials.");
        if(horizontalSide <= 0 || verticalSide <= 0)
            throw new IllegalArgumentException("Sides must be positive.");
        this.angle = angle;
        this.horizontalSide = horizontalSide;
        this.verticalSide = verticalSide;
        domainHeight = verticalSide * Math.sin(angle);
    }
    
    /**
     * Transform coordinates from unit square (-1,-1) - (1,1) to coordinates inside parallellogram.
     */
    public double[] transform(double x, double y){
        double[] coords = new double[2];
        coords[0] = 0.5*horizontalSide*x + 0.5*domainHeight*y/Math.tan(angle);
        coords[1] = 0.5*domainHeight*y;
        return coords;
    }
    
    /**
     * Transform coordinates from parallellogram to coordinates inside unit square (-1,-1) - (1,1).
     */
    public double[] inverseTransform(double x, double y){
        double[] coords = new double[2];
        coords[0] = 2*x/horizontalSide - 2*y/(horizontalSide*Math.tan(angle));
        coords[1] = 2*y/domainHeight;
        return coords;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle){
        if(!(angle <= 0 || angle >= Math.PI)){
            this.angle = angle;
            edge = null;
            domainHeight = verticalSide * Math.sin(angle);
            fireFundamentalDomainChanged();
        }
    }
    public double getDomainHeight() {
        return domainHeight;
    }

    public double getHorizontalSide() {
        return horizontalSide;
    }

    public void setHorizontalSide(double horizontalSide) {
        if(horizontalSide>0){
            this.horizontalSide = horizontalSide;
            edge = null;
            fireFundamentalDomainChanged();
        }
    }

    public double getVerticalSide() {
        return verticalSide;
    }

    public void setVerticalSide(double verticalSide) {
        if(verticalSide>0){
            this.verticalSide = verticalSide;
            edge = null;
            domainHeight = verticalSide * Math.sin(angle);
            fireFundamentalDomainChanged();
        }
    }

    public void setSides(double horizontalSide, double verticalSide) {
        if(horizontalSide>0 && verticalSide>0){
            this.horizontalSide = horizontalSide;
            this.verticalSide = verticalSide;
            edge = null;
            domainHeight = verticalSide * Math.sin(angle);
            fireFundamentalDomainChanged();
        }
    }

    public void setSidesAndAngle(double horizontalSide, double verticalSide, double angle) {
        if(horizontalSide>0 && verticalSide>0 && !(angle <= 0 || angle >= Math.PI)){
            this.horizontalSide = horizontalSide;
            this.verticalSide = verticalSide;
            this.angle = angle;
            edge = null;
            domainHeight = verticalSide * Math.sin(angle);
            fireFundamentalDomainChanged();
        }
    }

    public Polygon2D getBorder() {
        if(edge!=null)
            return edge;
        
        double horizontalOffset = (angle != Math.PI/2 ? domainHeight / (Math.tan(angle)*2) : 0);
        
        double[] xpoints = new double[4];
        double[] ypoints = new double[4];
        
        xpoints[0] = - horizontalSide/2 - horizontalOffset;
        xpoints[1] =   horizontalSide/2 - horizontalOffset;
        xpoints[2] =   horizontalSide/2 + horizontalOffset;
        xpoints[3] = - horizontalSide/2 + horizontalOffset;
        
        ypoints[0] = - domainHeight/2;
        ypoints[1] = - domainHeight/2;
        ypoints[2] =   domainHeight/2;
        ypoints[3] =   domainHeight/2;
        
        edge = new Polygon2D(xpoints, ypoints);
        
        return edge;
    }
    
    public Point2D getOrigin(int x, int y){
        double horizontalOffset = (angle != Math.PI/2 ? domainHeight / Math.tan(angle) : 0);
        return new Point2D.Double(x*horizontalSide + horizontalOffset*y, domainHeight*y);
    }
    
    public boolean inDomain(double x, double y, int domainX, int domainY){
        Point2D origin = getOrigin(domainX, domainY);
        x -= origin.getX();
        y -= origin.getY();
        double[] coords = inverseTransform(x, y);
        x = coords[0];
        y = coords[1];
        return x <= 1 && x>=-1 && y<=1 && y>=-1;
    }
    
    public double getHorizontalShift(){
        return (angle != Math.PI/2 ? domainHeight / Math.tan(angle) : 0);
    }
    
    List<FundamentalDomainListener> listeners = new ArrayList<FundamentalDomainListener>();
    
    public void addFundamentalDomainListener(FundamentalDomainListener listener){
        listeners.add(listener);
    }
    
    public void removeFundamentalDomainListener(FundamentalDomainListener listener){
        listeners.remove(listener);
    }
    
    private void fireFundamentalDomainChanged(){
        for (FundamentalDomainListener listener : listeners) {
            listener.fundamentalDomainShapeChanged();
        }
    }

    public void addToAngle(double d){
        if(angle + d <= 0 || angle + d >= Math.PI)
            return;
        angle += d;
        edge = null;
        domainHeight = verticalSide * Math.sin(angle);
        fireFundamentalDomainChanged();
    }
    
    public void addToHorizontalSide(double d){
        if(horizontalSide + d > 0) {
            horizontalSide += d;
            edge = null;
            fireFundamentalDomainChanged();
        }
    }
    
    public void addToVerticalSide(double d){
        if(verticalSide + d > 0) {
            verticalSide += d;
            edge = null;
            domainHeight = verticalSide * Math.sin(angle);
            fireFundamentalDomainChanged();
        }
    }
}

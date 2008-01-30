/*
 * Area.java
 *
 * Created on January 10, 2008, 12:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder.graph;

import azul.toroidalembedder.Polygon2D;
import java.awt.geom.Point2D;

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
        double[] coords = {0.5*horizontalSide*x + (angle != Math.PI/2 ? 0.5*horizontalSide*y/Math.tan(angle) : 0), 0.5*domainHeight*y};
        return coords;
    }
    
    /**
     * Transform coordinates from parallellogram to coordinates inside unit square (-1,-1) - (1,1).
     */
    public double[] inverseTransform(double x, double y){
        double[] coords = new double[2];
        if(angle != Math.PI/2)
            coords[0] = 2*x/horizontalSide - 2*y/(Math.tan(angle)*domainHeight);
        else
            coords[0] = 2*x/horizontalSide;
        coords[1] = 2*y/domainHeight;
        return coords;
    }
    
    public void reduceCoordinates(CoordinateAdjustment adjustment, double x, double y){
        adjustment.x = x;
        adjustment.y = y;
        adjustment.verticalTranslate = 0;
        adjustment.horizontalTranslate = 0;
        double horizontalShift = (angle != Math.PI/2 ? domainHeight / Math.tan(angle) : 0);
        while(adjustment.y<=-domainHeight/2){
            adjustment.verticalTranslate++;
            adjustment.y += domainHeight;
            adjustment.x += horizontalShift;
        }
        
        while(adjustment.y>domainHeight/2){
            adjustment.verticalTranslate--;
            adjustment.y -= domainHeight;
            adjustment.x -= horizontalShift;
        }
        
        double horizontalOffset = (angle != Math.PI/2 ? adjustment.y / Math.tan(angle) : 0);
        
        while(adjustment.x + horizontalOffset <= -horizontalSide/2){
            adjustment.horizontalTranslate++;
            adjustment.x += horizontalSide;
        }
        
        while(adjustment.x + horizontalOffset > horizontalSide/2){
            adjustment.horizontalTranslate--;
            adjustment.x -= horizontalSide;
        }
    }

    public double getAngle() {
        return angle;
    }

    public double getDomainHeight() {
        return domainHeight;
    }

    public double getHorizontalSide() {
        return horizontalSide;
    }

    public double getVerticalSide() {
        return verticalSide;
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
    
    public double getHorizontalShift(){
        return (angle != Math.PI/2 ? domainHeight / Math.tan(angle) : 0);
    }
    
    

    public static class CoordinateAdjustment{
        public int verticalTranslate;
        public int horizontalTranslate;
        public double x;
        public double y;
    }
}

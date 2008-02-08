/*
 * TorusView.java
 *
 * Created on January 10, 2008, 1:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;


import azul.toroidalembedder.Polygon2D;
import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Edge;
import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.FundamentalDomainListener;
import azul.toroidalembedder.graph.Vertex;
import azul.toroidalembedder.graph.GraphListener;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class TorusView extends JPanel implements GraphListener, FundamentalDomainListener{

    private Graph graph;
    private double widthView;
    private double heightView;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private FundamentalDomain fundamentalDomain;
    
    /** Creates a new instance of TorusView */
    public TorusView() {
        this(-1, -1, 1, 1);
    }
    
    public TorusView(Graph graph) {
        this(graph, -1, -1, 1, 1);   
    }
    
    public TorusView(Graph graph, int minX, int minY, int maxX, int maxY) {
        this(minX, minY, maxX, maxY);
        setGraph(graph, true);
        graph.addGraphListener(this);
    }
    
    public TorusView(FundamentalDomain fundamentalDomain, Graph graph, int minX, int minY, int maxX, int maxY) {
        this(minX, minY, maxX, maxY);
        setGraph(graph, true);
        graph.addGraphListener(this);
        setFundamentalDomain(fundamentalDomain);
    }
    
    public TorusView(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        if(graph!=null){
            widthView = (maxX - minX + 1)*fundamentalDomain.getHorizontalSide() + (fundamentalDomain.getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*fundamentalDomain.getVerticalSide()*Math.cos(fundamentalDomain.getAngle());
            heightView = ((maxY - minY + 1)*graph.getFundamentalDomain().getDomainHeight());
        }
    }
    
    public void setGraph(Graph graph, boolean changeDomain){
        this.graph = graph;
        if(graph!=null){
            if(changeDomain)
                setFundamentalDomain(graph.getFundamentalDomain());
            widthView = (maxX - minX + 1)*fundamentalDomain.getHorizontalSide() + (fundamentalDomain.getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*fundamentalDomain.getVerticalSide()*Math.cos(fundamentalDomain.getAngle());
            heightView = ((maxY - minY + 1)*fundamentalDomain.getDomainHeight());
        }
    }
    
    public void setFundamentalDomain(FundamentalDomain fundamentalDomain){
        if(fundamentalDomain!=null){
            if(this.fundamentalDomain!=null)
                this.fundamentalDomain.removeFundamentalDomainListener(this);
            fundamentalDomain.addFundamentalDomainListener(this);
            this.fundamentalDomain = fundamentalDomain;
            widthView = (maxX - minX + 1)*fundamentalDomain.getHorizontalSide() + (fundamentalDomain.getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*fundamentalDomain.getVerticalSide()*Math.cos(fundamentalDomain.getAngle());
            heightView = ((maxY - minY + 1)*fundamentalDomain.getDomainHeight());
            repaint();
        }
    }
    
    private void paintGrid(Graphics g){
        Polygon2D domain = fundamentalDomain.getBorder();

        for(int i = minX; i <= maxX; i++){
            for (int j = minY; j <= maxY; j++) {
                Graphics2D gr = (Graphics2D)(g.create());
                Point2D origin = fundamentalDomain.getOrigin(i, j);
                gr.translate(origin.getX(), origin.getY());
                gr.setColor(new Color(200,0,0));
                domain.draw(gr);
            }
        }
    }
    
    private void paint(Vertex vertex, Graphics2D g2, int areaX, int areaY){
        double r = 0.06;
        Ellipse2D ell = new Ellipse2D.Double(vertex.getX(areaX, areaY, fundamentalDomain)-r,vertex.getY(areaX, areaY, fundamentalDomain)-r,2*r,2*r);
        g2.setColor(Color.WHITE);
        g2.fill(ell);
        g2.setColor(Color.BLACK);
        g2.draw(ell);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        paintComponentImpl(g, getWidth(), getHeight(), true);
    }
    
    protected void paintComponentImpl(Graphics g, int width, int height, boolean paintGrid) {
        Graphics2D g2 = (Graphics2D)g.create();
        if(widthView==0 || heightView==0){
            widthView = (maxX - minX + 1)*fundamentalDomain.getHorizontalSide() + (fundamentalDomain.getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*fundamentalDomain.getVerticalSide()*Math.cos(fundamentalDomain.getAngle());
            heightView = (maxY - minY + 1)*fundamentalDomain.getDomainHeight();
        }
        double scaleX = width/widthView - 1;
        double scaleY = height/heightView - 1;
        double scale = Math.min(scaleX, scaleY);
        //g2.setClip(new HorizontalParallellogram((maxX - minX + 1)*graph.getFundamentalDomain().getHorizontalSide()*scale, (maxY - minY + 1)*graph.getFundamentalDomain().getVerticalSide()*scale, graph.getFundamentalDomain().getAngle(), 0, 0));
        g2.setColor(new Color(200, 200, 255));
        g2.fillRect(0, 0, width, height);
        g2.translate(width/2, height/2);
        g2.transform(AffineTransform.getScaleInstance(scale, scale));
        g2.setStroke(new BasicStroke(0.01f));
        if(paintGrid)
            paintGrid(g2);
        g2.setStroke(new BasicStroke(0.02f));
        g2.setColor(Color.BLACK);
        int minTargetX = 0;
        int maxTargetX = 0;
        int minTargetY = 0;
        int maxTargetY = 0;
        for (Vertex vertex : graph.getVertices()) {
            for (Edge e : vertex.getEdges()) {
                if(minTargetX > e.getTargetX())
                    minTargetX = e.getTargetX();
                if(maxTargetX < e.getTargetX())
                    maxTargetX = e.getTargetX();
                if(minTargetY > e.getTargetY())
                    minTargetY = e.getTargetY();
                if(maxTargetY < e.getTargetY())
                    maxTargetY = e.getTargetY();
            }
        }
        for (Vertex vertex : graph.getVertices()) {
            double x1 = vertex.getX(0, 0, fundamentalDomain);
            double y1 = vertex.getY(0, 0, fundamentalDomain);
            for (Edge e : vertex.getEdges()) {
                Line2D line = new Line2D.Double(x1,y1,e.getEnd().getX(e.getTargetX(), e.getTargetY(), fundamentalDomain), e.getEnd().getY(e.getTargetX(), e.getTargetY(), fundamentalDomain));
                for (int i = minX + minTargetX; i <= maxX + maxTargetX; i++)
                    for (int j = minY + minTargetY; j <= maxY + maxTargetY; j++){
                        Graphics2D gr = (Graphics2D)(g2.create());
                        Point2D origin = fundamentalDomain.getOrigin(i, j);
                        gr.translate(origin.getX(), origin.getY());

                        gr.draw(line);
                    }
            }
        }
        for (Vertex vertex : graph.getVertices()) {
            for (int i = minX; i <= maxX; i++)
                for (int j = minY; j <= maxY; j++){
                    Graphics2D gr = (Graphics2D)(g2.create());
                    Point2D origin = fundamentalDomain.getOrigin(i, j);
                    gr.translate(origin.getX(), origin.getY());

                    paint(vertex, gr, 0, 0);
                }
        }
    }
    
    public void setView(int minX, int minY, int maxX, int maxY){
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        if(graph!=null){
            widthView = (maxX - minX + 1)*fundamentalDomain.getHorizontalSide() + (fundamentalDomain.getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*fundamentalDomain.getVerticalSide()*Math.cos(fundamentalDomain.getAngle());
            heightView = ((maxY - minY + 1)*fundamentalDomain.getDomainHeight());
        }
        repaint();
    }
    
    public void graphChanged() {
        repaint();
    }
    
    public void fundamentalDomainChanged() {
        widthView = (maxX - minX + 1)*fundamentalDomain.getHorizontalSide() + (fundamentalDomain.getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*fundamentalDomain.getVerticalSide()*Math.cos(fundamentalDomain.getAngle());
        heightView = (maxY - minY + 1)*fundamentalDomain.getDomainHeight();
        repaint();
    }
    
    public void exportImage(){
        try {
            BufferedImage im = new BufferedImage((int)(widthView * 150), (int)(heightView * 150), BufferedImage.TYPE_INT_ARGB);
            paintComponentImpl(im.getGraphics(), im.getWidth(), im.getHeight(), false);
            JFileChooser chooser = new JFileChooser();
            if(chooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                if(!file.exists() || JOptionPane.showConfirmDialog(this, "Overwrite file " + file.toString(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    ImageIO.write(im, "PNG", file);
            }
        } catch (IOException ex) {
            Logger.getLogger(TorusView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet() || graph==null) {
            return super.getPreferredSize();
        } else {
            double scale = 600/((widthView > heightView ? heightView : widthView) - 1);
            return new Dimension((int)(widthView*scale), (int)(heightView*scale));
        }
    }
}

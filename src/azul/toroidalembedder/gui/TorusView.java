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
import javax.swing.event.ListDataEvent;

/**
 *
 * @author nvcleemp
 */
public class TorusView extends JPanel implements GraphListener, FundamentalDomainListener, GraphModelListener{

    private Graph graph;
    private double widthView;
    private double heightView;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private FundamentalDomain fundamentalDomain;
    private GraphModel graphListModel = null;
    
    /** Creates a new instance of TorusView */
    public TorusView() {
        setView(-1, -1, 1, 1);
    }
    
    public TorusView(int minX, int minY, int maxX, int maxY){
        setView(minX, minY, maxX, maxY);
    }
    
    public TorusView(Graph graph) {
        this(graph, -1, -1, 1, 1);   
    }
    
    public TorusView(GraphModel model) {
        this(model, -1, -1, 1, 1);   
    }
    
    public TorusView(Graph graph, int minX, int minY, int maxX, int maxY) {
        setView(minX, minY, maxX, maxY);
        setGraph(graph);
    }
    
    public TorusView(GraphModel model, int minX, int minY, int maxX, int maxY) {
        setView(minX, minY, maxX, maxY);
        setGraph(model.getSelectedGraph());
        graphListModel = model;
        model.addGraphModelListener(this);
    }
    
    public TorusView(FundamentalDomain fundamentalDomain, Graph graph, int minX, int minY, int maxX, int maxY) {
        setView(minX, minY, maxX, maxY);
        setGraph(graph);
        graph.addGraphListener(this);
        setFundamentalDomain(fundamentalDomain);
    }

    public void setGraph(Graph graph){
        if(this.graph!=null)
            this.graph.removeGraphListener(this);
        this.graph = graph;
        this.graph.addGraphListener(this);
        if(graph!=null){
            widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = ((maxY - minY + 1)*getFundamentalDomain().getDomainHeight());
        }
        repaint();
    }

    public FundamentalDomain getFundamentalDomain() {
        if(fundamentalDomain==null)
            return graph.getFundamentalDomain();
        else
            return fundamentalDomain;
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
        Polygon2D domain = getFundamentalDomain().getBorder();

        for(int i = minX; i <= maxX; i++){
            for (int j = minY; j <= maxY; j++) {
                Graphics2D gr = (Graphics2D)(g.create());
                Point2D origin = getFundamentalDomain().getOrigin(i, j);
                gr.translate(origin.getX(), origin.getY());
                gr.setColor(new Color(200,0,0));
                domain.draw(gr);
            }
        }
    }
    
    private void paint(Vertex vertex, Graphics2D g2, int areaX, int areaY, double r){
        Ellipse2D ell = new Ellipse2D.Double(vertex.getX(areaX, areaY, getFundamentalDomain())-r,vertex.getY(areaX, areaY, getFundamentalDomain())-r,2*r,2*r);
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
            widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = (maxY - minY + 1)*getFundamentalDomain().getDomainHeight();
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
            double x1 = vertex.getX(0, 0, getFundamentalDomain());
            double y1 = vertex.getY(0, 0, getFundamentalDomain());
            for (Edge e : vertex.getEdges()) {
                Line2D line = new Line2D.Double(x1,y1,e.getEnd().getX(e.getTargetX(), e.getTargetY(), getFundamentalDomain()), e.getEnd().getY(e.getTargetX(), e.getTargetY(), getFundamentalDomain()));
                for (int i = minX + minTargetX; i <= maxX + maxTargetX; i++)
                    for (int j = minY + minTargetY; j <= maxY + maxTargetY; j++){
                        Graphics2D gr = (Graphics2D)(g2.create());
                        Point2D origin = getFundamentalDomain().getOrigin(i, j);
                        gr.translate(origin.getX(), origin.getY());

                        gr.draw(line);
                    }
            }
        }
        for (Vertex vertex : graph.getVertices()) {
            for (int i = minX; i <= maxX; i++)
                for (int j = minY; j <= maxY; j++){
                    Graphics2D gr = (Graphics2D)(g2.create());
                    Point2D origin = getFundamentalDomain().getOrigin(i, j);
                    gr.translate(origin.getX(), origin.getY());

                    paint(vertex, gr, 0, 0, vertexSize*0.001*getFundamentalDomain().getHorizontalSide()/2);
                }
        }
    }
    
    private int vertexSize = 60; //in % of HS/20
    
    public int getVertexSize(){
        return vertexSize;
    }
    
    public void setVertexSize(int vertexSize){
        if(vertexSize!=this.vertexSize){
            this.vertexSize = vertexSize;
            repaint();
        }
    }
    
    public void setView(int minX, int minY, int maxX, int maxY){
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        if(graph!=null){
            widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = ((maxY - minY + 1)*getFundamentalDomain().getDomainHeight());
        }
        repaint();
    }
    
    public int getMaxX(){
        return maxX;
    }
    
    public int getMaxY(){
        return maxY;
    }
    
    public void graphChanged() {
        repaint();
    }
    
    public void fundamentalDomainChanged(FundamentalDomain oldDomain) {
        widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
        heightView = (maxY - minY + 1)*getFundamentalDomain().getDomainHeight();
        repaint();
    }
    
    public void fundamentalDomainShapeChanged() {
        widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
        heightView = (maxY - minY + 1)*getFundamentalDomain().getDomainHeight();
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

    public void selectedGraphChanged() {
        if(graphListModel!=null)
            setGraph(graphListModel.getSelectedGraph());
    }

    public void intervalAdded(ListDataEvent e) {
        //
    }

    public void intervalRemoved(ListDataEvent e) {
        //
    }

    public void contentsChanged(ListDataEvent e) {
        //
    }
}

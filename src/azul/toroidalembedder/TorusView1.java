/*
 * TorusView.java
 *
 * Created on January 10, 2008, 1:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder;

import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Edge;
import azul.toroidalembedder.graph.Vertex;
import azul.toroidalembedder.graph.GraphListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
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
public class TorusView1 extends JPanel /*implements GraphListener*/{

    private Graph graph;
    private int widthView;
    private int heightView;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    
    /** Creates a new instance of TorusView */
 /*   public TorusView1() {
        this(-1, -1, 1, 1);
    }
    
    public TorusView1(Graph graph) {
        this(graph, -1, -1, 1, 1);   
    }
    
    public TorusView1(Graph graph, int minX, int minY, int maxX, int maxY) {
        this(minX, minY, maxX, maxY);
        this.graph = graph;
        graph.addGraphListener(this);
    }
    
    public TorusView1(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        widthView = maxX - minX + 1;
        heightView = maxY - minY + 1;
        setPreferredSize(new Dimension(600, 600));
    }
    
    public void setGraph(Graph graph){
        this.graph = graph;
    }
    
    private void paintGrid(Graphics g, double scaleX, double scaleY){
        g.setColor(Color.BLACK);
        
        //Polygon domain = graph.getFundamentalDomain().getEdge();
        
        for(int i = minX * 2 - 1; i < maxX * 2 + 1; i+=2)
            g.drawLine((int)(i*scaleX), (int)((minY*2 - 1)*scaleY), (int)(i*scaleX), (int)((maxY*2 + 1)*scaleY));
        for(int i = minY * 2 - 1; i < maxY * 2 + 1; i+=2)
            g.drawLine((int)((minX*2 - 1)*scaleX), (int)(i*scaleY), (int)((maxX*2 + 1)*scaleX), (int)(i*scaleY));
    }
    
    private void paint(Vertex vertex, Graphics2D g2, int areaX, int areaY, double scaleX, double scaleY){
        double r = 4.0;
        Ellipse2D ell = new Ellipse2D.Double(vertex.getX(areaX)*scaleX-r,vertex.getY(areaY)*scaleY-r,2*r,2*r);
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
        g2.translate(width/2, height/2);
        double scaleX = width/(widthView*2.0);
        double scaleY = height/(heightView*2.0);
        if(paintGrid)
            paintGrid(g2, scaleX, scaleY);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.5f));
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
            int x1 = (int)(vertex.getX(0)*scaleX);
            int y1 = (int)(vertex.getY(0)*scaleY);
            for (Edge e : vertex.getEdges()) {
                for (int i = minX + minTargetX; i <= maxX + maxTargetX; i++)
                    for (int j = minY + minTargetY; j <= maxY + maxTargetY; j++)
                        g2.drawLine((int)(i*scaleX*2) + x1, (int)(j*scaleY*2) + y1, (int)(i*scaleX*2) + (int)(e.getEnd().getX(e.getTargetX())*scaleX), (int)(j*scaleY*2) + (int)(e.getEnd().getY(e.getTargetY())*scaleY));
            }
        }
        for (Vertex vertex : graph.getVertices()) {
            for (int i = minX; i <= maxX; i++)
                for (int j = minY; j <= maxY; j++){
                    paint(vertex, g2, i, j, scaleX, scaleY);
                }
        }
    }
    
    public void setView(int minX, int minY, int maxX, int maxY){
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        widthView = maxX - minX + 1;
        heightView = maxY - minY + 1;
        repaint();
    }
    
    public void graphChanged() {
        repaint();
    }
    
    public void exportImage(){
        try {
            BufferedImage im = new BufferedImage(widthView * 150, heightView * 150, BufferedImage.TYPE_INT_ARGB);
            paintComponentImpl(im.getGraphics(), im.getWidth(), im.getHeight(), false);
            JFileChooser chooser = new JFileChooser();
            if(chooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                if(!file.exists() || JOptionPane.showConfirmDialog(this, "Overwrite file " + file.toString(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    ImageIO.write(im, "PNG", file);
            }
        } catch (IOException ex) {
            Logger.getLogger(TorusView1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}

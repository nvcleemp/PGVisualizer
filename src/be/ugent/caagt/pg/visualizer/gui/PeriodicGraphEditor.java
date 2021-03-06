/* PeriodicGraphEditor.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008-2009 Universiteit Gent
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

package be.ugent.caagt.pg.visualizer.gui;


import be.ugent.caagt.pg.visualizer.Polygon2D;
import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.FundamentalDomainListener;
import be.ugent.caagt.pg.graph.GraphListener;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;

//TODO: factor out common code with TorusView!!!

/**
 *
 * @author nvcleemp
 */
public class PeriodicGraphEditor extends JPanel implements GraphListener, FundamentalDomainListener, GraphListModelListener, GraphSelectionListener, GraphFaceSelectionListener {
    
    private static final Color defaultVertexEdge = Color.BLACK;
    private static final Color defaultVertexFace = Color.WHITE;
    private static final Color defaultSelectedVertexEdge = Color.GREEN;
    
    private Graph graph;
    private double widthView;
    private double heightView;
    private FundamentalDomain fundamentalDomain;
    private GraphListModel graphListModel = null;
    private Color vertexEdge = defaultVertexEdge;
    private Color vertexFace = defaultVertexFace;
    private Color selectedVertexEdge = defaultSelectedVertexEdge;
    private GraphSelectionModel selectionModel = new DefaultGraphSelectionModel();
    private GraphFaceSelectionModel faceSelectionModel = new DefaultGraphFaceSelectionModel();
    private boolean paintFaces = false;
    private boolean paintSelectedFace = true;
    private Shape clip;
    
    private int lastX = 0;
    private int lastY = 0;
    
    public PeriodicGraphEditor(GraphListModel model) {
        setGraph(model.getSelectedGraph());
        graphListModel = model;
        model.addGraphModelListener(this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ViewVertex vv = getVertexAt(e.getX(), e.getY());
                ViewFace vf = getFaceAt(e.getX(), e.getY());
                if(e.isShiftDown()){
                    if(vv!=null){
                        getGraphSelectionModel().toggleVertex(vv.vertex);
                    } else if (vf!=null){
                        getFaceSelectionModel().toggleFace(vf.face);
                    }
                } else {
                    getGraphSelectionModel().clearSelection();
                    getFaceSelectionModel().clearSelection();
                    if(vv!=null){
                        getGraphSelectionModel().addVertex(vv.vertex);
                    } else if (vf!=null){
                        getFaceSelectionModel().toggleFace(vf.face);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
            
            
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(selectionModel.getSelectedVertices().length!=0 || faceSelectionModel.getSelectedFaces().length!=0){
                    if(widthView==0 || heightView==0){
                        widthView = getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
                        heightView = getFundamentalDomain().getDomainHeight();
                    }
                    double scale = Math.min(getWidth()/widthView - 1, getHeight()/heightView - 1);
                    double diffX = (e.getX() - lastX)/scale;
                    double diffY = (e.getY() - lastY)/scale;
                    Set<Vertex> toMove = new HashSet<Vertex>();
                    for (Vertex vertex : selectionModel.getSelectedVertices()) {
                        toMove.add(vertex);
                    }
                    for (Face face : faceSelectionModel.getSelectedFaces()) {
                        for (int i = 0; i < face.getSize(); i++) {
                            toMove.add(face.getVertexAt(i));
                        }
                    }
                    for (Vertex vertex : toMove) {
                        vertex.translate(diffX, diffY, getFundamentalDomain());
                    }

                }
                lastX = e.getX();
                lastY = e.getY();
            }
        });
        selectionModel.addGraphSelectionListener(new GraphSelectionListener() {
            public void graphSelectionChanged() {
                repaint();
            }
        });
        faceSelectionModel.addGraphFaceSelectionListener(new GraphFaceSelectionListener() {
            public void graphFaceSelectionChanged() {
                repaint();
            }
        });
    }

    public void setGraph(Graph graph){
        selectionModel.clearSelection();
        faceSelectionModel.clearSelection();
        if(this.graph!=null && this.graph instanceof DefaultGraph)
            ((DefaultGraph)this.graph).removeGraphListener(this);
        if(graph == null)
            return;
        this.graph = graph;
        if(this.graph instanceof DefaultGraph)
            ((DefaultGraph)this.graph).addGraphListener(this);
        if(graph!=null){
            widthView = getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = getFundamentalDomain().getDomainHeight();
        }
        clip = null;
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
            widthView = fundamentalDomain.getHorizontalSide() + (fundamentalDomain.getAngle()<=Math.PI/2 ? 1 : -1)*fundamentalDomain.getVerticalSide()*Math.cos(fundamentalDomain.getAngle());
            heightView = fundamentalDomain.getDomainHeight();
            repaint();
        }
    }
    
    private void paintGrid(Graphics g){
        Polygon2D domain = getFundamentalDomain().getBorder();
        Graphics2D gr = (Graphics2D)(g.create());
        Point2D origin = getFundamentalDomain().getOrigin(0, 0);
        gr.translate(origin.getX(), origin.getY());
        gr.setColor(new Color(200,0,0));
        domain.draw(gr);
    }
    
    private void paint(Vertex vertex, Graphics2D g2, int areaX, int areaY, double r, Color outer, Color inner){
        Ellipse2D ell = new Ellipse2D.Double(vertex.getX(areaX, areaY, getFundamentalDomain())-r,vertex.getY(areaX, areaY, getFundamentalDomain())-r,2*r,2*r);
        g2.setColor(inner);
        g2.fill(ell);
        g2.setColor(outer);
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
            widthView = getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = getFundamentalDomain().getDomainHeight();
        }
        double scaleX = width/widthView - 1;
        double scaleY = height/heightView - 1;
        double scale = Math.min(scaleX, scaleY);
        g2.setColor(new Color(200, 200, 255));
        g2.fillRect(0, 0, width, height);
        g2.translate(width/2, height/2);
        g2.transform(AffineTransform.getScaleInstance(scale, scale));
        if(clip==null)
            calculateClip();
        if(clip!=null)
            g2.setClip(clip);
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
        g2.setStroke(new BasicStroke(0.01f));
        if(paintGrid)
            paintGrid(g2);
        g2.setStroke(new BasicStroke(0.02f));
        g2.setColor(Color.BLACK);
              
        //paint faces
        /*if(paintFaces && graph instanceof DefaultGraph){
            for(Face f : ((DefaultGraph)graph).getFaces()){
                Shape s = f.getShape(getFundamentalDomain());
                Color c = null;
                if(getFaceHighlight()!=null)
                    c = getFaceHighlight().getColorFor(f);
                if(c==null)
                    c = FaceColorMapping.getColorFor(f.getSize());
                if(c==null)
                    c = Color.DARK_GRAY;
                c = new Color(c.getRed(), c.getGreen(), c.getBlue(), transparency);
                Graphics2D gr = (Graphics2D)(g2.create());
                Point2D origin = getFundamentalDomain().getOrigin(0, 0);
                gr.translate(origin.getX(), origin.getY());
                gr.setColor(c);
                gr.fill(s);
            }
        }*/
        
        //show selection of faces
        Face[] selectedFaces = faceSelectionModel.getSelectedFaces();
        if(paintSelectedFace && selectedFaces.length>0){
            Graphics2D gr = (Graphics2D)(g2.create());
            gr.setColor(new Color(0.9f, 0.9f, 0.9f, 0.9f));
            for(Face f : selectedFaces){
                Shape s = f.getShape(getFundamentalDomain());
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++){
                        Graphics2D gr2 = (Graphics2D)(gr.create());
                        Point2D origin = getFundamentalDomain().getOrigin(i, j);
                        gr2.translate(origin.getX(), origin.getY());
                        gr2.fill(s);
                    }
            }
        }

        //paint edges
        for (Vertex vertex : graph.getVertices()) {
            double x1 = vertex.getX(0, 0, getFundamentalDomain());
            double y1 = vertex.getY(0, 0, getFundamentalDomain());
            for (Edge e : vertex.getEdges()) {
                Line2D line = new Line2D.Double(x1,y1,e.getEnd().getX(e.getTargetX(), e.getTargetY(), getFundamentalDomain()), e.getEnd().getY(e.getTargetX(), e.getTargetY(), getFundamentalDomain()));
                for (int i = -1; i <= 1; i++){
                    for (int j = -1; j <= 1; j++){
                        Graphics2D gr = (Graphics2D)(g2.create());
                        Point2D origin = getFundamentalDomain().getOrigin(i, j);
                        gr.translate(origin.getX(), origin.getY());
                        gr.draw(line);
                    }
                }
            }
        }
        
        //paint vertices
        for (Vertex vertex : graph.getVertices()) {
            Color outer = vertexEdge;
            Color inner = vertexFace;
            if(selectionModel.isSelected(vertex))
                outer = selectedVertexEdge;
            VertexHighlighter theHighlight = getHighlight();
            if(theHighlight!=null && theHighlight.getColorFor(vertex)!=null)
                inner = theHighlight.getColorFor(vertex);
                for (int i = -1; i <= 1; i++){
                    for (int j = -1; j <= 1; j++){
                        Graphics2D gr = (Graphics2D)(g2.create());
                        Point2D origin = getFundamentalDomain().getOrigin(i, j);
                        gr.translate(origin.getX(), origin.getY());
                        paint(vertex, gr, 0, 0, vertexSize*0.001*getFundamentalDomain().getHorizontalSide()/2, outer, inner);
                    }
                }
        }
    }
    
    private int vertexSize = 20; //in % of HS/20
    
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
        if(graph!=null){
            widthView = getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = getFundamentalDomain().getDomainHeight();
        }
        repaint();
    }

    public void graphChanged() {
        repaint();
    }
    
    public void fundamentalDomainChanged(FundamentalDomain oldDomain) {
        widthView = getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
        heightView = getFundamentalDomain().getDomainHeight();
        clip = null;
        repaint();
    }
    
    public void fundamentalDomainShapeChanged() {
        widthView = getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
        heightView = getFundamentalDomain().getDomainHeight();
        clip = null;
        repaint();
    }
      
    public RenderedImage getBitmap(){
        BufferedImage im = new BufferedImage((int)(widthView * 150), (int)(heightView * 150), BufferedImage.TYPE_INT_ARGB);
        paintComponentImpl(im.getGraphics(), im.getWidth(), im.getHeight(), false);
        return im;
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
    
    public class ViewVertex {
        public Vertex vertex;
        public int domainX;
        public int domainY;
    }
    
    public ViewVertex getVertexAt(int x, int y){
        if(widthView==0 || heightView==0){
            widthView = getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = getFundamentalDomain().getDomainHeight();
        }
        double scaleX = getWidth()/widthView - 1;
        double scaleY = getHeight()/heightView - 1;
        double scale = Math.min(scaleX, scaleY);
        
        x -= getWidth()/2;
        y -= getHeight()/2;
        
        double x1 = x/scale;
        double y1 = y/scale;
        
        int domainX = 0;
        int domainY = 0;
        
        x1 -= getFundamentalDomain().getOrigin(domainX, domainY).getX();
        y1 -= getFundamentalDomain().getOrigin(domainX, domainY).getY();
        
        double r2 = (vertexSize*0.001*getFundamentalDomain().getHorizontalSide()/2)*(vertexSize*0.001*getFundamentalDomain().getHorizontalSide()/2);
        
        ViewVertex viewVertex = null;
        
        int j = graph.getVertices().size() - 1;
        
        while(viewVertex == null && j >=0){
            Vertex vertex = graph.getVertices().get(j);
            double d2 = (x1 - vertex.getX(getFundamentalDomain()))*(x1 - vertex.getX(getFundamentalDomain())) + (y1 - vertex.getY(getFundamentalDomain()))*(y1 - vertex.getY(getFundamentalDomain()));
            if(d2 <= r2){
                viewVertex = new ViewVertex();
                viewVertex.domainX = domainX;
                viewVertex.domainY = domainY;
                viewVertex.vertex = vertex;
            }
            j--;
        }
        
        return viewVertex;
    }

    public class ViewFace {
        public Face face;
        public int domainX;
        public int domainY;
    }
    
    public ViewFace getFaceAt(int x, int y){
        if(!(graph instanceof DefaultGraph))
            return null;
        if(widthView==0 || heightView==0){
            widthView = getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = getFundamentalDomain().getDomainHeight();
        }
        double scaleX = getWidth()/widthView - 1;
        double scaleY = getHeight()/heightView - 1;
        double scale = Math.min(scaleX, scaleY);
        
        x -= getWidth()/2;
        y -= getHeight()/2;
        
        double x1 = x/scale;
        double y1 = y/scale;
        
        int domainX = -2;
        int domainY = -2;
        
        List<Face> faces = ((DefaultGraph)graph).getFaces();
        int k = 0;
        
        while(domainX == -2 && k<faces.size()) {
            Shape face = faces.get(k).getShape(getFundamentalDomain());
            for(int i = -1; i <= 1; i++){
                for (int j = -1; j <= 1; j++) {
                    if(face.contains(x1 - getFundamentalDomain().getOrigin(i, j).getX(), y1 - getFundamentalDomain().getOrigin(i, j).getY())){
                        domainX = i;
                        domainY = j;
                    }
                }
            }
            k++;
        }
        
        if(domainX == -2)
            return null;
        
        ViewFace viewFace = new ViewFace();
        viewFace.face = faces.get(k-1);
        viewFace.domainX = domainX;
        viewFace.domainY = domainY;
        
        return viewFace;
    }

    public void graphSelectionChanged() {
        repaint();
    }
    
    public GraphSelectionModel getGraphSelectionModel(){
        return selectionModel;
    }
    
    private VertexHighlighter highlight = null;
    
    public void setHighlight(VertexHighlighter highlight){
        if(graphListModel==null)
            this.highlight = highlight;
        else
            graphListModel.getSelectedGraphGUIData().setHighlighter(highlight);
    }
    
    public VertexHighlighter getHighlight(){
        if(graphListModel==null)
            return highlight;
        else
            return graphListModel.getSelectedGraphGUIData().getHighlighter();
    }
    
    public ViewFace selectedFace = null;

    public void graphFaceSelectionChanged() {
        repaint();
    }

    public GraphFaceSelectionModel getFaceSelectionModel() {
        return faceSelectionModel;
    }
    
    private FaceHighlighter faceHighlight = null;
    
    public void setFaceHighlight(FaceHighlighter faceHighlight){
        if(graphListModel==null)
            this.faceHighlight = faceHighlight;
        else
            graphListModel.getSelectedGraphGUIData().setFaceHighlighter(faceHighlight);
    }
    
    public FaceHighlighter getFaceHighlight(){
        if(graphListModel==null)
            return faceHighlight;
        else
            return graphListModel.getSelectedGraphGUIData().getFaceHighlighter();
    }
    
    private int transparency = 255;

    public int getTransparency() {
        return transparency;
    }

    public void setTransparency(int transparency) {
        if(transparency <= 0)
            this.transparency = 0;
        else if(transparency >= 255)
            this.transparency = 255;
        else
            this.transparency = transparency;
        repaint();
    }
    private void calculateClip(){
        double horizontalOffset = (getFundamentalDomain().getAngle() != Math.PI/2 ? getFundamentalDomain().getDomainHeight() / (Math.tan(getFundamentalDomain().getAngle())*2) : 0);
        
        double[] xpoints = new double[4];
        double[] ypoints = new double[4];
        
        xpoints[0] = - getFundamentalDomain().getHorizontalSide()/2 - horizontalOffset;
        xpoints[1] =   getFundamentalDomain().getHorizontalSide()/2 - horizontalOffset;
        xpoints[2] =   getFundamentalDomain().getHorizontalSide()/2 + horizontalOffset;
        xpoints[3] = - getFundamentalDomain().getHorizontalSide()/2 + horizontalOffset;
        
        ypoints[0] = - getFundamentalDomain().getDomainHeight()/2;
        ypoints[1] = - getFundamentalDomain().getDomainHeight()/2;
        ypoints[2] =   getFundamentalDomain().getDomainHeight()/2;
        ypoints[3] =   getFundamentalDomain().getDomainHeight()/2;
        
        GeneralPath c = new GeneralPath();
        c.moveTo((float)xpoints[0], (float)ypoints[0]);
        c.lineTo((float)xpoints[1], (float)ypoints[1]);
        c.lineTo((float)xpoints[2], (float)ypoints[2]);
        c.lineTo((float)xpoints[3], (float)ypoints[3]);
        c.closePath();
        
        clip = c;
    }
}

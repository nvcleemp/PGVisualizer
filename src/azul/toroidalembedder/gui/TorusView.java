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
import azul.toroidalembedder.graph.DefaultGraph;
import azul.toroidalembedder.graph.Face;
import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.FundamentalDomainListener;
import azul.toroidalembedder.graph.GraphListener;

import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.graph.general.Vertex;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author nvcleemp
 */
public class TorusView extends JPanel implements GraphListener, FundamentalDomainListener, GraphModelListener, GraphSelectionListener, GraphFaceSelectionListener {
    
    private static final Color defaultVertexEdge = Color.BLACK;
    private static final Color defaultVertexFace = Color.WHITE;
    private static final Color defaultSelectedVertexEdge = Color.GREEN;

    private Graph graph;
    private Shape clip;
    private double widthView;
    private double heightView;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private FundamentalDomain fundamentalDomain;
    private GraphModel graphListModel = null;
    private Color vertexEdge = defaultVertexEdge;
    private Color vertexFace = defaultVertexFace;
    private Color selectedVertexEdge = defaultSelectedVertexEdge;
    private GraphSelectionModel selectionModel = new DefaultGraphSelectionModel();
    private GraphFaceSelectionModel faceSelectionModel = new DefaultGraphFaceSelectionModel();
    private boolean paintFaces = false;
    private boolean paintSelectedFace = true;
    private Map<Integer, Color> faceColorMap = new HashMap<Integer, Color>();
    {
        faceColorMap.put(4, Color.WHITE);
        faceColorMap.put(5, Color.GRAY);
        faceColorMap.put(6, Color.YELLOW);
        faceColorMap.put(7, new Color(0.6f, 0.7f, 1.0f));
        faceColorMap.put(8, new Color(0f, 0.4f, 0f));
        faceColorMap.put(9, new Color(0.4f, 0f, 0f));
        faceColorMap.put(10, Color.PINK);
    }
    
    /** Creates a new instance of TorusView */
    public TorusView() {
        this(-1, -1, 1, 1);
    }
    
    public TorusView(int minX, int minY, int maxX, int maxY){
        setView(minX, minY, maxX, maxY);
        selectionModel.addGraphSelectionListener(this);
        faceSelectionModel.addGraphFaceSelectionListener(this);
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
        selectionModel.addGraphSelectionListener(this);
        faceSelectionModel.addGraphFaceSelectionListener(this);
    }
    
    public TorusView(GraphModel model, int minX, int minY, int maxX, int maxY) {
        this(model.getSelectedGraph(), minX, minY, maxX, maxY);
        graphListModel = model;
        model.addGraphModelListener(this);
    }
    
    public TorusView(FundamentalDomain fundamentalDomain, Graph graph, int minX, int minY, int maxX, int maxY) {
        this(graph, minX, minY, maxX, maxY);
        setFundamentalDomain(fundamentalDomain);
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
            widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = ((maxY - minY + 1)*getFundamentalDomain().getDomainHeight());
            calculateClip();
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
            calculateClip();
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
            widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = (maxY - minY + 1)*getFundamentalDomain().getDomainHeight();
        }
        double scaleX = width/widthView - 1;
        double scaleY = height/heightView - 1;
        double scale = Math.min(scaleX, scaleY);
        g2.setColor(new Color(200, 200, 255));
        g2.fillRect(0, 0, width, height);
        g2.translate(width/2, height/2);
        g2.transform(AffineTransform.getScaleInstance(scale, scale));
        if(clip!=null && viewClipped)
            g2.setClip(clip);
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
        
        //paint faces
        if(paintFaces && graph instanceof DefaultGraph){
            for(Face f : ((DefaultGraph)graph).getFaces()){
                Shape s = f.getShape(getFundamentalDomain());
                Color c = null;
                if(getFaceHighlight()!=null)
                    c = getFaceHighlight().getColorFor(f);
                if(c==null)
                    c = faceColorMap.get(f.getSize());
                if(c==null)
                    c = Color.DARK_GRAY;
                c = new Color(c.getRed(), c.getGreen(), c.getBlue(), transparency);
                for (int i = minX + minTargetX; i <= maxX + maxTargetX; i++)
                    for (int j = minY + minTargetY; j <= maxY + maxTargetY; j++){
                        Graphics2D gr = (Graphics2D)(g2.create());
                        Point2D origin = getFundamentalDomain().getOrigin(i, j);
                        gr.translate(origin.getX(), origin.getY());
                        gr.setColor(c);
                        gr.fill(s);
                    }
            }
        }
        
        //show selection of faces
        Face[] selectedFaces = faceSelectionModel.getSelectedFaces();
        if(paintSelectedFace && selectedFaces.length>0){
            Graphics2D gr = (Graphics2D)(g2.create());
            gr.setColor(new Color(0.3f, 0.3f, 0.3f, 0.9f));
            for(Face f : selectedFaces){
                Shape s = f.getShape(getFundamentalDomain());
                for (int i = minX + minTargetX; i <= maxX + maxTargetX; i++)
                    for (int j = minY + minTargetY; j <= maxY + maxTargetY; j++){
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
                for (int i = minX + minTargetX; i <= maxX + maxTargetX; i++)
                    for (int j = minY + minTargetY; j <= maxY + maxTargetY; j++){
                        Graphics2D gr = (Graphics2D)(g2.create());
                        Point2D origin = getFundamentalDomain().getOrigin(i, j);
                        gr.translate(origin.getX(), origin.getY());

                        gr.draw(line);
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
            for (int i = minX; i <= maxX; i++)
                for (int j = minY; j <= maxY; j++){
                    Graphics2D gr = (Graphics2D)(g2.create());
                    Point2D origin = getFundamentalDomain().getOrigin(i, j);
                    gr.translate(origin.getX(), origin.getY());
                    paint(vertex, gr, 0, 0, vertexSize*0.001*getFundamentalDomain().getHorizontalSide()/2, outer, inner);
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
            calculateClip();
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
        calculateClip();
        repaint();
    }
    
    public void fundamentalDomainShapeChanged() {
        widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
        heightView = (maxY - minY + 1)*getFundamentalDomain().getDomainHeight();
        calculateClip();
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
        c.moveTo((float)xpoints[0]*(maxX - minX + 1.1f), (float)ypoints[0]*(maxY - minY + 1.1f));
        c.lineTo((float)xpoints[1]*(maxX - minX + 1.1f), (float)ypoints[1]*(maxY - minY + 1.1f));
        c.lineTo((float)xpoints[2]*(maxX - minX + 1.1f), (float)ypoints[2]*(maxY - minY + 1.1f));
        c.lineTo((float)xpoints[3]*(maxX - minX + 1.1f), (float)ypoints[3]*(maxY - minY + 1.1f));
        c.closePath();
        
        clip = c;
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
            widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = (maxY - minY + 1)*getFundamentalDomain().getDomainHeight();
        }
        double scaleX = getWidth()/widthView - 1;
        double scaleY = getHeight()/heightView - 1;
        double scale = Math.min(scaleX, scaleY);
        
        x -= getWidth()/2;
        y -= getHeight()/2;
        
        double x1 = x/scale;
        double y1 = y/scale;
        
        int domainX = minX - 1;
        int domainY = minY - 1;
        for(int i = minX; i <= maxX; i++){
            for (int j = minY; j <= maxY; j++) {
                if(getFundamentalDomain().inDomain(x1, y1, i, j)){
                    domainX = i;
                    domainY = j;
                }
            }
        }
        
        if(domainX == minX - 1 || domainY == minY - 1)
            return null;
        
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
            widthView = (maxX - minX + 1)*getFundamentalDomain().getHorizontalSide() + (getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*(maxY - minY + 1)*getFundamentalDomain().getVerticalSide()*Math.cos(getFundamentalDomain().getAngle());
            heightView = (maxY - minY + 1)*getFundamentalDomain().getDomainHeight();
        }
        double scaleX = getWidth()/widthView - 1;
        double scaleY = getHeight()/heightView - 1;
        double scale = Math.min(scaleX, scaleY);
        
        x -= getWidth()/2;
        y -= getHeight()/2;
        
        double x1 = x/scale;
        double y1 = y/scale;
        
        int domainX = minX - 1;
        int domainY = minY - 1;
        
        List<Face> faces = ((DefaultGraph)graph).getFaces();
        int k = 0;
        
        while(domainX == minX-1 && k<faces.size()) {
            Shape face = faces.get(k).getShape(getFundamentalDomain());
            for(int i = minX; i <= maxX; i++){
                for (int j = minY; j <= maxY; j++) {
                    if(face.contains(x1 - getFundamentalDomain().getOrigin(i, j).getX(), y1 - getFundamentalDomain().getOrigin(i, j).getY())){
                        domainX = i;
                        domainY = j;
                    }
                }
            }
            k++;
        }
        
        if(domainX == minX-1)
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

    public boolean isPaintFaces() {
        return paintFaces;
    }

    public void setPaintFaces(boolean paintFaces) {
        this.paintFaces = paintFaces;
        repaint();
    }

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
    
    private boolean viewClipped = true;

    public boolean isViewClipped() {
        return viewClipped;
    }

    public void setViewClipped(boolean viewClipped) {
        this.viewClipped = viewClipped;
        repaint();
    }
}

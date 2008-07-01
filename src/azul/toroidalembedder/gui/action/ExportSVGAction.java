/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui.action;

import azul.preferences.PGPreferences;
import azul.toroidalembedder.graph.DefaultGraph;
import azul.toroidalembedder.graph.Face;
import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.graph.general.Vertex;
import azul.toroidalembedder.gui.FaceColorMapping;
import azul.toroidalembedder.gui.GraphGUIData;
import azul.toroidalembedder.gui.GraphListModel;
import azul.toroidalembedder.gui.TorusView;
import java.awt.Color;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author nvcleemp
 */
public class ExportSVGAction extends AbstractAction{
    
    private GraphListModel graphListModel;
    private JFileChooser chooser;


    public ExportSVGAction(GraphListModel graphListModel) {
        super("Export SVG");
        this.graphListModel = graphListModel;
        String dir = PGPreferences.getInstance().getStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY);
        if(dir==null)
            chooser = new JFileChooser();
        else
            chooser = new JFileChooser(new File(dir));
    }

    public void actionPerformed(ActionEvent e) {
        exportSVG(3, 3, true, true, 0);
    }
    
    private void exportSVG(int horizontalTiles, int verticalTiles, boolean showVertices, boolean showFaces, int faceTransparency){
        Graph graph = graphListModel.getSelectedGraph();
        GraphGUIData guiData = graphListModel.getSelectedGraphGUIData();
        double widthView = horizontalTiles*graph.getFundamentalDomain().getHorizontalSide() + (graph.getFundamentalDomain().getAngle()<=Math.PI/2 ? 1 : -1)*verticalTiles*graph.getFundamentalDomain().getVerticalSide()*Math.cos(graph.getFundamentalDomain().getAngle());
        double heightView = verticalTiles*graph.getFundamentalDomain().getDomainHeight();
        
        Element root = new Element("svg", "http://www.w3.org/2000/svg");
        root.setAttribute("width", ""+widthView);
        root.setAttribute("height", ""+heightView);
        root.setAttribute("version", "1.1");
        
        Element transformedGroup = new Element("g");
        transformedGroup.setAttribute("transform", "translate(" + widthView/2 + "," + heightView/2 + ")");
        root.addContent(transformedGroup);
        Element tile = new Element("g");
        //TODO: grid

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
        
        //create faces
        if(showFaces && graph instanceof DefaultGraph){
            for(Face f : ((DefaultGraph)graph).getFaces()){
                Shape s = f.getShape(graph.getFundamentalDomain());
                Color c = null;
                if(guiData.getFaceHighlighter()!=null)
                    c = guiData.getFaceHighlighter().getColorFor(f);
                if(c==null)
                    c = FaceColorMapping.getColorFor(f.getSize());
                if(c==null)
                    c = Color.DARK_GRAY;
                //create face
                Element face = new Element("polygon");
                face.setAttribute("fill",colorToString(c));
                face.setAttribute("stroke", "black");
                face.setAttribute("stroke-width", "0.001");
                StringBuffer points = new StringBuffer();
                PathIterator it = s.getPathIterator(null);
                while(!it.isDone()){
                    double[] coords = new double[2];
                    if(it.currentSegment(coords)!=PathIterator.SEG_CLOSE){
                        points.append(coords[0]);
                        points.append(",");
                        points.append(coords[1]);
                        points.append(" ");
                    }
                    it.next();
                }
                face.setAttribute("points", points.toString().trim());
                tile.addContent(face);
            }
        }
/*
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
        }  */
        for (int i = -(horizontalTiles/2) + minTargetX; i <= (horizontalTiles/2) + maxTargetX; i++)
            for (int j = -(verticalTiles/2) + minTargetY; j <= (verticalTiles/2) + maxTargetY; j++){
                Element newTile = (Element)tile.clone();
                Point2D origin = graph.getFundamentalDomain().getOrigin(i, j);
                newTile.setAttribute("transform", "translate(" + origin.getX() + "," +  origin.getY() + ")");
                transformedGroup.addContent(newTile);
            }
        Document doc = new Document(root, new DocType("svg", "-//W3C//DTD SVG 1.1//EN", "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd"));

        try {
            if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                PGPreferences.getInstance().setStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY, file.getParent());
                if(!file.exists() || JOptionPane.showConfirmDialog(null, "Overwrite file " + file.toString(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    Writer w = new FileWriter(file);
                    w.write(new XMLOutputter(Format.getPrettyFormat()).outputString(doc).replaceAll(" xmlns=\"\"", ""));
                    //TODO: find better way to solve xmlns-problem!!!
                    w.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TorusView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String colorToString(Color c){
        StringBuffer buf = new StringBuffer("rgb(");
        buf.append(c.getRed());
        buf.append(",");
        buf.append(c.getGreen());
        buf.append(",");
        buf.append(c.getBlue());
        buf.append(")");
        return buf.toString();
    }

}

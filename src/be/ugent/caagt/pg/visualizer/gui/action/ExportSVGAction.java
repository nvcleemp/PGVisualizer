/* ExportSVGAction.java
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

package be.ugent.caagt.pg.visualizer.gui.action;

import be.ugent.caagt.pg.preferences.PGPreferences;
import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;
import be.ugent.caagt.pg.visualizer.gui.FaceColorMapping;
import be.ugent.caagt.pg.visualizer.gui.GraphGUIData;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import be.ugent.caagt.pg.visualizer.gui.TorusView;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
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

        //paint edges
        for (Vertex vertex : graph.getVertices()) {
            double x1 = vertex.getX(0, 0, graph.getFundamentalDomain());
            double y1 = vertex.getY(0, 0, graph.getFundamentalDomain());
            for (Edge e : vertex.getEdges()) {
                Element line = new Element("line");
                line.setAttribute("stroke", "black");
                line.setAttribute("stroke-width", "0.001");
                line.setAttribute("x1", Double.toString(x1));
                line.setAttribute("x2", Double.toString(e.getEnd().getX(e.getTargetX(), e.getTargetY(), graph.getFundamentalDomain())));
                line.setAttribute("y1", Double.toString(y1));
                line.setAttribute("y2", Double.toString(e.getEnd().getY(e.getTargetX(), e.getTargetY(), graph.getFundamentalDomain())));
                tile.addContent(line);
            }
        }
/*        
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

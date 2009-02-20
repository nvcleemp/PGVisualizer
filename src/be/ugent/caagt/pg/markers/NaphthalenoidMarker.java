/* NaphthalenoidMarker.java
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

package be.ugent.caagt.pg.markers;

import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.visualizer.gui.DefaultFaceHightlighter;
import be.ugent.caagt.pg.visualizer.gui.DefaultGraphListModel;
import be.ugent.caagt.pg.visualizer.gui.FaceHighlighter;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import be.ugent.caagt.pg.visualizer.gui.SaveDialog;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author nvcleemp
 */
public class NaphthalenoidMarker {
    private GraphListModel model;
    private static final Color NAPHTHALENE_GREEN = new Color(0, 155, 155);
    
    public NaphthalenoidMarker(File file) {
        model = new DefaultGraphListModel(file);
    }

    private void searchForNaphthalenoids(){
        int count = 0;
        for (int i = 0; i < model.getSize(); i++) {
            model.getSelectionModel().setSelectionInterval(i, i);
            DefaultGraph graph = (DefaultGraph)model.getGraph(i);
            if(model.getSelectedGraphGUIData().getFaceHighlighter()!=null)
                System.out.println("Already marked");
            else {
                FaceHighlighter highlighter = new DefaultFaceHightlighter();
                //prepare face info
                Face[][] faces = new Face[graph.getVertices().size()][3];
                for (Face face : graph.getFaces()) {
                    for (int j = 0; j < face.getSize(); j++) {
                        int index = face.getVertexAt(j).getIndex();
                        int pos = 0;
                        while(pos < 3 && faces[index][pos]!=null)
                            pos++;
                        if(pos==3)
                            System.out.println("ERROR"); //TODO: catch!!!!
                        else
                            faces[index][pos]=face;
                    }
                }

                //mark implied faces
                for (int j = 0; j < faces.length; j++) {
                    int nrSixRings = 0;
                    for (int k = 0; k < faces[j].length; k++) {
                        if(faces[j][k].getSize()==6)
                            nrSixRings++;
                    }
                    if(nrSixRings == 1){
                        int k = 0;
                        while(k<faces[j].length && (faces[j][k].getSize()!=6))
                            k++;
                        if(k!=faces[j].length)
                            highlighter.setColor(faces[j][k], NAPHTHALENE_GREEN);
                    }

                }
                model.getSelectedGraphGUIData().setFaceHighlighter(highlighter);
            }
        }
    }
    
    private void save(){
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(null) == JFileChooser.CANCEL_OPTION || chooser.getSelectedFile().exists()) {
                System.exit(0);
            } else {
                try {
                    new SaveDialog(null, chooser.getSelectedFile(), model).save();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SaveDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SaveDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
    }
    
    public static void main(String[] args) {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile().exists()) {
                NaphthalenoidMarker marker = new NaphthalenoidMarker(chooser.getSelectedFile());
                marker.searchForNaphthalenoids();
                marker.save();
            }
            //System.exit(0);
    }

}

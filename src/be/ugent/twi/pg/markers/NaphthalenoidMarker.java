/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.markers;

import be.ugent.twi.pg.graph.DefaultGraph;
import be.ugent.twi.pg.graph.Face;
import be.ugent.twi.pg.visualizer.gui.DefaultFaceHightlighter;
import be.ugent.twi.pg.visualizer.gui.DefaultGraphListModel;
import be.ugent.twi.pg.visualizer.gui.FaceHighlighter;
import be.ugent.twi.pg.visualizer.gui.GraphListModel;
import be.ugent.twi.pg.visualizer.gui.SaveDialog;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.gui;

import be.ugent.twi.pg.graph.DefaultGraph;
import be.ugent.twi.pg.graph.Face;
import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author nvcleemp
 */
public class PartitionChecker {
    private GraphListModel model;
    private static final Color AZULENE_BLUE = new Color(0, 100, 200);
    
    public PartitionChecker(File file) {
        model = new DefaultGraphListModel(file);
    }

    private void checkPartitions(){
        int incorrect = 0;
        int incomplete = 0;
        for (int i = 0; i < model.getSize(); i++) {
            model.getSelectionModel().setSelectionInterval(i, i);
            DefaultGraph graph = (DefaultGraph)model.getGraph(i);
            if(model.getSelectedGraphGUIData().getFaceHighlighter()!=null){
                DefaultFaceHightlighter highlighter = (DefaultFaceHightlighter) model.getSelectedGraphGUIData().getFaceHighlighter();
                int[] included = new int[graph.getVertices().size()];
                for (int j = 0; j < included.length; j++) {
                    included[j]=0;
                }

                for (Face face : highlighter.getFaces()) {
                    int j = 0;
                    while(j<face.getSize()){
                        int vertex = face.getVertexAt(j).getIndex();
                        included[vertex]++;
                        j++;
                    }
                }
                int doubles = 0;
                for (int j : included) {
                    if(j==2)
                        doubles++;
                }

                if(doubles > graph.getVertices().size()/5){
                    System.out.println("Incorrect partition!!! " + (i+1));
                    incorrect++;
                }else{
                    int j = 0;
                    while(j<graph.getVertices().size() && included[j]!=0)
                        j++;
                    if(j!=graph.getVertices().size()){
                        System.out.println("Incomplete partition!!! " + (i+1));
                        incomplete++;
                    }
                }
            } else {
                System.out.println("Not yet marked! " + (i+1));
            }
        }
        System.out.println("Incorrect: " + incorrect);
        System.out.println("Incomplete: " + incomplete);
    }
    
    public static void main(String[] args) {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile().exists()) {
                PartitionChecker marker = new PartitionChecker(chooser.getSelectedFile());
                marker.checkPartitions();
            }
            //System.exit(0);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.molecule;

import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.graph.Graph;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class Tiled3DStructure {

    protected int size;
    protected int edgeSize;
    protected int selectedFaceCount;
    protected int[][] adjacencyList;
    protected int[][] edges;
    protected float[][] coordinate;
    protected int[][] selectedFaces;
    protected Color[] colors;

    public enum Embedding{
        PLANAR{
            public String getDescription(){
                return "Planar";
            }
        },
        CYLINDER_H{
            public String getDescription(){
                return "Cylinder along X axis";
            }
        },
        CYLINDER_V{
            public String getDescription(){
                return "Cylinder along Y axis";
            }
        },
        TORUS_X{
            public String getDescription(){
                return "Torus: X axis as major circle";
            }
        },
        TORUS_Y{
            public String getDescription(){
                return "Torus: Y axis as major circle";
            }
        };
        
        public abstract String getDescription();
    }

    public Tiled3DStructure(Graph graph, List<Face> resultFaces, Map<Face, Color> colors, Embedding embedding) {
        size = graph.getVertices().size();
        adjacencyList = new int[size][];
        coordinate = new float[size][3];
        if(Embedding.PLANAR.equals(embedding))
            doPlanarEmbedding(graph);
        else if(Embedding.CYLINDER_H.equals(embedding))
            doCylinderHEmbedding(graph);
        else if(Embedding.CYLINDER_V.equals(embedding))
            doCylinderVEmbedding(graph);
        else if(Embedding.TORUS_X.equals(embedding))
            doTorusXEmbedding(graph);
        else if(Embedding.TORUS_Y.equals(embedding))
            doTorusYEmbedding(graph);
        edgeSize = 0;
        for (int[] is : adjacencyList)
            edgeSize += is.length;
        edgeSize /= 2;
        doEdges();
        if(resultFaces!=null)
            doFaces(resultFaces, colors);
    }
    
    protected void doFaces(List<Face> faces, Map<Face, Color> colors){
        selectedFaceCount = faces.size();
        selectedFaces = new int[selectedFaceCount][];
        this.colors = new Color[selectedFaceCount];
        for (int i = 0; i < faces.size(); i++) {
            selectedFaces[i] = new int[faces.get(i).getSize()];
            if(colors!=null)
                this.colors[i] = colors.get(faces.get(i));
            for (int j = 0; j < selectedFaces[i].length; j++) {
                selectedFaces[i][j]=faces.get(i).getVertexAt(j).getIndex();
            }
        }
    }
    
    protected void doEdges(){
        edges = new int[edgeSize][2];
        int i = 0;
        for (int j = 0; j < adjacencyList.length; j++) {
            for (int k = 0; k < adjacencyList[j].length; k++) {
                if(adjacencyList[j][k]>j){
                    edges[i][0] = j;
                    edges[i][1] = adjacencyList[j][k];
                    i++;
                }
            }
        }
    }
    
    protected void doPlanarEmbedding(Graph graph){
        for (int k = 0; k < graph.getVertices().size(); k++) {
            coordinate[k][0] = (float)graph.getVertex(k).getX(graph.getFundamentalDomain());
            coordinate[k][1] = (float)graph.getVertex(k).getY(graph.getFundamentalDomain());
            coordinate[k][2] = 0f;
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            for (Edge edge : graph.getVertex(k).getEdges()) {
                if(edge.getTargetX() == 0 && edge.getTargetY() == 0){
                    arrayList.add(edge.getEnd().getIndex());
                }
            }
            adjacencyList[k] = new int[arrayList.size()];
            for (int l = 0; l < arrayList.size(); l++) {
                adjacencyList[k][l] = arrayList.get(l);

            }
       }

    }
    
    protected void doCylinderHEmbedding(Graph graph){
        double circleLength = graph.getFundamentalDomain().getDomainHeight();
        for (int k = 0; k < graph.getVertices().size(); k++) {
            double angle = graph.getVertex(k).getY(graph.getFundamentalDomain())/circleLength * 2 * Math.PI;
            coordinate[k][0] = (float)graph.getVertex(k).getX(graph.getFundamentalDomain());
            coordinate[k][1] = (float)Math.cos(angle);
            coordinate[k][2] = (float)Math.sin(angle);
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            for (Edge edge : graph.getVertex(k).getEdges()) {
                if(edge.getTargetX() == 0){
                    arrayList.add(edge.getEnd().getIndex());
                }
            }
            adjacencyList[k] = new int[arrayList.size()];
            for (int l = 0; l < arrayList.size(); l++) {
                adjacencyList[k][l] = arrayList.get(l);

            }
       }

    }
    
    protected void doCylinderVEmbedding(Graph graph){
        double circleLength = graph.getFundamentalDomain().getHorizontalSide();
        for (int k = 0; k < graph.getVertices().size(); k++) {
            double angle = graph.getVertex(k).getX(graph.getFundamentalDomain())/circleLength * 2 * Math.PI;
            coordinate[k][0] = (float)Math.cos(angle);
            coordinate[k][1] = (float)graph.getVertex(k).getY(graph.getFundamentalDomain());
            coordinate[k][2] = (float)Math.sin(angle);
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            for (Edge edge : graph.getVertex(k).getEdges()) {
                if(edge.getTargetY() == 0){
                    arrayList.add(edge.getEnd().getIndex());
                }
            }
            adjacencyList[k] = new int[arrayList.size()];
            for (int l = 0; l < arrayList.size(); l++) {
                adjacencyList[k][l] = arrayList.get(l);

            }
       }

    }
    
    protected void doTorusXEmbedding(Graph graph){
        double circleLengthX = graph.getFundamentalDomain().getHorizontalSide(); //major circle
        double circleLengthY = graph.getFundamentalDomain().getDomainHeight(); //minor circle
        for (int k = 0; k < graph.getVertices().size(); k++) {
            double angle1 = graph.getVertex(k).getX(graph.getFundamentalDomain())/circleLengthX * 2 * Math.PI; //major radius angle
            double angle2 = graph.getVertex(k).getY(graph.getFundamentalDomain())/circleLengthY * 2 * Math.PI; //minor radius angle
            double radius1 = 5; //major radius
            double radius2 = 1; //minor radius
            coordinate[k][0] = (float)((radius1 - radius2*Math.cos(angle2))*Math.cos(angle1));
            coordinate[k][1] = (float)((radius1 - radius2*Math.cos(angle2))*Math.sin(angle1));
            coordinate[k][2] = (float)(radius2*Math.sin(angle2));
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            for (Edge edge : graph.getVertex(k).getEdges()) {
                arrayList.add(edge.getEnd().getIndex());
            }
            adjacencyList[k] = new int[arrayList.size()];
            for (int l = 0; l < arrayList.size(); l++) {
                adjacencyList[k][l] = arrayList.get(l);

            }
       }

    }
    
    protected void doTorusYEmbedding(Graph graph){
        double circleLengthX = graph.getFundamentalDomain().getHorizontalSide(); //major circle
        double circleLengthY = graph.getFundamentalDomain().getDomainHeight(); //minor circle
        for (int k = 0; k < graph.getVertices().size(); k++) {
            double angle1 = graph.getVertex(k).getX(graph.getFundamentalDomain())/circleLengthX * 2 * Math.PI; //major radius angle
            double angle2 = graph.getVertex(k).getY(graph.getFundamentalDomain())/circleLengthY * 2 * Math.PI; //minor radius angle
            double radius1 = 1; //major radius
            double radius2 = 5; //minor radius
            coordinate[k][0] = (float)((radius2 - radius1*Math.cos(angle1))*Math.cos(angle2));
            coordinate[k][1] = (float)((radius2 - radius1*Math.cos(angle1))*Math.sin(angle2));
            coordinate[k][2] = (float)(radius1*Math.sin(angle1));
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            for (Edge edge : graph.getVertex(k).getEdges()) {
                arrayList.add(edge.getEnd().getIndex());
            }
            adjacencyList[k] = new int[arrayList.size()];
            for (int l = 0; l < arrayList.size(); l++) {
                adjacencyList[k][l] = arrayList.get(l);

            }
       }
    }
    
    public int edgeFrom(int edgeIndex) {
        return edges[edgeIndex][0];
    }

    public int edgeTo(int edgeIndex) {
        return edges[edgeIndex][1];
    }

    public Color getColorOfFace(int index) {
        return colors[index];
    }

    public int getEdgeSize() {
        return edgeSize;
    }

    public int[] getFaceAt(int index) {
        return selectedFaces[index];
    }

    public int getFaceSize() {
        return selectedFaceCount;
    }

    public int getSize() {
        return size;
    }

    public float getX(int index) {
        return coordinate[index][0];
    }

    public float getY(int index) {
        return coordinate[index][1];
    }

    public float getZ(int index) {
        return coordinate[index][2];
    }

}

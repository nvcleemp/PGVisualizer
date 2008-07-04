/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.molecule;

import be.ugent.twi.pg.graph.Face;
import be.ugent.twi.pg.graph.Edge;
import be.ugent.twi.pg.graph.Graph;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class Molecule {
    
    private int size;
    private int edgeSize;
    private int selectedFaceCount;
    private int[][] adjacencyList;
    private int[][] edges;
    private float[][] coordinate;
    private String[] atomType;
    private int[][] selectedFaces;
    private Color[] colors;

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
    
    public Molecule(Graph graph){
        this(graph, null, null, Embedding.PLANAR);             
    }
    
    public Molecule(Graph graph, Embedding embedding) {
        this(graph, null, null, embedding);
    }
    
    public Molecule(Graph graph, List<Face> resultFaces, Embedding embedding) {
        this(graph, null, null, embedding);
    }
    
    public Molecule(Graph graph, List<Face> resultFaces, Map<Face, Color> colors, Embedding embedding) {
        size = graph.getVertices().size();
        adjacencyList = new int[size][];
        atomType = new String[size];
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
    
    private void doFaces(List<Face> faces, Map<Face, Color> colors){
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
    
    private void doEdges(){
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
    
    private void doPlanarEmbedding(Graph graph){
        for (int k = 0; k < graph.getVertices().size(); k++) {
            atomType[k] = "C";
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
    
    private void doCylinderHEmbedding(Graph graph){
        double circleLength = graph.getFundamentalDomain().getDomainHeight();
        for (int k = 0; k < graph.getVertices().size(); k++) {
            double angle = graph.getVertex(k).getY(graph.getFundamentalDomain())/circleLength * 2 * Math.PI;
            atomType[k] = "C";
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
    
    private void doCylinderVEmbedding(Graph graph){
        double circleLength = graph.getFundamentalDomain().getHorizontalSide();
        for (int k = 0; k < graph.getVertices().size(); k++) {
            double angle = graph.getVertex(k).getX(graph.getFundamentalDomain())/circleLength * 2 * Math.PI;
            atomType[k] = "C";
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
    
    private void doTorusXEmbedding(Graph graph){
        double circleLengthX = graph.getFundamentalDomain().getHorizontalSide(); //major circle
        double circleLengthY = graph.getFundamentalDomain().getDomainHeight(); //minor circle
        for (int k = 0; k < graph.getVertices().size(); k++) {
            double angle1 = graph.getVertex(k).getX(graph.getFundamentalDomain())/circleLengthX * 2 * Math.PI; //major radius angle
            double angle2 = graph.getVertex(k).getY(graph.getFundamentalDomain())/circleLengthY * 2 * Math.PI; //minor radius angle
            double radius1 = 5; //major radius
            double radius2 = 1; //minor radius
            atomType[k] = "C";
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
    
    private void doTorusYEmbedding(Graph graph){
        double circleLengthX = graph.getFundamentalDomain().getHorizontalSide(); //major circle
        double circleLengthY = graph.getFundamentalDomain().getDomainHeight(); //minor circle
        for (int k = 0; k < graph.getVertices().size(); k++) {
            double angle1 = graph.getVertex(k).getX(graph.getFundamentalDomain())/circleLengthX * 2 * Math.PI; //major radius angle
            double angle2 = graph.getVertex(k).getY(graph.getFundamentalDomain())/circleLengthY * 2 * Math.PI; //minor radius angle
            double radius1 = 1; //major radius
            double radius2 = 5; //minor radius
            atomType[k] = "C";
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
    
    public String writeCML(){
        StringBuffer buffer = new StringBuffer();
        String sep;
        int i, k, n = size;
        buffer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        buffer.append("<!DOCTYPE molecule SYSTEM \"cml.dtd\" []>\n");
        buffer.append("<molecule convention=\"MathGraph\">\n");
        buffer.append("  <atomArray>\n");
        buffer.append("    <stringArray builtin=\"id\">");
        sep = "";
        for (i = 0; i < n; ++i)
        {
          buffer.append(sep + "a" + i);
          sep = " ";
        }
        buffer.append("</stringArray>\n");
        buffer.append("    <stringArray builtin=\"elementType\">");
        sep = "";
        for (i = 0; i < n; ++i)
        {
          buffer.append(sep + atomType[i]);
          sep = " ";
        }
        buffer.append("</stringArray>\n");
        for (k = 0; k < 3; ++k)
        {
          buffer.append("    <floatArray builtin=\"" + "xyz".substring(k, k+1) + 3 + "\">");
          sep = "";
          for (i = 0; i < n; ++i)
          {
            buffer.append(sep + (coordinate[i][k]*10));
            sep = " ";
          }
          buffer.append("</floatArray>\n");
        }
        buffer.append("  </atomArray>\n");
        buffer.append("  <bondArray>\n");
        StringBuffer from = new StringBuffer();
        StringBuffer to = new StringBuffer();
        sep = "";
        for (i = 0; i < n; ++i)
        {
            for (int j = 0; j < adjacencyList[i].length; j++)
            {
                //if(i < adjacencyList[i][j]){
                    from.append(sep + "a" + i);
                    to.append(sep + "a" + adjacencyList[i][j]);
                    sep = " ";
                //}
            }
        }
        buffer.append("    <stringArray builtin=\"atomRef\">" + from + "</stringArray>\n");
        buffer.append("    <stringArray builtin=\"atomRef\">" + to + "</stringArray>\n");
        buffer.append("  </bondArray>\n");
        buffer.append("</molecule>\n");
        return buffer.toString();
    }

    public int getSize() {
        return size;
    }
    
    public int getEdgeSize() {
        return edgeSize;
    }
    
    public int getFaceSize() {
        return selectedFaceCount;
    }
    
    public float getX(int index){
        return coordinate[index][0];
    }

    public float getY(int index){
        return coordinate[index][1];
    }
    
    public float getZ(int index){
        return coordinate[index][2];
    }
    
    public int edgeFrom(int edgeIndex){
        return edges[edgeIndex][0];
    }
    
    public int edgeTo(int edgeIndex){
        return edges[edgeIndex][1];
    }
    
    public int[] getFaceAt(int index){
        return selectedFaces[index];
    }
    
    public Color getColorOfFace(int index){
        return colors[index];
    }
}

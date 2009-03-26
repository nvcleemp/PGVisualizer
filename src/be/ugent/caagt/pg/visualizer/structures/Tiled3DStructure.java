/* Tiled3DStructure.java
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

package be.ugent.caagt.pg.visualizer.structures;

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
    protected int faceCount;
    protected int[][] adjacencyList;
    protected int[][] edges;
    protected float[][] coordinate;
    protected int[][] faces;
    protected Color[] colors;
    protected int maxDegree = 0;

    public enum Embedding{
        PLANAR{
            public String getDescription(){
                return "Planar";
            }
            
            public boolean isOverflowEdge(Edge edge){
                return edge.getTargetX()!=0 || edge.getTargetY()!=0;
            }
        },
        CYLINDER_H{
            public String getDescription(){
                return "Cylinder along X axis";
            }
            
            public boolean isOverflowEdge(Edge edge){
                return edge.getTargetX()!=0;
            }
        },
        CYLINDER_V{
            public String getDescription(){
                return "Cylinder along Y axis";
            }
            
            public boolean isOverflowEdge(Edge edge){
                return edge.getTargetY()!=0;
            }
        },
        TORUS_X{
            public String getDescription(){
                return "Torus: X axis as major circle";
            }
            
            public boolean isOverflowEdge(Edge edge){
                return false;
            }
        },
        TORUS_Y{
            public String getDescription(){
                return "Torus: Y axis as major circle";
            }
            
            public boolean isOverflowEdge(Edge edge){
                return false;
            }
        };
        
        public abstract String getDescription();
        public abstract boolean isOverflowEdge(Edge edge);
    }

    public Tiled3DStructure(Graph graph, List<Face> resultFaces, Map<Face, Color> colors, Embedding embedding, boolean allowOverflowFaces) {
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
            doFaces(resultFaces, colors, allowOverflowFaces, embedding);
    }
    
    protected void doFaces(List<Face> faces, Map<Face, Color> colors, boolean allowOverflowFaces, Embedding embedding){
        List<int[]> targetFaces = new ArrayList<int[]>();
        List<Color> targetColors = new ArrayList<Color>();
        for (int i = 0; i < faces.size(); i++) {
            boolean overflowFace = false;
            if(!allowOverflowFaces)
                for (Edge edge : faces.get(i).getEdges()) {
                    overflowFace = overflowFace || embedding.isOverflowEdge(edge);
                }
            if(allowOverflowFaces || !overflowFace){
                int[] currentFace = new int[faces.get(i).getSize()];
                if(colors!=null)
                    targetColors.add(colors.get(faces.get(i)));
                for (int j = 0; j < currentFace.length; j++) {
                    currentFace[j]=faces.get(i).getVertexAt(j).getIndex();
                }
                targetFaces.add(currentFace);
            }
        }
        faceCount = targetFaces.size();
        this.faces = new int[faceCount][];
        this.colors = new Color[faceCount];
        for (int i = 0; i < faceCount; i++) {
            this.faces[i] = targetFaces.get(i);
            this.colors[i] = targetColors.get(i);
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
            if(arrayList.size()>maxDegree)
                maxDegree = arrayList.size();
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
            if(arrayList.size()>maxDegree)
                maxDegree = arrayList.size();
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
            if(arrayList.size()>maxDegree)
                maxDegree = arrayList.size();
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
            if(arrayList.size()>maxDegree)
                maxDegree = arrayList.size();
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
            if(arrayList.size()>maxDegree)
                maxDegree = arrayList.size();
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
        int[] face = new int[faces[index].length];
        System.arraycopy(faces[index], 0, face, 0, face.length);
        return face;
    }

    public int getFaceSize() {
        return faceCount;
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
    
    public int getDegree(int index){
        return adjacencyList[index].length;
    }

    public int getMaximumDegree(){
        return maxDegree;
    }
    
    public int adjacentTo(int vertex, int number){
        return adjacencyList[vertex][number];
    }
}

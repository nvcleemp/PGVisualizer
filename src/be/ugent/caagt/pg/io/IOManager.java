/* IOManager.java
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

package be.ugent.caagt.pg.io;

import be.ugent.caagt.pg.delaney.BasicDelaney;
import be.ugent.caagt.pg.delaney.Chamber;
import be.ugent.caagt.pg.delaney.DelaneySymbol;
import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.DefaultVertex;

import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;
import be.ugent.caagt.pg.graph.triangulation.TriangulatedGraph;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nvcleemp
 */
public class IOManager {
    
    /** Creates a new instance of IOManager */
    public IOManager() {
    }
    
    
    public static List<DefaultGraph> readPG(File file){
        try {
            Scanner fileScanner = new Scanner(file);
            List<DefaultGraph> list = new ArrayList<DefaultGraph>();
            while(fileScanner.hasNextLine()){
                String line = fileScanner.nextLine().trim();
                if(!line.startsWith("#") && line.length()!=0) //ignore comments
                    list.add(readPG(line));
            }
            return list;
        } catch (FileFormatException ex) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static DefaultGraph readPG(String pg) throws FileFormatException {
        String[] parts;
        //split off comments
        if(pg.indexOf('#')==-1)
            parts = pg.split("\\|");
        else
            parts = pg.substring(0, pg.indexOf('#')).split("\\|");
        
        if(parts.length!=4 && parts.length!=5)
            throw new FileFormatException("Illegal number of parts in file: expected 4, found " + parts.length);
        int order;
        try {
            order = Integer.parseInt(parts[0]);
        } catch (NumberFormatException ex) {
            throw new FileFormatException("Illegal order of graph: expected number, found " + parts[0], ex);
        }
        
        String[] domainParameters = parts[1].split("\\s");
        if(domainParameters.length<2 || domainParameters.length>3)
            throw new FileFormatException("Illegal number of parts for fundamental domain: expected 2 or 3, found " + domainParameters.length);
        
        FundamentalDomain domain;
        
        try {
            double hs = Double.parseDouble(domainParameters[0]);
            double vs = Double.parseDouble(domainParameters[1]);
            double angle = domainParameters.length == 3 ? Double.parseDouble(domainParameters[2]) : Math.PI / 2;
            domain = new FundamentalDomain(angle, hs, vs);
        } catch (NumberFormatException ex) {
            throw new FileFormatException("Illegal value while reading fundamental domain: expected number.", ex);
        }
               
        DefaultGraph graph = new DefaultGraph(domain);
        Scanner coords = new Scanner(parts[2]).useDelimiter("(\\s)|(;)").useLocale(Locale.US);
        try {
            for(int i = 0; i<order; i++)
                graph.addVertex(new DefaultVertex(coords.nextDouble(),coords.nextDouble()));
        } catch (InputMismatchException ex) {
            throw new FileFormatException("Illegal coordinate for vertex: expected number, found '" + coords.next() + "'.", ex);
        } catch (NoSuchElementException ex) {
            throw new FileFormatException("End of section while reading coordinates.", ex);
        }
        
        Scanner edges = new Scanner(parts[3]).useDelimiter("(\\s)|(;)");
        try {
            while (edges.hasNextInt())
                graph.addEdge(edges.nextInt(), edges.nextInt(), edges.nextInt(), edges.nextInt());
        } catch (InputMismatchException ex) {
            throw new FileFormatException("Illegal input while adding edge: expected integer, found " + edges.next(), ex);
        } catch (NoSuchElementException ex) {
            throw new FileFormatException("End of section while reading edges.", ex);
        }
        
        if(parts.length == 4) //no face information: older format
            return graph;
        
        String[] faces = parts[4].split(";");
        for (int i = 0; i<faces.length; i++) {
            String faceString = faces[i];
            Face face = new Face();
            Scanner faceScanner = new Scanner(faceString);
            while(faceScanner.hasNextInt())
                face.add(graph.getVertex(faceScanner.nextInt()));
            graph.addFace(face);
        }
        
        return graph;
    }
    
    public static String writePG(Graph graph){
        List<? extends Vertex> vertices = graph.getVertices();
        StringBuffer buf = new StringBuffer(vertices.size()*8);
        buf.append(vertices.size() + "|");
        FundamentalDomain domain = graph.getFundamentalDomain();
        buf.append(domain.getHorizontalSide());
        buf.append(" ");
        buf.append(domain.getVerticalSide());
        buf.append(" ");
        buf.append(domain.getAngle());
        buf.append("|");
        for (int i = 0; i < vertices.size(); i++) {
            buf.append(vertices.get(i).getRawX());
            buf.append(" ");
            buf.append(vertices.get(i).getRawY());
            buf.append(";");
        }
        buf.deleteCharAt(buf.length()-1);
        buf.append("|");
        for (int i = 0; i < vertices.size(); i++) {
            for (Edge edge : vertices.get(i).getEdges()) {
                int end = vertices.indexOf(edge.getEnd());
                if(end>=i){
                    buf.append(i);
                    buf.append(" ");
                    buf.append(end);
                    buf.append(" ");
                    buf.append(edge.getTargetX());
                    buf.append(" ");
                    buf.append(edge.getTargetY());
                    buf.append(";");
                }
            }
        }
        buf.deleteCharAt(buf.length()-1);
        if(graph instanceof DefaultGraph){
            buf.append("|");
            for (Face f : ((DefaultGraph)graph).getFaces()) {
                for (int i = 0; i<f.getSize(); i++) {
                        buf.append(f.getVertexAt(i).getIndex());
                        buf.append(" ");
                }
                buf.append(";");
            }
            buf.deleteCharAt(buf.length()-1);
        }
        return buf.toString();
    }
    
    public static TriangulatedGraph readTPG(String tpg) throws FileFormatException {
        String[] parts;
        //split off comments
        if(tpg.indexOf('#')==-1)
            parts = tpg.split("\\|");
        else
            parts = tpg.substring(0, tpg.indexOf('#')).split("\\|");
        
        if(parts.length!=3)
            throw new FileFormatException("Illegal number of parts in file: expected 3, found " + parts.length);
        
        String[] orders = parts[0].split(":");
        if(orders.length!=3)
            throw new FileFormatException("Illegal number of order parts in file: expected 3, found " + orders.length);
        
        int vertexOrder;
        try {
            vertexOrder = Integer.parseInt(orders[0]);
        } catch (NumberFormatException ex) {
            throw new FileFormatException("Illegal number of vertices: expected number, found " + orders[0], ex);
        }
        int edgeOrder;
        try {
            edgeOrder = Integer.parseInt(orders[1]);
        } catch (NumberFormatException ex) {
            throw new FileFormatException("Illegal number of edges: expected number, found " + orders[1], ex);
        }
        int faceOrder;
        try {
            faceOrder = Integer.parseInt(orders[2]);
        } catch (NumberFormatException ex) {
            throw new FileFormatException("Illegal number of faces: expected number, found " + orders[2], ex);
        }
                       
        TriangulatedGraph graph = new TriangulatedGraph();
        String[] coords = parts[1].split(":");
        Scanner vertexCoords = new Scanner(coords[0]).useDelimiter("(\\s)|(;)").useLocale(Locale.US);
        try {
            for(int i = 0; i<vertexOrder; i++)
                graph.addVertex(vertexCoords.nextDouble(),vertexCoords.nextDouble());
        } catch (InputMismatchException ex) {
            throw new FileFormatException("Illegal coordinate for vertexnode: expected number, found '" + vertexCoords.next() + "'.", ex);
        } catch (NoSuchElementException ex) {
            throw new FileFormatException("End of section while reading coordinates.", ex);
        }
        Scanner edgeCoords = new Scanner(coords[1]).useDelimiter("(\\s)|(;)").useLocale(Locale.US);
        try {
            for(int i = 0; i<edgeOrder; i++)
                graph.addEdge(edgeCoords.nextDouble(),edgeCoords.nextDouble());
        } catch (InputMismatchException ex) {
            throw new FileFormatException("Illegal coordinate for edgenode: expected number, found '" + edgeCoords.next() + "'.", ex);
        } catch (NoSuchElementException ex) {
            throw new FileFormatException("End of section while reading coordinates.", ex);
        }
        Scanner faceCoords = new Scanner(coords[2]).useDelimiter("(\\s)|(;)").useLocale(Locale.US);
        try {
            for(int i = 0; i<faceOrder; i++)
                graph.addFace(faceCoords.nextDouble(),faceCoords.nextDouble());
        } catch (InputMismatchException ex) {
            throw new FileFormatException("Illegal coordinate for facenode: expected number, found '" + faceCoords.next() + "'.", ex);
        } catch (NoSuchElementException ex) {
            throw new FileFormatException("End of section while reading coordinates.", ex);
        }
        
        Scanner connections = new Scanner(parts[2]).useDelimiter("(\\s)|(;)");
        try {
            while (connections.hasNext())
                graph.addConnection(connections.next(), connections.nextInt(), connections.next(), connections.nextInt(), connections.nextInt(), connections.nextInt());
        } catch (InputMismatchException ex) {
            throw new FileFormatException("Illegal input while adding edge: expected integer or type, found " + connections.next(), ex);
        } catch (NoSuchElementException ex) {
            throw new FileFormatException("End of section while reading edges.", ex);
        }
        
        return graph;
    }
    
    public static String writeDS(DelaneySymbol symbol){
        String part1 = "1.1";
        String part2 = symbol.getSize() + " " + symbol.getDimension();
        String[] temp;
        temp = new String[symbol.getDimension()+1];
        for (int i = 0; i < symbol.getDimension() + 1; i++)
            temp[i] = "";
        BasicDelaney basic = symbol.getBasicDelaney();
        int[][] sigma = basic.getSigma();
        for (int i = 0; i < basic.getDimension() + 1; i++)
            for (int j = 0; j < basic.getSize(); j++)
                if(sigma[j][i]>=j)
                    temp[i] += " " + (sigma[j][i]+1);
        String part3 = concat(temp, ",");
        
        temp = new String[symbol.getDimension()];
        for (int i = 0; i < symbol.getDimension(); i++)
            temp[i] = "";
        for (int i = 0; i < symbol.getDimension(); i++){
            List<Chamber> visited = new ArrayList<Chamber>();
            int[] s = {i, i+1};
            for (int j = 0; j < symbol.getSize(); j++)
                if(!visited.contains(symbol.getChamber(j))){
                    temp[i] += " " + symbol.getChamber(j).m(i);
                    visited.add(symbol.getChamber(j));
                    int k = 1;
                    Chamber next = symbol.getChamber(j).sigma(s[0]);
                    while(k!=0 || next!=symbol.getChamber(j)){
                        visited.add(next);
                        next = next.sigma(s[k]);
                        k=(k+1)%2;
                    }
                }
        }
        String part4 = concat(temp, ",");
        
        return "<" + part1 + ":" + part2 + ":" + part3 + ":" + part4 + ">";
    }
    
    public static DelaneySymbol readDS(String ds) throws FileFormatException{
        if(!ds.startsWith("<") || !ds.endsWith(ds))
            throw new FileFormatException("A .ds file should start with '<' and end with '>'");
        
        ds = ds.split("#")[0];
        
        String[] parts = ds.substring(1, ds.length()-1).split(":");
        if(parts.length!=4)
            throw new FileFormatException("Illegal number of sections in file: expected 4, found " + parts.length);
        
        //for now: ignore first part
        
        //basic structure
        Scanner struct = new Scanner(parts[1]);
        int size = struct.nextInt();
        int dimension = 2;
        if(struct.hasNextInt())
            dimension = struct.nextInt();
        DelaneySymbol symbol = new DelaneySymbol(dimension, size);

        //sigmas
        String[] sigmas = parts[2].split(",");
        if(sigmas.length!=dimension+1)
            throw new FileFormatException("Illegal number of sigma-definitions in file: expected " + (dimension+1) + ", found " + sigmas.length);
        for (int i = 0; i < sigmas.length; i++) {
            Scanner sigma = new Scanner(sigmas[i]);
            for (int j = 0; j < symbol.getSize(); j++) {
                if(symbol.getChamber(j).sigma(i)==null){
                    int s = sigma.nextInt()-1;
                    symbol.getChamber(j).setSigma(i, symbol.getChamber(s));
                    symbol.getChamber(s).setSigma(i, symbol.getChamber(j));
                }
            }
        }
        
        //m_ij's
        String[] ms = parts[3].split(",");
        if(ms.length!=dimension)
            throw new FileFormatException("Illegal number of m-definitions in file: expected " + dimension + ", found " + ms.length + "(" + ds + ")");
        for (int i = 0; i < ms.length; i++) {
            Scanner m = new Scanner(ms[i]);
            int[] s = {i, i+1};
            for (int j = 0; j < symbol.getSize(); j++) {
                if(symbol.getChamber(j).m(i)==0){
                    int value = m.nextInt();
                    int k = 1;
                    symbol.getChamber(j).setM(i, value);
                    Chamber next = symbol.getChamber(j).sigma(s[0]);
                    while(k!=0 || next!=symbol.getChamber(j)){
                        next.setM(i, value);
                        next = next.sigma(s[k]);
                        k=(k+1)%2;
                    }
                }
            }
        }
        
        
        
        return symbol;
    }
    
    public static List<DelaneySymbol> readDS(File file){
        try {
            Scanner fileScanner = new Scanner(file);
            List<DelaneySymbol> list = new ArrayList<DelaneySymbol>();
            while(fileScanner.hasNextLine()){
                String line = fileScanner.nextLine().trim();
                if(!line.startsWith("#") && line.length()!=0) //ignore comments
                    list.add(readDS(line));
            }
            return list;
        } catch (FileFormatException ex) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static String concat(String[] parts, String glue){
        if(parts.length==0)
            return null;
        String result = parts[0].trim();
        for (int i = 1; i < parts.length; i++)
            result += glue + parts[i].trim();
        return result;
    }
}

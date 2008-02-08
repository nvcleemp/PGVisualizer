/*
 * IOManager.java
 *
 * Created on January 15, 2008, 10:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.io;

import azul.delaney.BasicDelaney;
import azul.delaney.Chamber;
import azul.delaney.DelaneySymbol;
import azul.delaney.Utility;
import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
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
    
    public static Graph readTorGraph(String torGraph) throws FileFormatException {
        String[] parts = torGraph.split("\\|");
        
        if(parts.length!=4)
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
               
        Graph graph = new Graph(domain);
        Scanner coords = new Scanner(parts[2]).useDelimiter("(\\s)|(;)");
        try {
            for(int i = 0; i<order; i++)
                graph.addVertex(new Vertex(coords.nextDouble(),coords.nextDouble()));
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
    
    public static void main(String[] args) {
        //List<DelaneySymbol> list = readDS(new File("/Users/nvcleemp/doctoraat/code/azul/azul2.ds"));
        List<DelaneySymbol> list = readDS(new File("/Users/nvcleemp/doctoraat/test.ds"));
        for (DelaneySymbol symbol : list) {
            //symbol.printSymbol();
            System.out.println("OUT: " + writeDS(symbol));
        }
        Graph g = Utility.delaneyToTorGraph(list.get(0));
        //new EmbedderWindow(g).setVisible(true);
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney;

import azul.toroidalembedder.graph.Edge;
import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Vertex;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class Utility {
    
    public static Graph delaneyToTorGraph(DelaneySymbol symbol){
        //TODO: convert symbol to translation only
        
        
        //create graph
        Graph graph = new Graph();

        
        //mark the orbits that correspond with vertices
        Map<Chamber, Vertex> vertices = new HashMap<Chamber, Vertex>();
        for(int i = 0; i<symbol.getSize(); i++){
            Chamber chamber = symbol.getChamber(i);
            if(!vertices.containsKey(chamber)){
                Vertex v = new Vertex(0,0);
                graph.addVertex(v);
                while(!vertices.containsKey(chamber)){
                    vertices.put(chamber, v);
                    vertices.put(chamber.sigma(1), v);
                    chamber = chamber.sigma(1).sigma(2);
                }
            }
        }

        //map for the edges
        Map<Chamber, Edge> edges = new HashMap<Chamber, Edge>();       
        
        
        //determine sides of quadrangle
        Chamber start = symbol.getChamber(0);
        int angleCorner = start.m(0)/2; //number of chambers in corner where angle is defined.
        int oppositeCorner = start.m(0) - angleCorner; //number of chambers in corner where angle is defined.
        
        //arrays for corners
        Chamber[] cornerNW = new Chamber[angleCorner];
        Chamber[] cornerSE = new Chamber[angleCorner];
        Chamber[] cornerNE = new Chamber[oppositeCorner];
        Chamber[] cornerSW = new Chamber[oppositeCorner];
        
        //determine corners
        Chamber next = start;
        int[] sigmas = {0, 1};
        int currentSigma = 0;
        for (int i = 0; i < cornerNW.length; i++) {
            cornerNW[i] = next;
            currentSigma = (currentSigma + 1)%2;
            next = next.sigma(sigmas[currentSigma]);
        }
        for (int i = 0; i < cornerSW.length; i++) {
            cornerSW[i] = next;
            currentSigma = (currentSigma + 1)%2;
            next = next.sigma(sigmas[currentSigma]);
        }
        for (int i = 0; i < cornerSE.length; i++) {
            cornerSE[i] = next;
            currentSigma = (currentSigma + 1)%2;
            next = next.sigma(sigmas[currentSigma]);
        }
        for (int i = 0; i < cornerNE.length; i++) {
            cornerNE[i] = next;
            currentSigma = (currentSigma + 1)%2;
            next = next.sigma(sigmas[currentSigma]);
        }
        
        //horizontal sides
        int index = 0;
        int lastMissing = 2; //which sigma was not present in the last orbit that was looked at
        int lastUsed = (cornerNW.length)%2; //which sigma was last used (i.e to cross the edge)
        Chamber currentStart = cornerNW[cornerNW.length-1];
        
        while(currentStart!=cornerNE[0]){
            int[] localSigmas = {lastMissing, lastUsed};
            lastMissing = getRemaining(lastMissing, lastUsed); //which sigma is missing this time?
            int m = getM(currentStart, lastMissing); //m_ij value for current orbit
            
            if(lastMissing==2){
                //do nothing: this is a face
                //just go to the next orbit
                currentStart = walkAlongOrbit(currentStart, localSigmas[0], localSigmas[1], m-1);
            } else if(lastMissing==1) {
                if(lastUsed==0){
                    addEdgeToGraph(graph, vertices.get(currentStart), vertices.get(currentStart.sigma(0)), 0, -1, edges, currentStart);
                    currentStart = currentStart.sigma(2);
                } else { //lastUsed == 2
                    addEdgeToGraph(graph, vertices.get(currentStart), vertices.get(currentStart.sigma(0)), 0, 0, edges, currentStart);
                    currentStart = currentStart.sigma(0);
                }
            } else { //lastMissing == 0
                int bound;
                if(lastUsed==2 && m%2==0)
                    bound = (m-2)/2;
                else
                    bound = m/2; //also correct when m is odd
                
                //edges for which one vertex lies on the north side
                next = currentStart.sigma(localSigmas[0]);
                for (int i = 0; i < bound; i++) {
                    addEdgeToGraph(graph, vertices.get(next), vertices.get(next.sigma(0)), 0, 0, edges, next);
                    next = next.sigma(localSigmas[1]).sigma(localSigmas[0]);
                }
                
                currentStart = walkAlongOrbit(currentStart, localSigmas[0], localSigmas[1], m-1);
                
                //edges for which one vertex lies on the south side
                next = currentStart.sigma(localSigmas[(m+1)%2]).sigma(localSigmas[m/2]);
                for (int i = 0; i < bound; i++) {
                    addEdgeToGraph(graph, vertices.get(next), vertices.get(next.sigma(0)), 0, -1, edges, next);
                    next = next.sigma(localSigmas[(m+1)%2]).sigma(localSigmas[m/2]);
                }
            }
        }
        
        //vertical sides
        index = 0;
        lastMissing = 2; //which sigma was not present in the last orbit that was looked at
        lastUsed = (cornerNE.length)%2; //which sigma was last used (i.e to cross the edge)
        currentStart = cornerNE[cornerNE.length-1];
        
        while(currentStart!=cornerSE[0]){
            int[] localSigmas = {lastMissing, lastUsed};
            lastMissing = getRemaining(lastMissing, lastUsed); //which sigma is missing this time?
            int m = getM(currentStart, lastMissing); //m_ij value for current orbit
            
            if(lastMissing==2){
                //do nothing: this is a face
                //just go to the next orbit
                currentStart = walkAlongOrbit(currentStart, localSigmas[0], localSigmas[1], m-1);
            } else if(lastMissing==1) {
                if(lastUsed==0){
                    addEdgeToGraph(graph, vertices.get(currentStart), vertices.get(currentStart.sigma(0)), 1, 0, edges, currentStart);
                    currentStart = currentStart.sigma(2);
                } else { //lastUsed == 2
                    addEdgeToGraph(graph, vertices.get(currentStart), vertices.get(currentStart.sigma(0)), 0, 0, edges, currentStart);
                    currentStart = currentStart.sigma(0);
                }
            } else { //lastMissing == 0
                int bound;
                if(lastUsed==2 && m%2==0)
                    bound = (m-2)/2;
                else
                    bound = m/2; //also correct when m is odd
                
                //edges for which one vertex lies on the east side
                next = currentStart.sigma(localSigmas[0]);
                for (int i = 0; i < bound; i++) {
                    addEdgeToGraph(graph, vertices.get(next), vertices.get(next.sigma(0)), -1, 0, edges, next);
                    next = next.sigma(localSigmas[1]).sigma(localSigmas[0]);
                }
                
                currentStart = walkAlongOrbit(currentStart, localSigmas[0], localSigmas[1], m-1);
                
                //edges for which one vertex lies on the west side
                next = currentStart.sigma(localSigmas[(m+1)%2]).sigma(localSigmas[m/2]);
                for (int i = 0; i < bound; i++) {
                    addEdgeToGraph(graph, vertices.get(next), vertices.get(next.sigma(0)), 0, 0, edges, next);
                    next = next.sigma(localSigmas[(m+1)%2]).sigma(localSigmas[m/2]);
                }
            }
        }
        
        //remaining edges
        for (int i = 0; i < symbol.getSize(); i++) {
            if(!edges.containsKey(symbol.getChamber(i))){
                addEdgeToGraph(graph, vertices.get(symbol.getChamber(i)), vertices.get(symbol.getChamber(i).sigma(0)), 0, 0, edges, symbol.getChamber(i));
                
            }
        }

        return graph;
    }
    
    public static void addEdgeToGraph(Graph graph, Vertex v1, Vertex v2, int targetX, int targetY, Map<Chamber, Edge> map, Chamber chamber){
        if(map.containsKey(chamber))
            return;
        Edge edge = graph.addEdge(v1, v2, targetX, targetY);
        map.put(chamber, edge);
        map.put(chamber.sigma(0), edge);
        map.put(chamber.sigma(0).sigma(2), edge);
        map.put(chamber.sigma(0).sigma(2).sigma(0), edge);
    }

    private static Chamber walkAlongOrbit(Chamber start, int i, int j, int steps){
        Chamber next = start;
        int[] sigmas = {i, j};
        int counter = 0;
        int pos = 0;
        while(counter<steps){
            next = next.sigma(sigmas[pos]);
            pos = (pos + 1)%2;
            counter++;
        }
        return next;
    }
    
    private static int getRemaining(int i, int j){
        if(i==0) {
            if(j==1)
                return 2;
            else
                return 1;
        } else if(j==0){
            if(i==1)
                return 2;
            else
                return 1;
        } else {
            return 0;
        }
    }
    
    private static int getM(Chamber chamber, int excluded){
        if(excluded==2)
            return chamber.m(0);
        else if(excluded==0)
            return chamber.m(1);
        else
            return 2;
    }    
}

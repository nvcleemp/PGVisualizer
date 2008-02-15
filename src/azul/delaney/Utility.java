/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney;

import azul.toroidalembedder.graph.Edge;
import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author nvcleemp
 */
public class Utility {
    
    private static enum Side {
        NORTH, EAST, SOUTH, WEST;
    }
    
    public static Graph delaneyToTorGraph2(DelaneySymbol symbol){
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

        //arrays for sides: large enough
        Chamber[] sideN = new Chamber[symbol.getSize()];
        Chamber[] sideE = new Chamber[symbol.getSize()];
        Chamber[] sideS = new Chamber[symbol.getSize()];
        Chamber[] sideW = new Chamber[symbol.getSize()];
        
        //horizontal sides
        int index = 0;
        int lastMissing = 2; //which sigma was not present in the last orbit that was looked at
        int lastUsed = (cornerNW.length + 1)%2; //which sigma was last used (i.e to cross the edge)
        Chamber currentStart = cornerNW[cornerNW.length-1];
        sideN[index] = currentStart; //currentStart is always already in the array
        
        while(index==0 || sideN[index-1]!=cornerNE[0]){
            int[] localSigmas = {lastMissing, lastUsed};
            lastMissing = getRemaining(lastMissing, lastUsed); //which sigma is missing this time?
            int m = getM(currentStart, lastMissing); //m_ij value for current orbit
            currentSigma = 0;
            next = currentStart.sigma(localSigmas[currentSigma]);
            currentSigma = 1;
            for (int i = 1; i < m; i++) {
                sideN[index + i] = next;
                next = next.sigma(localSigmas[currentSigma]);
                currentSigma = (currentSigma+1)%2;
            }
            lastUsed = localSigmas[(currentSigma+1)%2];
            currentStart = sideN[index + m - 1];
            for (int i = 0; i < m; i++) {
                sideS[sideS.length - 1 - index - m + i] = next;
                next = next.sigma(localSigmas[currentSigma]);
                currentSigma = (currentSigma+1)%2;
            }
            index+=m;
        }
        
        //vertical sides
        index = 0;
        lastMissing = 2; //which sigma was not present in the last orbit that was looked at
        lastUsed = (cornerNE.length + 1)%2; //which sigma was last used (i.e to cross the edge)
        currentStart = cornerNE[cornerNE.length-1];
        sideN[index] = currentStart; //currentStart is always already in the array
        
        while(index==0 || sideE[index-1]!=cornerSE[0]){
            int[] localSigmas = {lastMissing, lastUsed};
            lastMissing = getRemaining(lastMissing, lastUsed); //which sigma is missing this time?
            int m = getM(currentStart, lastMissing); //m_ij value for current orbit
            currentSigma = 0;
            next = currentStart.sigma(localSigmas[currentSigma]);
            currentSigma = 1;
            for (int i = 1; i < m; i++) {
                sideE[index + i] = next;
                next = next.sigma(localSigmas[currentSigma]);
                currentSigma = (currentSigma+1)%2;
            }
            lastUsed = localSigmas[(currentSigma+1)%2];
            currentStart = sideE[index + m - 1];
            for (int i = 0; i < m; i++) {
                sideW[sideW.length - 1 - index - m + i] = next;
                next = next.sigma(localSigmas[currentSigma]);
                currentSigma = (currentSigma+1)%2;
            }
            index+=m;
        }
        

        return graph;
    }

    public static Graph delaneyToTorGraph(DelaneySymbol symbol){
        //TODO: convert symbol to translation only
        
        
        //determine sides of quadrangle
        Map<Chamber, Side> sides = new HashMap<Chamber, Side>();
        Chamber westStart = symbol.getChamber(0);
        int westCorner = westStart.m(0)/2; //number of chambers in west upper corner
        int eastCorner = westStart.m(0) - westCorner; //number of chambers in east upper corner
        
        sides.put(westStart, Side.WEST);
        sides.put(westStart.sigma(0), Side.EAST);
        Chamber northStart = walkAlongOrbit(westStart, 1, 0, westCorner - 1);
        sides.put(northStart, Side.NORTH);
        sides.put(northStart.sigma(westCorner%2), Side.SOUTH);
        Chamber northStop = walkAlongOrbit(westStart.sigma(0), 1, 0, eastCorner - 1);
        sides.put(northStop, Side.NORTH);
        sides.put(northStop.sigma(eastCorner%2), Side.SOUTH);
        Chamber westStop = walkAlongOrbit(northStart.sigma(westCorner%2), (westCorner+1)%2, westCorner%2, eastCorner-1);
        sides.put(westStop, Side.WEST);
        sides.put(westStop.sigma((eastCorner+westCorner)%2), Side.EAST);
        
        ///north side
        Chamber next = walkAlongOrbit(northStart, 2, westCorner%2, getM(northStart, (westCorner+1)%2)-1);
        int last = getM(northStart, (westCorner+1)%2)%2==0 ? 2 : westCorner%2;
        int excluded = (westCorner+1)%2;
        while(next!=northStop){
            sides.put(next, Side.NORTH);
            sides.put(next.sigma(getRemaining(last, excluded)), Side.SOUTH);
            
            //for the next walk to the side we start with the previous excluded
            //and the previous last is the new excluded
            int oldLast = last;
            int oldExcluded = excluded;
            excluded = last;
            next = walkAlongOrbit(next, oldExcluded, getRemaining(oldLast, oldExcluded), getM(next, excluded)-1);
            last = getM(next, last)%2==0 ? oldExcluded : getRemaining(oldLast, oldExcluded);
        }
        
        ///west side
        next = walkAlongOrbit(westStart, 2, 0, getM(northStart, 1)-1);
        last = getM(northStart, 1)%2==0 ? 2 : 0;
        excluded = 1;
        while(next!=westStop){
            sides.put(next, Side.WEST);
            sides.put(next.sigma(getRemaining(last, excluded)), Side.EAST);
            
            //for the next walk to the side we start with the previous excluded
            //and the previous last is the new excluded
            int oldLast = last;
            int oldExcluded = excluded;
            excluded = last;
            next = walkAlongOrbit(next, oldExcluded, getRemaining(oldLast, oldExcluded), getM(next, excluded)-1);
            last = getM(next, last)%2==0 ? oldExcluded : getRemaining(oldLast, oldExcluded);
        }
        

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
        
        
        //add the edges
        Map<Chamber, Edge> edges = new HashMap<Chamber, Edge>();
        for(int i = 0; i<symbol.getSize(); i++){
            Chamber chamber = symbol.getChamber(i);
            if(!edges.containsKey(chamber)){
                Vertex v1 = vertices.get(chamber);
                Vertex v2 = vertices.get(chamber.sigma(0));
                Edge e = null;
                if(sides.containsKey(chamber) && sides.containsKey(chamber.sigma(0))){
                    if(sides.get(chamber)==sides.get(chamber.sigma(0))){
                        //edge lies on the side
                        e = graph.addEdge(v1, v2, 0, 0);
                    } else if(sides.get(chamber)==sides.get(chamber.sigma(2))){
                        //edge intersects the side
                        if(sides.get(chamber).equals(Side.NORTH))
                            e = graph.addEdge(v1, v2, 0, -1);
                        else if(sides.get(chamber).equals(Side.SOUTH))
                            e = graph.addEdge(v1, v2, 0, 1);
                        else if(sides.get(chamber).equals(Side.EAST))
                            e = graph.addEdge(v1, v2, 1, 0);
                        else if(sides.get(chamber).equals(Side.WEST))
                            e = graph.addEdge(v1, v2, -1, 0);
                    } else {
                        //edge lies on a corner
                    }
                } else {
                    //edge doesn't lie on side nor does it intersect the side
                    //one of the vertices may still lie on side
                    //e = graph.addEdge(v1, v2, 0, 0);
                    List<Side> list1 = sidesForVertex(chamber, sides);
                    List<Side> list2 = sidesForVertex(chamber.sigma(0), sides);
                    if(list1.size()<2 && list2.size()<2){
                        //no vertices on sides
                        e = graph.addEdge(v1, v2, 0, 0);
                    } else if(list1.size()==2){
                        if(list1.contains(Side.NORTH) && list1.contains(Side.SOUTH)){//v1 on horizontal side
                            if(list1.get(0).equals(Side.SOUTH)) // v2 lies above v1
                                e = graph.addEdge(v1, v2, 0, -1);
                            else // v2 lies beneath v1
                                e = graph.addEdge(v1, v2, 0, 0);
                        } else if(list1.contains(Side.WEST) && list1.contains(Side.EAST)){//v1 on vertical side
                            if(list1.get(0).equals(Side.EAST)) // v2 lies left of v1
                                e = graph.addEdge(v1, v2, -1, 0);
                            else // v2 lies right of v1
                                e = graph.addEdge(v1, v2, 0, 0);
                        } else { //v1 just near a corner
                            e = graph.addEdge(v1, v2, 0, 0);
                        }
                        
                    } else if(list2.size()==2){
                        if(list2.contains(Side.NORTH) && list2.contains(Side.SOUTH)){//v2 on horizontal side
                            if(list2.get(0).equals(Side.SOUTH)) // v2 lies beneath v1
                                e = graph.addEdge(v1, v2, 0, 1);
                            else // v2 lies above v1
                                e = graph.addEdge(v1, v2, 0, 0);
                        } else if(list2.contains(Side.WEST) && list2.contains(Side.EAST)){//v2 on vertical side
                            if(list2.get(0).equals(Side.EAST)) // v2 lies right of v1
                                e = graph.addEdge(v1, v2, 1, 0);
                            else // v2 lies left of v1
                                e = graph.addEdge(v1, v2, 0, 0);
                        } else { //v2 just near a corner
                            e = graph.addEdge(v1, v2, 0, 0);
                        }
                    } else if(list1.size()==4){
                        //v1 on corner
                        int x=0;
                        int y=0;
                        if(list1.indexOf(Side.EAST)<list1.indexOf(Side.WEST)) //v2 lies left of v1
                            x=-1;
                        if(list1.indexOf(Side.SOUTH)<list1.indexOf(Side.NORTH)) //v2 lies above of v1
                            y=-1;
                        e = graph.addEdge(v1, v2, x, y);
                    } else if(list2.size()==4){
                        //v2 on corner
                        int x=0;
                        int y=0;
                        if(list2.indexOf(Side.EAST)<list2.indexOf(Side.WEST)) //v2 lies right of v1
                            x=1;
                        if(list2.indexOf(Side.SOUTH)<list2.indexOf(Side.NORTH)) //v2 lies beneath of v1
                            y=1;
                        e = graph.addEdge(v1, v2, x, y);
                    } else {
                        throw new RuntimeException("List 1 has size " + list1.size() + " and list 2 has size " + list2.size());
                    }
                }
                
                if(e==null)
                    throw new RuntimeException("Edge wasn't added.");
                edges.put(chamber, e);
                edges.put(chamber.sigma(0), e);
                edges.put(chamber.sigma(0).sigma(2), e);
                edges.put(chamber.sigma(0).sigma(2).sigma(0), e);
                
                //while(!edges.containsKey(chamber)){  
                //}
            }
        }
        
        
        return graph;
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
    
    private static List<Side> sidesForVertex(Chamber chamber, Map<Chamber, Side> sides){
        int counter = 0;
        List<Side> sidesList = new ArrayList<Side>();
        Chamber next = chamber;
        if(sides.containsKey(next) && !sidesList.contains(sides.get(next)))
            sidesList.add(sides.get(next));
        while(counter<chamber.m(1)){
            next = next.sigma(1);
            if(sides.containsKey(next) && !sidesList.contains(sides.get(next)))
                sidesList.add(sides.get(next));
            next = next.sigma(2);
            if(sides.containsKey(next) && !sidesList.contains(sides.get(next)))
                sidesList.add(sides.get(next));
            counter++;
        }

        return sidesList;
    }

    private static List<Side> sidesForEdge(Chamber chamber, Map<Chamber, Side> sides){
        int counter = 0;
        List<Side> sidesList = new ArrayList<Side>();
        Chamber next = chamber;
        if(sides.containsKey(next) && !sidesList.contains(sides.get(next)))
            sidesList.add(sides.get(next));
        while(counter<2){
            next = next.sigma(0);
            if(sides.containsKey(next) && !sidesList.contains(sides.get(next)))
                sidesList.add(sides.get(next));
            next = next.sigma(2);
            if(sides.containsKey(next) && !sidesList.contains(sides.get(next)))
                sidesList.add(sides.get(next));
            counter++;
        }

        return sidesList;
    }

}

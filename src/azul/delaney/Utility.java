/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney;

import azul.delaney.FundamentalPatch.ChamberDelegate;
import azul.toroidalembedder.graph.Edge;
import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author nvcleemp
 */
public class Utility {
    
    public static Path dijkstra(DelaneySymbol symbol, Chamber start, Chamber target, Set<Chamber> illegals){
        Queue<Chamber> queue = new LinkedList<Chamber>();
        Map<Chamber, Integer> distance = new HashMap<Chamber, Integer>();
        Map<Chamber, Integer> previous = new HashMap<Chamber, Integer>();
        illegals.remove(target); //make sure target isn't illegal
        distance.put(start, 0);
        queue.offer(start);
        while(!distance.containsKey(target) && !queue.isEmpty()){
            Chamber current = queue.poll();
            int dist = distance.get(current);
            for (int i = 0; i <= symbol.getDimension(); i++) {
                if(!illegals.contains(current.sigma(i)) && (!distance.containsKey(current.sigma(i)) || distance.get(current.sigma(i)) > dist + 1)){
                    previous.put(current.sigma(i), i);
                    distance.put(current.sigma(i), dist + 1);
                    queue.offer(current.sigma(i));
                }
            }
        }
        if(!distance.containsKey(target))
            throw new RuntimeException("Target not reached by Dijkstra algorithm.");
        
        int[] path = new int[distance.get(target)];
        Chamber last = target;
        for (int i = path.length - 1; i >= 0; i--) {
            path[i] = previous.get(last);
            last = last.sigma(path[i]);
        }
        
        Path shortestPath = new Path(start, path);
        
        if(target!=shortestPath.getEnd())
            throw new RuntimeException("Error in shortest path by Dijkstra algorithm.");

        return shortestPath;
    }
    
    public static Path dijkstraPath(DelaneySymbol symbol, Chamber start, Path targetPath, Set<Chamber> illegals){
        Chamber target = null;
        Queue<Chamber> queue = new LinkedList<Chamber>();
        Map<Chamber, Integer> distance = new HashMap<Chamber, Integer>();
        Map<Chamber, Integer> previous = new HashMap<Chamber, Integer>();
        illegals.remove(targetPath.getStart());
        distance.put(start, 0);
        queue.offer(start);
        while(!queue.isEmpty() && (target==null || distance.get(queue.peek()) < distance.get(target))){
            Chamber current = queue.poll();
            int dist = distance.get(current);
            for (int i = 0; i <= symbol.getDimension(); i++) {
                if(!illegals.contains(current.sigma(i)) && (!distance.containsKey(current.sigma(i)) || distance.get(current.sigma(i)) > dist + 1)){
                    previous.put(current.sigma(i), i);
                    distance.put(current.sigma(i), dist + 1);
                    queue.offer(current.sigma(i));
                }
                if(!illegals.contains(current.sigma(i)) && targetPath.contains(current.sigma(i))  && 
                        (target==null || targetPath.getPosition(current.sigma(i))<targetPath.getPosition(target)))
                    target = current.sigma(i);
            }
        }

        if(target==null)
            throw new RuntimeException("No shortest path to path");
        
        int[] path = new int[distance.get(target)];
        Chamber last = target;
        for (int i = path.length - 1; i >= 0; i--) {
            path[i] = previous.get(last);
            last = last.sigma(path[i]);
        }
        Path shortestPath = new Path(start, path);
        
        if(target!=shortestPath.getEnd())
            throw new RuntimeException("Error in shortest path by Dijkstra algorithm.");

        return shortestPath;
    }
    
    public static Path straightLinePath(DelaneySymbol symbol, Chamber start, int startSigma, int secondSigma, Set<Chamber> illegals){
        int lastMissing = startSigma; //which sigma was not present in the last orbit that was looked at
        int lastUsed = secondSigma; //which sigma was last used (i.e to cross the edge)
        Chamber next = start;
        Set<Chamber> myIllegals = new HashSet<Chamber>(illegals);
        List<Integer> pathSigmas = new ArrayList<Integer>();
        boolean atStart = true;
        
        while(atStart || !myIllegals.contains(next)){
            if(atStart)
                atStart = false; //TODO: find better condition
            
            int[] localSigmas = {lastMissing, lastUsed};
            lastMissing = getRemaining(lastMissing, lastUsed); //which sigma is missing this time?
            int m = getM(next, lastMissing); //m_ij value for current orbit

            int counter = 0;
            int pos = 0;
            while(counter<m-1  && !(myIllegals.contains(next))){
                next = next.sigma(localSigmas[pos]);
                if(!(myIllegals.contains(next))){
                    myIllegals.add(next);
                    pathSigmas.add(localSigmas[pos]);
                }
                pos = (pos + 1)%2;
                counter++;
            }
        }
        
        int[] path = new int[pathSigmas.size()];
        for (int i = 0; i < path.length; i++)
            path[i] = pathSigmas.get(i);
        
        return new Path(start, path);
    }
    
    public static Path cornerToCorner(DelaneySymbol symbol, Chamber start, Chamber target, int firstSigma, int secondSigma, Set<Chamber> illegals){

        Path attemptStraight = straightLinePath(symbol, start, firstSigma, secondSigma, illegals);
        Path shortestPath = dijkstraPath(symbol, target, attemptStraight, illegals).getInversePath();
        
        return attemptStraight.subPath(shortestPath.getStart()).concat(shortestPath);
    }
    
    public static Graph twoPaths(DelaneySymbol symbol){
        if(!symbol.isOrientable()){
            try {
                symbol = symbol.getBasicDelaney().getOrientable().toDelaneySymbol();

            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println(exception);
                throw new RuntimeException("not orientable");
            }
            System.out.println(symbol.isOrientable());
            //throw new RuntimeException("not orientable");
        }
        
        
        Chamber start = symbol.getChamber(0);
        
        //determine corner
        Set<Chamber> corner = new HashSet<Chamber>();
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
        
        for (Chamber chamber : cornerNW)
            corner.add(chamber);
        for (Chamber chamber : cornerNE)
            corner.add(chamber);
        for (Chamber chamber : cornerSE)
            corner.add(chamber);
        for (Chamber chamber : cornerSW)
            corner.add(chamber);
        
        Path first = cornerToCorner(symbol, cornerNW[cornerNW.length-1], cornerNE[0], 2, (cornerNW.length)%2, corner);
        //System.out.println("Found first path.");
        
        Set<Chamber> illegals = first.getChambers();
        //illegals.addAll(first.getBorderingPath((cornerNW.length)%2).getChambers());
        illegals.addAll(corner);
        //System.out.println("Calculated illegals for second path.");
        
        Path second = dijkstra(symbol, cornerNE[cornerNE.length-1], cornerSE[0], illegals);
        //System.out.println("Found second path.");
        
        
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
        
        Chamber chamber = first.getStart();
        for (int i = 0; i < first.getLength() - 1; i++) {
            System.out.print(first.getSigmaAt(i) + " ");
            chamber = chamber.sigma(first.getSigmaAt(i));
            if(first.getSigmaAt(i)==2){
                int prevS, nextS;
                if(i>0)
                    prevS=first.getSigmaAt(i-1);
                else
                    prevS=-1;

                if(i<first.getLength() - 2)
                    nextS=first.getSigmaAt(i+1);
                else
                    nextS=-1;
                                
               if((prevS==1 && nextS==1) || ((prevS==-1 || nextS==-1) && (prevS==1 || nextS==1))){
                   addEdgeToGraph(graph, vertices.get(chamber), vertices.get(chamber.sigma(0)), 0, -1, edges, chamber);
               }
            }
        }

        System.out.println();
        
        Path south = first.getBorderingPath((cornerNW.length + 1)%2);
        chamber = south.getStart();
        for (int i = 0; i < south.getLength() - 1; i++) {
            System.out.print(south.getSigmaAt(i) + " ");
            chamber = chamber.sigma(south.getSigmaAt(i));
            if(south.getSigmaAt(i)==2){
                int prevS, nextS;
                if(i>0)
                    prevS=south.getSigmaAt(i-1);
                else
                    prevS=-1;

                if(i<south.getLength() - 2)
                    nextS=south.getSigmaAt(i+1);
                else
                    nextS=-1;
                
               if(prevS==0 || nextS==0){
                   addEdgeToGraph(graph, vertices.get(chamber), vertices.get(chamber.sigma(0)), 0, 1, edges, chamber);
               }
            }
        }
        
        System.out.println();
        System.out.println();
        
        chamber = second.getStart();
        for (int i = 0; i < second.getLength() - 1; i++) {
            chamber = chamber.sigma(second.getSigmaAt(i));
            if(second.getSigmaAt(i)==2){
                int prevS, nextS;
                if(i>0)
                    prevS=second.getSigmaAt(i-1);
                else
                    prevS=-1;

                if(i<first.getLength() - 2)
                    nextS=second.getSigmaAt(i+1);
                else
                    nextS=-1;
                
                addEdgeToGraph(graph, vertices.get(chamber), vertices.get(chamber.sigma(0)), 1, 0, edges, chamber);
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
    
    public static void edgeForTree(FundamentalPatch tree){
        DelaneySymbol symbol = tree.getSymbol();
        for (int i = 0; i < symbol.getSize(); i++) {
            ChamberDelegate node = tree.getDelegateForChamber(symbol.getChamber(i));
        }

    }
    
    public static Graph translationOnlyDelaneySymbolToPeriodicGraph(DelaneySymbol symbol){
        BasicDelaney bd = symbol.getBasicDelaney();
        BasicDelaney patch = bd.getPatch();
        
        return null;
    }
    

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

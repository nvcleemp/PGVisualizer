/* Path.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
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

package be.ugent.caagt.pg.delaney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Path represents a path of chambers inside a Delaney symbol. This path may not intersect on itself.
 * @author nvcleemp
 */
public class Path {

    private Map<Chamber, Integer> chambers = new HashMap<Chamber, Integer>();
    private Chamber start;
    private Chamber end;
    private int[] path;

    public Path(Chamber start, int[] path) {
        super();
        this.start = start;
        this.path = new int[path.length];
        System.arraycopy(path, 0, this.path, 0, path.length);
        Chamber current = start;
        chambers.put(start, 0);
        for (int i = 0; i < path.length; i++) {
            current = current.sigma(path[i]);
            if(chambers.containsKey(current))
                throw new IllegalArgumentException("Path intersects on itself.");
            chambers.put(current, i + 1);
        }
        end = current;
    }

    /**
     * Returns the first Chamber in this path.
     * @return The first Chamber in this path.
     */
    public Chamber getStart() {
        return start;
    }

    /**
     * Returns the last Chamber in this path.
     * @return The last Chamber in this path.
     */
    public Chamber getEnd() {
        return end;
    }

    /**
     * Returns the position of a chamber in this path or -1 if the
     * chamber doesn't belong to the path. This method will return in constant time.
     * @param c The chamber whose position in this path is requested
     * @return The position of c or -1 if c doesn't belong to the path.
     */
    public int getPosition(Chamber c) {
        if (chambers.containsKey(c)) {
            return chambers.get(c);
        } else {
            return -1;
        }
    }

    /**
     * Returns <tt>true</tt> if this path contains the chamber.
     * This method will return in constant time.
     * @param c The chamber whose presence in this path is requested.
     * @return <tt>true</tt> if this path contains the chamber.
     */
    public boolean contains(Chamber c) {
        return chambers.containsKey(c);
    }
    
    public Set<Chamber> getChambers(){
        return new HashSet<Chamber>(chambers.keySet());
    }
    
    public Path getInversePath(){
        int[] newPath = new int[path.length];
        for (int i = 0; i < newPath.length; i++) {
            newPath[i] = path[path.length-1-i];
        }
        return new Path(end, newPath);
    }
    
    public Path concat(Path secondPath){
        if(!end.equals(secondPath.start))
            throw new IllegalArgumentException("Second path should start with end of this path.");
        int[] newPath = new int[path.length + secondPath.path.length];
        if(path.length!=0)
           System.arraycopy(path, 0, newPath, 0, path.length - 1);
        if(secondPath.path.length!=0)
           System.arraycopy(secondPath.path, 0, newPath, (path.length - 1 > 0 ? path.length - 1 : 0), secondPath.path.length);
        return new Path(start, newPath);
    }
    
    public Path subPath(Chamber newEnd){
        return subPath(getPosition(newEnd));
    }
    
    public Path subPath(int length){
        int[] newPath = new int[length];
        System.arraycopy(path, 0, newPath, 0, length);
        return new Path(start, newPath);
    }
    
    public Path getBorderingPath(int startSigma){
        if(startSigma==path[0])
            throw new IllegalArgumentException("Wrong start sigma for a bordering path");
        
        Chamber newStart = start.sigma(startSigma);
        Chamber next = newStart;
        List<Integer> sigmas = new ArrayList<Integer>();
        
        int[] localSigmas = {startSigma, getRemaining(startSigma, path[0])};
        int pos = 0;
        
        while(!next.sigma(localSigmas[pos]).equals(end)){
            int lastUsed = localSigmas[pos];
            int lastMissing = getRemaining(localSigmas[0], localSigmas[1]);
            localSigmas[0] = lastMissing;
            localSigmas[1] = lastUsed;
            pos = 0;
            
            while(!chambers.containsKey(next.sigma(localSigmas[pos]))){
                sigmas.add(localSigmas[pos]);
                next = next.sigma(localSigmas[pos]);
                pos = (pos + 1)%2;
            }
        }
        
        int[] newPath = new int[sigmas.size()];
        for (int i = 0; i < newPath.length; i++)
            newPath[i] = sigmas.get(i);
        
        return new Path(newStart, newPath);
    }
    
    public int getSigmaAt(int i){
        return path[i];
    }
    
    int getLength() {
        return path.length + 1;
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
    
}

/* DefaultGraph.java
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

package be.ugent.caagt.pg.graph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class DefaultGraph implements VertexListener, FundamentalDomainListener, Graph {
    
    private List<DefaultVertex> vertices = new ArrayList<DefaultVertex>();
    private List<GraphListener> listeners = new ArrayList<GraphListener>();
    private List<Face> faces = new ArrayList<Face>();
    private FundamentalDomain fundamentalDomain;
    
    /** Creates a new instance of DefaultGraph */
    public DefaultGraph() {
        this(new FundamentalDomain());
    }

    public DefaultGraph(FundamentalDomain fundamentalDomain) {
        this.fundamentalDomain = fundamentalDomain;
        fundamentalDomain.addFundamentalDomainListener(this);
    }
    
    public List<DefaultVertex> getVertices(){
        return vertices;
    }
    
    public DefaultVertex getVertex(int index){
        int i = 0;
        while(i<vertices.size() && vertices.get(i).getIndex()!=index)
            i++;
        if(i==vertices.size())
            return null;
        else
            return vertices.get(i);
    }
    
    public boolean addVertex(DefaultVertex vertex){
        if(vertex.getIndex()!=-1)
            return false;
        vertex.addVertexListener(this);
        vertex.setIndex(vertices.size());
        vertices.add(vertex);
        fireGraphChanged();
        return true;
    }
    
    public DefaultEdge addEdge(int start, int end, int targetX, int targetY){
        return addEdge(vertices.get(start), vertices.get(end), targetX, targetY);
    }
    
    public DefaultEdge addEdge(DefaultVertex start, DefaultVertex end, int targetX, int targetY){
        if(!(vertices.contains(start) && vertices.contains(end)))
            return null;
        DefaultEdge e = DefaultEdge.getEdge(start, end, targetX, targetY);
        start.addEdge(e);
        end.addEdge(e.getInverse());
        fireGraphChanged();
        return e;
    }
    
    public double getEdgeLength(Edge e){
        return e.getLength(fundamentalDomain);
    }
    
    public void translate(double dx, double dy){
        for(DefaultVertex vertex : vertices)
            vertex.translate(dx, dy, fundamentalDomain);
    }

    public FundamentalDomain getFundamentalDomain() {
        return fundamentalDomain;
    }

    public void setFundamentalDomain(FundamentalDomain fundamentalDomain) {
        if(!this.fundamentalDomain.equals(fundamentalDomain)){
            FundamentalDomain oldDomain = this.fundamentalDomain;
            oldDomain.removeFundamentalDomainListener(this);
            this.fundamentalDomain = fundamentalDomain;
            fundamentalDomain.addFundamentalDomainListener(this);
            fireFundamentalDomainChanged(oldDomain);
        }
    }
    
    private void fireGraphChanged(){
        for (GraphListener listener : listeners) {
            listener.graphChanged();
        }
    }
    
    private void fireFundamentalDomainChanged(FundamentalDomain oldDomain){
        for (GraphListener listener : listeners) {
            listener.fundamentalDomainChanged(oldDomain);
        }
    }
    
    private void fireFundamentalDomainShapeChanged(){
        for (GraphListener listener : listeners) {
            listener.fundamentalDomainShapeChanged();
        }
    }
    
    public void addGraphListener(GraphListener listener){
        listeners.add(listener);
    }

    public void removeGraphListener(GraphListener listener){
        listeners.remove(listener);
    }

    public void vertexMoved() {
        fireGraphChanged();
    }

    public void fundamentalDomainShapeChanged() {
        fireFundamentalDomainShapeChanged();
    }
    
    public boolean addFace(Face f){
        if(f.getIndex()!=-1)
            return false;
        f.setIndex(faces.size());
        faces.add(f);
        return true;
    }
    
    public List<Face> getFaces(){
        return faces; //TODO: return copy of list
    }
}

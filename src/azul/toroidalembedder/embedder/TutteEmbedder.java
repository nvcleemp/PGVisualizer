/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;

import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.graph.general.Vertex;
import azul.toroidalembedder.gui.GraphModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class TutteEmbedder extends AbstractEmbedder {
    
    List<Vertex> initialVertices = null;
    List<Vertex> embeddedVertices = null;
    
    /** Creates a new instance of DefaultEmbedder */
    public TutteEmbedder(Graph graph) {
        super(graph);
    }

    public TutteEmbedder(GraphModel graphModel) {
        super(graphModel);
    }


    @Override
    protected void resetEmbedder() {
        initialVertices = new ArrayList<Vertex>();
        for (Vertex vertex : graph.getVertices()) {
            boolean external = false;
            List<? extends Edge> edges = vertex.getEdges();
            int i = 0;
            while(!external && i<edges.size()){
                external = edges.get(i).getTargetX()!=0 || edges.get(i).getTargetY()!=0;
                i++;
            }
            if(external)
                initialVertices.add(vertex);
        }
        embeddedVertices = null;
    }

    public void initialize() {
        if(initialVertices == null)
            return;
        embeddedVertices = new ArrayList<Vertex>(initialVertices);
        List<Vertex> vertices = new ArrayList<Vertex>(graph.getVertices());
        int previous = vertices.size();
        vertices.removeAll(embeddedVertices);
        while(vertices.size()!=previous){
            previous = vertices.size();
            for (Vertex vertex : vertices) {
                List<Vertex> neighbours = new ArrayList<Vertex>();
                for (Edge edge : vertex.getEdges()) {
                    if(edge.getTargetX()==0 && edge.getTargetY()==0 && !neighbours.contains(edge.getEnd()))
                        neighbours.add(edge.getEnd());
                }
                if(neighbours.size()==2 || neighbours.size()==3){
                    double x = 0;
                    double y = 0;
                    for (Vertex v : neighbours) {
                        x += v.getRawX();
                        y += v.getRawY();
                    }
                    vertex.setRawCoordinates(x/3, y/3);
                    embeddedVertices.add(vertex);
                }
            }
            vertices.removeAll(embeddedVertices);
        }
    }

    public void embed() {
        if(initialVertices == null)
            return;
        else if(embeddedVertices == null)
            initialize();
    }
}

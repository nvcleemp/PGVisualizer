/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder;

import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Vertex;
import java.util.Random;

/**
 * Gives the vertices random coordinates during initialisation and doesn't do anything after that.
 * Can be used before an embedder that requires that not all the vertices are at the same location.
 */
public class RandomEmbedder implements Embedder {
    
    private Graph graph;
    private static final Random RG = new Random();

    public RandomEmbedder(Graph graph) {
        this.graph = graph;
    }

    public void initialize() {
        for (Vertex v : graph.getVertices())
            v.setRawCoordinates(RG.nextDouble()*2-1, RG.nextDouble()*2-1);
    }

    public void embed() {
        // do nothing
    }

}

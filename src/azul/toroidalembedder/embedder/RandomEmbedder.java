/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;

import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.graph.general.Vertex;
import azul.toroidalembedder.gui.GraphModel;
import java.util.Random;

/**
 * Gives the vertices random coordinates during initialisation and doesn't do anything after that.
 * Can be used before an embedder that requires that not all the vertices are at the same location.
 */
public class RandomEmbedder extends AbstractEmbedder {
    
    private static final Random RG = new Random();

    public RandomEmbedder(Graph graph) {
        super(graph);
    }

    public RandomEmbedder(GraphModel graphModel) {
        super(graphModel);
    }

    public void initialize() {
        for (Vertex v : graph.getVertices())
            v.setRawCoordinates(RG.nextDouble()*2-1, RG.nextDouble()*2-1);
    }

    public void embed() {
        // do nothing
    }

    @Override
    protected void resetEmbedder() {
        //
    }
}

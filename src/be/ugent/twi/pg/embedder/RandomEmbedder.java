/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.embedder;

import be.ugent.twi.pg.graph.Graph;
import be.ugent.twi.pg.graph.Vertex;
import be.ugent.twi.pg.visualizer.gui.GraphListModel;
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

    public RandomEmbedder(GraphListModel graphModel) {
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

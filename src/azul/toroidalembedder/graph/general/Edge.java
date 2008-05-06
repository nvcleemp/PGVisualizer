/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.graph.general;

import azul.toroidalembedder.graph.*;

/**
 *
 * @author nvcleemp
 */
public interface Edge {

    public Vertex getEnd();

    public Edge getInverse();

    public Vertex getStart();

    public int getTargetX();

    public int getTargetY();

    public void translateTartget(int dx, int dy);

}

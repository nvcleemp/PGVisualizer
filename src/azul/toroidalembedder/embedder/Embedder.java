
/*
 * Embedder.java
 *
 * Created on January 10, 2008, 4:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;

import azul.toroidalembedder.gui.GraphListModelListener;

/**
 *
 * @author nvcleemp
 */
public interface Embedder extends GraphListModelListener{
    
    public void initialize();
    public void embed();
    
}


/*
 * Embedder.java
 *
 * Created on January 10, 2008, 4:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;

import azul.toroidalembedder.gui.GraphModelListener;

/**
 *
 * @author nvcleemp
 */
public interface Embedder extends GraphModelListener{
    
    public void initialize();
    public void embed();
    
}

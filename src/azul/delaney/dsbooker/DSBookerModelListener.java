/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney.dsbooker;

import azul.delaney.DelaneySymbol;

/**
 *
 * @author nvcleemp
 */
public interface DSBookerModelListener {
    public void configured();
    public void newGraphInLibrary(DelaneySymbolLibrary library, DelaneySymbol originalSymbol);
    public void finish();
}

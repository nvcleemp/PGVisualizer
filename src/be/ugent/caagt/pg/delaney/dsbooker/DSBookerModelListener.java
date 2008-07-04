/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.delaney.dsbooker;

import be.ugent.caagt.pg.delaney.DelaneySymbol;

/**
 *
 * @author nvcleemp
 */
public interface DSBookerModelListener {
    public void configured();
    public void newGraphInLibrary(DelaneySymbolLibrary library, DelaneySymbol originalSymbol);
    public void finish();
}

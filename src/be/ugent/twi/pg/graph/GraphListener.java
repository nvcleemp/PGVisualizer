/*
 * GraphListener.java
 *
 * Created on January 10, 2008, 3:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package be.ugent.twi.pg.graph;

/**
 *
 * @author nvcleemp
 */
public interface GraphListener extends FundamentalDomainListener {
    
    public void graphChanged();
    public void fundamentalDomainChanged(FundamentalDomain oldDomain);
}

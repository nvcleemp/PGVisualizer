/* DSBookerHeadlessView.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008-2009 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.pg.delaney.dsbooker;

import be.ugent.caagt.pg.delaney.DelaneySymbol;
import be.ugent.caagt.pg.io.IOManager;
import java.io.PrintStream;
import java.util.List;

/**
 * This view for DSBookerModel will print all the output to stdout.
 */
public class DSBookerHeadlessView implements DSBookerModelListener {
    
    private boolean readableOutput = false;
    private int verbosity = 0;
    private PrintStream out = System.out;
    private PrintStream err = System.err;
    private OutputType outputType;
    private DSBookerModel model;

    public DSBookerHeadlessView(DSBookerModel model) {
        this.model = model;
    }

    public void setReadable(boolean aFlag) {
        readableOutput = aFlag;
    }

    public void configured() {
        //do nothing: this listener is born ready
    }

    public void newGraphInLibrary(DelaneySymbolLibrary library, DelaneySymbol originalSymbol) {
        err.print(".");
        if(verbosity<2)
            return;
        err.println("Added symbol to library.");
        if(verbosity<3)
            return;
        if(readableOutput)
            originalSymbol.printSymbol(out);
        else
            out.println(IOManager.writeDS(originalSymbol));
    }
    
    public void increaseVerbosity(){
        verbosity++;
    }

    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }

    public void finish() {
        err.println("Finished");
        if(outputType!=null)
            for (DelaneySymbol symbol : outputType.getList(model))
                if(readableOutput)
                    symbol.printSymbol(out);
                else
                    out.println(IOManager.writeDS(symbol));
    }
    
    
    public enum OutputType {
        ALL{public List<DelaneySymbol> getList(DSBookerModel model){return model.getUnion();}},
        FINAL{public List<DelaneySymbol> getList(DSBookerModel model){return model.getMainLibrary().getList();}},
        PLUS{public List<DelaneySymbol> getList(DSBookerModel model){return model.mainLibraryPlus();}},
        MINUS{public List<DelaneySymbol> getList(DSBookerModel model){return model.mainLibraryMinus();}},
        NOT_MINIMAL{public List<DelaneySymbol> getList(DSBookerModel model){return model.mainLibraryNotMinimal();}};
        
        public abstract List<DelaneySymbol> getList(DSBookerModel model);
    }
}

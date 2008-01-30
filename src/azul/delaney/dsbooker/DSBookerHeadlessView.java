/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney.dsbooker;

import azul.delaney.DelaneySymbol;
import azul.io.IOManager;
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
        MINUS{public List<DelaneySymbol> getList(DSBookerModel model){return model.mainLibraryMinus();}};
        
        public abstract List<DelaneySymbol> getList(DSBookerModel model);
    }
}

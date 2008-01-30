/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney.dsbooker;

import be.ugent.twijug.jclops.CLManager;
import be.ugent.twijug.jclops.CLParseException;
import be.ugent.twijug.jclops.annotations.AtMostOneOf;
import be.ugent.twijug.jclops.annotations.AtMostOneOfList;
import be.ugent.twijug.jclops.annotations.Exclusion;
import be.ugent.twijug.jclops.annotations.Option;
import be.ugent.twijug.jclops.annotations.Repeatable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicolas Van Cleemput
 */
@AtMostOneOfList({
    @AtMostOneOf({"human-readable","machine-readable"}),
    @AtMostOneOf({"final","plus", "minus"}),
    @AtMostOneOf({"as-is","canonical", "minimal"})
})
public class DSBooker {
    
    private DSBookerModel model = new DSBookerModel();
    private DSBookerHeadlessView headlessView = new DSBookerHeadlessView(model);
    private boolean quiet = false;
    private DSBookerGUI window = new DSBookerGUI();
    private List<File> libraryFiles = new ArrayList<File>();
    
    @Option(longName="help", shortName='h', exclude=Exclusion.OTHER_OPTIONS_OR_ARGUMENTS, priority=100, description="Show a more verbose help message.")
    public void showHelp(){
        System.out.println("This is the help.");
    }

    @Option(shortName='x', description="Run the graphical user interface of DSBooker.", usageRank=3)
    public void setWindow(){
        model.addListener(window);
    }
    
    @Option(shortName='t', description="Print human-readable tables of the Delaney symbols.", usageRank=5)
    public void setHumanReadable(){
        headlessView.setReadable(true);
    }
    
    @Option(shortName='f', description="Print machine-readable files of the Delaney symbols.", usageRank=5)
    public void setMachineReadable(){
        headlessView.setReadable(false);
    }
    
    @Option(shortName='v', description="Increase the level of verbosity.", usageRank=3)
    public void setVerbose(){
        headlessView.increaseVerbosity();
    }
    
    @Option(shortName='q', description="Disable the output to standard out and standard error.", usageRank=3)
    public void setQuiet(){
        quiet = true;
    }
    
    @Option(usageRank=4, description="Output all the libraries.")
    public void setAll(){
        headlessView.setOutputType(DSBookerHeadlessView.OutputType.ALL);
    }
    
    @Option(usageRank=4, description="Output the final main library.")
    public void setFinal(){
        headlessView.setOutputType(DSBookerHeadlessView.OutputType.FINAL);
    }
    
    @Option(usageRank=4, description="Output the symbols that are in the main library but not in the other libraries.")
    public void setPlus(){
        headlessView.setOutputType(DSBookerHeadlessView.OutputType.PLUS);
    }
    
    @Option(usageRank=4, description="Output the symbols that are not in the main library but are in the other libraries.")
    public void setMinus(){
        headlessView.setOutputType(DSBookerHeadlessView.OutputType.MINUS);
    }
    
    public void setAsIs(){
        model.setLibraryType(DelaneySymbolLibrary.LibraryType.AS_IS);
    }
    
    public void setCanonical(){
        model.setLibraryType(DelaneySymbolLibrary.LibraryType.CANONICAL);
    }
    
    public void setMinimal(){
        model.setLibraryType(DelaneySymbolLibrary.LibraryType.MINIMAL);
    }
    
    @Option(shortName='l', description="Included a library file.")
    @Repeatable
    public void setLibraryFile(File... files){
        for (File file : files) {
            libraryFiles.add(file);
        }
    }
    
    @Option(shortName='i', description="Set input file.")
    public void setInput(File file){
        try {
            model.setMainInput(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DSBooker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        System.err.println("Hello, this is the dsbooker.");
        if(!quiet)
            model.addListener(headlessView);
        model.initialize(libraryFiles);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DSBooker dsbooker = new DSBooker();
        CLManager clm = new CLManager();
        clm.registerArgumentParser(new FileParser(), File.class);
        clm.setContext(dsbooker);
        try {
            clm.parse(args);
            dsbooker.run();
        } catch (CLParseException e) {
            System.err.println(clm.getUsageMessage());
            System.exit(1);
        }
    }

}

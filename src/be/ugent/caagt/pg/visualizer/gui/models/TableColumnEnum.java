package be.ugent.caagt.pg.visualizer.gui.models;

import be.ugent.caagt.pg.visualizer.groups.WallpaperGroup;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;

public enum TableColumnEnum {

    INDEX {
        public String getName() {
            return "Index";
        }

        public Object getValue(int i, GraphListModel graphListModel) {
            return i + 1;
        }

        public Class<?> getColumnClass(){
            return Integer.class;
        }

        public Object getPrototypeCellValue(GraphListModel graphListModel){
            return (graphListModel.getSize() + 10) * 10;
        }

        @Override
        public boolean canGrowBeyondSetSize(){
            return false;
        }
    }, CATALOGUENUMBER {
        public String getName() {
            return "Catalogue number";
        }

        public Object getValue(int i, GraphListModel graphListModel) {
            return graphListModel.getCatalogueNumber(i);
        }

        public Class<?> getColumnClass(){
            return Integer.class;
        }

        public Object getPrototypeCellValue(GraphListModel graphListModel){
            return (graphListModel.getCatalogueNumber(graphListModel.getSize()-1) + 10) * 10;
        }

        @Override
        public boolean canGrowBeyondSetSize(){
            return false;
        }
    }, NUMBEROFVERTICES{
        public String getName() {
            return "Number of vertices";
        }

        public Object getValue(int i, GraphListModel graphListModel) {
            return graphListModel.getGraph(i).getVertices().size();
        }

        public Class<?> getColumnClass(){
            return Integer.class;
        }

        public Object getPrototypeCellValue(GraphListModel graphListModel){
            //this was removed for performance reasons with large files
//            int max = 0;
//            for (int i = 0; i < graphListModel.getSize(); i++) {
//                if(graphListModel.getGraph(i).getVertices().size()>max)
//                    max = graphListModel.getGraph(i).getVertices().size();
//            }
//            return (max + 10) * 10;
            return 99999;
        }
    }, GROUP {
        public String getName() {
            return "Wallpaper group";
        }

        public Object getValue(int i, GraphListModel graphListModel) {
            return graphListModel.getGraphGUIData(i).getGroup();
        }

        public Class<?> getColumnClass(){
            return WallpaperGroup.class;
        }

        public Object getPrototypeCellValue(GraphListModel graphListModel){
            return WallpaperGroup.UNKNOWN;
        }
        
        @Override
        public boolean canGrowBeyondSetSize(){
            return false;
        }
    }, FACES {
        public String getName() {
            return "Face overview";
        }

        public Object getValue(int i, GraphListModel graphListModel) {
            return graphListModel.getGraphGUIData(i).getSymbol() == null ?
                null :
                graphListModel.getGraphGUIData(i).getSymbol().substring(1,
                    graphListModel.getGraphGUIData(i).getSymbol().length()-1)
                                                .split(":")[3].split(",")[0];
        }

        public Class<?> getColumnClass(){
            return String.class;
        }

        public Object getPrototypeCellValue(GraphListModel graphListModel){
            String maxString = "                       ";
            //this was removed for performance reasons with large files
//            for (int i = 0; i < graphListModel.getSize(); i++) {
//                if(graphListModel.getGraphGUIData(i).getSymbol() != null && graphListModel.getGraphGUIData(i).getSymbol().substring(1,
//                    graphListModel.getGraphGUIData(i).getSymbol().length()-1)
//                                                .split(":")[3].split(",")[0].length()>maxString.length()){
//                    maxString = graphListModel.getGraphGUIData(i).getSymbol().substring(1,
//                    graphListModel.getGraphGUIData(i).getSymbol().length()-1)
//                                                .split(":")[3].split(",")[0];
//                }
//            }
            return maxString;
        }
    }, SYMBOL {
        public String getName() {
            return "Delaney-Dress symbol";
        }

        public Object getValue(int i, GraphListModel graphListModel) {
            return graphListModel.getGraphGUIData(i).getSymbol();
        }

        public Class<?> getColumnClass(){
            return String.class;
        }

        public Object getPrototypeCellValue(GraphListModel graphListModel){
            String maxString = "                                                 ";
            //this was removed for performance reasons with large files
//            for (int i = 0; i < graphListModel.getSize(); i++) {
//                if(graphListModel.getGraphGUIData(i).getSymbol() != null && graphListModel.getGraphGUIData(i).getSymbol().length()>maxString.length()){
//                    maxString = graphListModel.getGraphGUIData(i).getSymbol();
//                }
//            }
            return maxString;
        }
    };

    public abstract String getName();

    public abstract Object getValue(int i, GraphListModel graphListModel);

    public abstract Class<?> getColumnClass();

    public abstract Object getPrototypeCellValue(GraphListModel graphListModel);

    public boolean canGrowBeyondSetSize(){
        return true;
    }
}

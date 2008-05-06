/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.DefaultGraph;
import azul.toroidalembedder.graph.general.Graph;

/**
 *
 * @author nvcleemp
 */
public class GraphGUIData {
    private VertexHighlighter highlighter = null;
    private FaceHighlighter faceHighlighter = null;
    private String comment;

    public GraphGUIData() {
        comment = "";
    }

    public GraphGUIData(String comment) {
        this.comment = comment;
    }

    public VertexHighlighter getHighlighter() {
        return highlighter;
    }

    public void setHighlighter(VertexHighlighter highlighter) {
        this.highlighter = highlighter;
    }

    public FaceHighlighter getFaceHighlighter() {
        return faceHighlighter;
    }

    public void setFaceHighlighter(FaceHighlighter faceHighlighter) {
        this.faceHighlighter = faceHighlighter;
    }
    
    public String getComment(){
        return comment;
    }
    
    public String export(){
        StringBuffer buf = new StringBuffer();
        if(faceHighlighter!=null){
            buf.append("facehighlight ");
            buf.append(faceHighlighter.export());
            buf.append(" # ");
        }
        buf.append(comment);
        return buf.toString();
    }

    public static GraphGUIData createGraphGUIData(String s, Graph g){
        GraphGUIData data = new GraphGUIData();
        StringBuffer comment = new StringBuffer();
        String[] parts = s.split("#");
        for (String string : parts) {
            if(string.startsWith("facehighlight ")){
                DefaultFaceHightlighter faceHighlighter = new DefaultFaceHightlighter();
                if(g instanceof DefaultGraph)
                    faceHighlighter.importHighlighting((DefaultGraph)g, string.substring(14));
                data.faceHighlighter = faceHighlighter;
            } else
                comment.append(string);
        }
        data.comment = comment.toString();
        return data;
    }
}

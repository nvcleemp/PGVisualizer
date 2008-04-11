/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

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
    
    public String export(){
        return comment;
    }

}

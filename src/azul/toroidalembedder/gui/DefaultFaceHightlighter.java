/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.Face;
import azul.toroidalembedder.graph.DefaultGraph;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author nvcleemp
 */
public class DefaultFaceHightlighter implements FaceHighlighter {
    
    Map<Face, Color> map = new HashMap<Face, Color>();

    public Color getColorFor(Face f) {
        return map.get(f);
    }

    public void setColor(Face f, Color c) {
        if(c==null)
            map.remove(f);
        else
            map.put(f, c);
    }

    public String export() {
        StringBuffer buf = new StringBuffer();
        for (Face face : map.keySet()) {
            buf.append(face.getIndex());
            buf.append(" ");
            buf.append(map.get(face).getRGB());
            buf.append(" ");
        }
        return buf.toString();
    }
    
    public void importHighlighting(DefaultGraph g, String s){
        Scanner scanner = new Scanner(s);
        List<Face> faces = g.getFaces();
        while(scanner.hasNextInt()){
            map.put(faces.get(scanner.nextInt()), Color.decode(scanner.next()));
        }
    }
    
    public List<Face> getFaces(){
        return new ArrayList<Face>(map.keySet());
    }
}

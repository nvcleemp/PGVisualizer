/*
 * TorusViewTest.java
 *
 * Created on January 10, 2008, 3:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder;

import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Vertex;
import azul.io.FileFormatException;
import azul.io.IOManager;
import azul.toroidalembedder.graph.FundamentalDomain;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public class TorusViewTest {
    
    public static void main(String[] args){
        final JFrame frame = new JFrame();
        final Graph graph = new Graph();
        final Vertex v1 = new Vertex(0, 0);
        graph.addVertex(v1);
        final Vertex v2 = new Vertex(0.5, 0);
        graph.addVertex(v2);
        final Vertex v3 = new Vertex(0, 0.5);
        graph.addVertex(v3);
        graph.addEdge(v1, v2, 0, 0);
        graph.addEdge(v1, v3, 0, 0);
        graph.addEdge(v2, v3, 0, 0);
        graph.addEdge(v3, v1, 1, -1);
        graph.addEdge(v3, v2, 0, 1);
        //graph.addEdge(v3, v2, 1, 0);
        Graph graph1 = null;
        try {
            graph1 = IOManager.readTorGraph("3|0 0;0.5 0;0 0.5|0 1 0 0;0 2 0 0;1 2 0 0;2 0 1 -1;2 1 0 1");
            graph1 = IOManager.readTorGraph("3|0 0;0.5 0;0 0.5|0 1 0 0;0 2 0 0;1 2 0 0;2 0 1 -1;2 1 0 1;2 1 1 0");
            graph1 = IOManager.readTorGraph("1|0 0|0 0 1 0;0 0 0 1");
        } catch (FileFormatException fileFormatException) {
            fileFormatException.printStackTrace();
        }
        final Embedder embedder1 = new SpringEmbedder(graph);
        final Embedder embedder2 = new SpringEmbedderEqualEdges(graph);
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if(e.isShiftDown()){
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                        graph.translate(0.1, 0);
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                        graph.translate(- 0.1, 0);
                    else if(e.getKeyCode() == KeyEvent.VK_UP)
                        graph.translate(0, -0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                        graph.translate(0, 0.1);
                } else if(e.isControlDown()){
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                        v1.translate(0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                        v1.translate(- 0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_UP)
                        v1.translate(0, -0.1, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                        v1.translate(0, 0.1, graph.getFundamentalDomain());
                } else if(e.isAltDown()){
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                        v3.translate(0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                        v3.translate(- 0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_UP)
                        v3.translate(0, -0.1, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                        v3.translate(0, 0.1, graph.getFundamentalDomain());
                } else {
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                        v2.translate(0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                        v2.translate(- 0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_UP)
                        v2.translate(0, -0.1, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                        v2.translate(0, 0.1, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_SPACE)
                        embedder1.embed();
                    else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                        embedder2.embed();
                    else if(e.getKeyCode() == KeyEvent.VK_A)
                        graph.getFundamentalDomain().addToAngle(0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_V)
                        graph.getFundamentalDomain().addToAngle(-0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_NUMPAD6)
                        graph.getFundamentalDomain().addToHorizontalSide(0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_NUMPAD4)
                        graph.getFundamentalDomain().addToHorizontalSide(-0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_NUMPAD8)
                        graph.getFundamentalDomain().addToVerticalSide(0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_NUMPAD2)
                        graph.getFundamentalDomain().addToVerticalSide(-0.1);
                }
            }
            public void keyReleased(KeyEvent e) {
            }
            public void keyTyped(KeyEvent e) {
            }
        });
        frame.setContentPane(new TorusView(graph, -2, -2, 2, 2));
        frame.pack();
        JFrame frame2 = new JFrame("Raw view");
        frame2.setContentPane(new TorusView(new FundamentalDomain(), graph, -2, -2, 2, 2));
        frame2.pack();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame2.setVisible(true);
    }    
}

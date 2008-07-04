/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.delaney;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author nvcleemp
 */
public class FundamentalPatch {
    
    private DelaneySymbol symbol;
    private Map<Chamber, ChamberDelegate> map;

    public FundamentalPatch(DelaneySymbol symbol, Map<Chamber, ChamberDelegate> map) {
        this.symbol = symbol;
        this.map = map;
    }
    
    public int getSize(){
        return map.size();
    }
    
    public ChamberDelegate getDelegateForChamber(Chamber chamber){
        return map.get(chamber);
    }

    public DelaneySymbol getSymbol() {
        return symbol;
    }
    
    public void closeOrbits(){
        boolean stop = false;
        while(!stop){
            stop = true;
            for (int i = 0; i<symbol.getSize(); i++) {
                ChamberDelegate delegate = map.get(symbol.getChamber(i));
                for (int j = 0; j < delegate.neighbours.length; j++)
                    if(delegate.neighbours[j]==null)
                        stop = !tryEdge(symbol.getChamber(i), j) && stop;
            }
        }
    }
    
    private boolean tryEdge(Chamber chamber, int sigma){
        for (int i = 0; i < 3; i++) {
            if(i!=sigma){
                int m = getM(chamber, getRemaining(i, sigma));
                int[] sigmas = {i, sigma};
                int j = 0;
                int count = 0;
                Chamber current = chamber;
                while(map.get(current).neighbours[sigmas[j]]!=null){
                    current = current.sigma(sigmas[j]);
                    j = (j+1)%2;
                    count++;
                }
                if(count==2*m-1){
                    map.get(chamber).setChild(map.get(chamber.sigma(sigma)), sigma);
                    map.get(chamber.sigma(sigma)).setChild(map.get(chamber), sigma);
                    return true;
                }
            }
        }
        return false;
    }
    
    public static class ChamberDelegate{
        private ChamberDelegate[] neighbours = new ChamberDelegate[3];
        private Chamber chamber;

        public ChamberDelegate(Chamber chamber) {
            this.chamber = chamber;
        }
        
        public Chamber getChamber(){
            return chamber;
        }
        
        public void setChild(ChamberDelegate node, int sigma){
            neighbours[sigma]=node;
        }
        
        public int getNumberOfChildren(){
            int count = 0;
            for (int i = 0; i < neighbours.length; i++)
                if(neighbours[i]!=null) count++;
            return count;
        }
    }
    
    public static FundamentalPatch createBFSTree(DelaneySymbol symbol){
        ChamberDelegate root = new ChamberDelegate(symbol.getChamber(0));
        Queue<ChamberDelegate> queue = new LinkedList<ChamberDelegate>();
        Map<Chamber, ChamberDelegate> map = new HashMap<Chamber, FundamentalPatch.ChamberDelegate>();
        map.put(symbol.getChamber(0), root);
        queue.offer(root);
        
        while(!queue.isEmpty()){
            ChamberDelegate current = queue.poll();
            Chamber currentChamber = current.getChamber();
            for(int i=0; i<3; i++){
                if(!map.containsKey(currentChamber.sigma(i))){
                    ChamberDelegate node = new ChamberDelegate(currentChamber.sigma(i));
                    map.put(currentChamber.sigma(i), node);
                    node.setChild(current, i);
                    current.setChild(node, i);
                    queue.offer(node);
                }
            }
        }
        
        return new FundamentalPatch(symbol, map);
    }
    
    public void print(){
        for (int i = 0; i<symbol.getSize(); i++) {
            System.out.print((i+1)+"] -");
            for (int j = 0; j < 3; j++) {
                if(map.get(symbol.getChamber(i)).neighbours[j]==null)
                    System.out.print("|-");
                else
                    System.out.print((symbol.getIndex(map.get(symbol.getChamber(i)).neighbours[j].getChamber()) + 1) + "-");
            }
            System.out.println();
        }

    }
    
    //methods and class to represent this patch as a tree
    //TODO: remove!!!!
    /*
    private static class MyNode implements TreeNode{
        
        private MyNode parent;
        private List<MyNode> children = new ArrayList<MyNode>();
        private ChamberDelegate peer;
        
        @Override
        public String toString(){
            int i = peer.getNumberOfChildren();
            if(i==3)
                return "" + (peer.chamber.getSymbol().getIndex(peer.chamber)+1);
            else if(i==2){
                int j=0;
                while(peer.neighbours[j]!=null) j++;
                return (peer.chamber.getSymbol().getIndex(peer.chamber)+1) + "   s" + j + " : " + (peer.chamber.getSymbol().getIndex(peer.chamber)+1) + "-" + (peer.chamber.getSymbol().getIndex(peer.chamber.sigma(j))+1);
            } else {
                String s = "";
                for (int j = 0; j < peer.neighbours.length; j++) {
                    if(peer.neighbours[j]==null){
                        s += "s" + j + " : " + (peer.chamber.getSymbol().getIndex(peer.chamber)+1) + "-" + (peer.chamber.getSymbol().getIndex(peer.chamber.sigma(j))+1) + "   ";
                    }
                }
                return (peer.chamber.getSymbol().getIndex(peer.chamber)+1) + "   " + s;
            }
        }

        public TreeNode getChildAt(int childIndex) {
            return children.get(childIndex);
        }

        public int getChildCount() {
            return children.size();
        }

        public TreeNode getParent() {
            return parent;
        }

        public int getIndex(TreeNode node) {
            return parent.children.indexOf(node);
        }

        public boolean getAllowsChildren() {
            return false;
        }

        public boolean isLeaf() {
            return children.size()==0;
        }

        public Enumeration children() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }

    public static TreeModel createModel(FundamentalPatch tree){
        MyNode root = new MyNode();
        ChamberDelegate rootChamber = tree.map.get(tree.symbol.getChamber(0));
        root.peer = rootChamber;
        addChildren(root, rootChamber, null);
        return new DefaultTreeModel(root);
    }
    
    private static void addChildren(MyNode currentNode, ChamberDelegate currentChamber, ChamberDelegate parentChamber){
        for (int i = 0; i < 3; i++) {
            if(currentChamber.neighbours[i]!=null && !currentChamber.neighbours[i].equals(parentChamber)){
                MyNode newNode = new MyNode();
                newNode.parent = currentNode;
                newNode.peer = currentChamber.neighbours[i];
                currentNode.children.add(newNode);
                addChildren(newNode, currentChamber.neighbours[i], currentChamber);
            }
        }

    }
    */
    
    //utility methods
    
    private static int getRemaining(int i, int j){
        if(i==0) {
            if(j==1)
                return 2;
            else
                return 1;
        } else if(j==0){
            if(i==1)
                return 2;
            else
                return 1;
        } else {
            return 0;
        }
    }
    
    private static int getM(Chamber chamber, int excluded){
        if(excluded==2)
            return chamber.m(0);
        else if(excluded==0)
            return chamber.m(1);
        else
            return 2;
    }
}

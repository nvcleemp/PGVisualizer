/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azul.delaney;

import java.util.Stack;

/**
 *
 * @author nvcleemp
 */
public class BasicDelaney {

    private int dimension;
    private int[][] sigma;
    private int[][] m;

    public BasicDelaney(int dimension, int[][] sigma, int[][] m) {
        if (sigma.length != m.length || sigma.length == 0 || sigma[0].length != dimension + 1 || m[0].length != dimension) {
            throw new RuntimeException("Illegal structure for Delaney symbol");
        }
        this.dimension = dimension;
        this.sigma = sigma;
        this.m = m;
    }
    
    public BasicDelaney getMinimal(){
        int[] partition = new int[sigma.length];
        int[] temp = new int[sigma.length];
        for (int i = 0; i < partition.length; i++)
            partition[i] = i;
            
        //completely collapse symbol
	for(int i=1; i<partition.length; i++){
            //copy partition to temp
            for(int j=0;j<partition.length;j++)
                temp[j]=partition[j];
		
            //collapse 0 and i
            if(collapseChambers(0, i, temp)) //when successfull
                for(int j=0;j<partition.length;j++) //copy temp to partition
                    partition[j]=temp[j];
	}
        
        //create new symbol from partition
	//labelling
	int newSize = 0;
	int[] old2new = new int[partition.length];
	int[] new2old = new int[partition.length];
	for(int i=0; i<partition.length; i++)
            old2new[i]=-1;
	for(int i=0; i<partition.length; i++){
            if(old2new[partition[i]]==-1){
                old2new[partition[i]]=newSize;
                new2old[newSize]=partition[i];
                newSize++;
            }
            old2new[i]=old2new[partition[i]];
	}
        
        int[][] newM = new int[newSize][dimension];
        for (int i = 0; i < newM.length; i++)
            for (int j = 0; j < newM[i].length; j++)
                newM[i][j] = m[new2old[i]][j];
        
        int[][] newSigma = new int[newSize][dimension+1];
        for (int i = 0; i < newSigma.length; i++)
            for (int j = 0; j < newSigma[i].length; j++)
                newSigma[i][j] = old2new[sigma[new2old[i]][j]];
        
        return new BasicDelaney(dimension, newSigma, newM).makeCanonical();
    }
    
    private boolean collapseChambers(int chamber1, int chamber2, int[] partition){
        for (int i = 0; i < m[chamber1].length; i++)
            if(m[chamber1][i] != m[chamber2][i])
                return false;
        
        if(partition[chamber1]==partition[chamber2]) return true; //already collapsed
        
	//union
	if(partition[chamber1]<partition[chamber2]){
            int oldvalue = partition[chamber2];
            for(int i = 0; i<partition.length; i++)
                if(partition[i]==oldvalue)
                    partition[i]=partition[chamber1];
	} else {
            int oldvalue = partition[chamber1];
            for(int i = 0; i<partition.length; i++)
                if(partition[i]==oldvalue)
                    partition[i]=partition[chamber2];
	}
        
        Stack<Integer> stack1 = new Stack<Integer>();
        Stack<Integer> stack2 = new Stack<Integer>();
        
        stack1.push(chamber1);
        stack2.push(chamber2);
        
        while(!stack1.empty()){
            int current1 = stack1.pop();
            int current2 = stack2.pop();
            for (int j = 0; j < sigma[current1].length; j++) {
                int neighbour1 = sigma[current1][j];
                int neighbour2 = sigma[current2][j];
                
                for (int i = 0; i < m[neighbour1].length; i++)
                    if(m[neighbour1][i]!=m[neighbour2][i])
                        return false;

                //union
                if(partition[neighbour1]<partition[neighbour2]){
                    int oldvalue = partition[neighbour2];
                    for(int i = 0; i<partition.length; i++)
                        if(partition[i]==oldvalue)
                            partition[i]=partition[neighbour1];
                    stack1.push(neighbour1);
                    stack2.push(neighbour2);                    
                } else if(partition[neighbour1]>partition[neighbour2]){
                    int oldvalue = partition[neighbour1];
                    for(int i = 0; i<partition.length; i++)
                        if(partition[i]==oldvalue)
                            partition[i]=partition[neighbour2];
                    stack1.push(neighbour1);
                    stack2.push(neighbour2);
                }
            }
        }
        
        return true;
    }

    public BasicDelaney makeCanonical() {
        int[][] canonicalSigma = sigma;
        int[][] canonicalM = m;
        for (int i = 0; i < sigma.length; i++) {
            int[] relabelling = getCanonicalRelabelling(i);
            int[][] newSigma = new int[sigma.length][dimension+1];
            int[][] newM = new int[m.length][dimension];
            applyRelabelling(relabelling, newSigma, newM);
            if(compare(newSigma, newM)<0){
                canonicalSigma = newSigma;
                canonicalM = newM;
            }
        }
        sigma = canonicalSigma;
        m = canonicalM;
        
        return this;
    }

    private int[] getCanonicalRelabelling(int start) {
        Stack<Integer> stack = new Stack<Integer>();

        stack.push(start);
        int[] relabelling = new int[sigma.length];
        boolean[] visited = new boolean[sigma.length];
        int i = 0;
        relabelling[i++] = start;
        visited[start] = true;

        while (!stack.empty()) {
            int chamber = stack.pop();
            for (int j = 0; j < 3; j++) {
                if (!visited[sigma[chamber][j]]) {
                    visited[sigma[chamber][j]] = true;
                    relabelling[i++] = sigma[chamber][j];
                    stack.push(sigma[chamber][j]);
                }
            }
        }
        
        return relabelling;
    }
    
    private void applyRelabelling(int[] relabelling, int[][] sigma, int[][] m){
        int[] reverse_labelling = new int[relabelling.length];
	for(int i=0; i<relabelling.length; i++)
		reverse_labelling[relabelling[i]] = i;
	
	for(int i=0; i<relabelling.length; i++){
            for (int j = 0; j < m[i].length; j++)
                m[i][j] = this.m[relabelling[i]][j];
            for (int j = 0; j < sigma[i].length; j++)
                sigma[i][j] = reverse_labelling[this.sigma[relabelling[i]][j]];
	}
    }

    public int getDimension() {
        return dimension;
    }

    public int getSize() {
        return sigma.length;
    }

    public int[][] getM() {
        return m;
    }

    public int[][] getSigma() {
        return sigma;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BasicDelaney other = (BasicDelaney) obj;
        if (dimension != other.dimension)
            return false;
        if (getSize() != other.getSize())
            return false;
        if(compare(other.getSigma(), other.getM())!=0)
            return false;
        return true;
    }
    
    private int compare(int[][] sigma, int[][] m){
        for (int j = 0; j < m.length; j++) {
            int i = 0;
            while(i<m[j].length && m[j][i] == this.m[j][i]) i++;
            if(i!=m[j].length) return this.m[j][i] - m[j][i];
        }
        for (int j = 0; j < sigma.length; j++) {
            int i = 0;
            while(i<sigma[j].length && sigma[j][i] == this.sigma[j][i]) i++;
            if(i!=sigma[j].length) return this.sigma[j][i] - sigma[j][i];
        }
        
        return 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + dimension;
        for (int i = 0; i < sigma.length; i++)
            for (int j = 0; j < sigma[i].length; j++)
                hash = 89*hash + sigma[i][j];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[i].length; j++)
                hash = 89*hash + m[i][j];
        return hash;
    }
    
    public DelaneySymbol toDelaneySymbol(){
        DelaneySymbol symbol = new DelaneySymbol(dimension, sigma.length);
        
        for (int i = 0; i < sigma.length; i++) {
            Chamber chamber = symbol.getChamber(i);
            for (int j = 0; j < dimension+1; j++)
                chamber.setSigma(j, symbol.getChamber(sigma[i][j]));
            for (int j = 0; j < dimension; j++)
                chamber.setM(j, m[i][j]);
        }
        
        return symbol;
    }
    
    public boolean isOrientable(){
	int[] orientation = new int[sigma.length];
        Stack<Integer> stack = new Stack<Integer>();
	
	for(int i=0;i<sigma.length;i++)
            orientation[i]=0;
		
	orientation[0]=1;
	stack.push(0);
	
	while(!stack.empty()){
            int chamber = stack.pop();
            for(int j=0; j<3; j++){
                if(orientation[sigma[chamber][j]]==0){
                    orientation[sigma[chamber][j]]=-orientation[chamber];
                    stack.push(sigma[chamber][j]);
                } else if(orientation[sigma[chamber][j]]==orientation[chamber]){
                    return false;
                }
            }
	}
	
	return true;
    }
    
    public BasicDelaney getOrientable(){
        if(isOrientable())
            return this;
        
        int[] orientation = new int[sigma.length];
        Stack<Integer> stack = new Stack<Integer>();
        
        orientation[0] = 1;
        stack.push(0);
        
	while(!stack.empty()){
            int chamber = stack.pop();
            for(int j=0; j<3; j++){
                if(orientation[sigma[chamber][j]]==0){
                    orientation[sigma[chamber][j]]=-orientation[chamber];
                    stack.push(sigma[chamber][j]);
                }
            }
	}
        
        int[][] newSigma = new int[2*sigma.length][dimension];
        int[][] newM = new int[2*m.length][dimension-1];
        
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                newM[i][j]=m[i][j];
                newM[i+m.length][j]=m[i][j];
            }
        }
        
        for (int i = 0; i < sigma.length; i++) {
            for (int j = 0; j < sigma[i].length; j++) {
                int target = sigma[i][j];
                if(orientation[i]==orientation[target]){
                    newSigma[i][j] = target + sigma.length;
                    newSigma[i + sigma.length][j] = target;
                } else {
                    newSigma[i][j] = target;
                    newSigma[i + sigma.length][j] = target + sigma.length;
                }
            }
        }
        
        return new BasicDelaney(dimension, newSigma, newM);
    }
}

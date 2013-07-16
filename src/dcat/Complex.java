
package dcat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * An object containing all information associated with a simplicial complex,
 * and methods for operating on such.
 * @author Brian Green
 */
public class Complex {
    /**
     * Public Constructor
     */
    public Complex(){
        simplices=new ArrayList();
        dimension = -1; 
        dimIndex=null;
        searchTally= null;
    }    
    /**
     * Returns a list of the simplices currently in the complex
     * @return An ArrayList of simplices
     */
    public ArrayList<Simplex> getSimplices(){return simplices;}
    
    /**
     * Recursively adds the passed simplex and all subsimplices to the complex.
     * @param simplex A comma separated string of n simplices identifying an n-1 simplex
     */
    public void addSimplexWithDescription(String simplex){
        ArrayList<String> fullSet = powerset(simplex.split(" ",0));
        Simplex s;
        for(int i=0; i<fullSet.size(); i++){
            s=new Simplex(fullSet.get(i));
            if(!inComplex(s)) {
                simplices.add(s);
            }
            sort();
            determineDimIndex();
        }
    }
    
    public void addSimplex(Simplex s){
        simplices.add(s);
    }
    
    private ArrayList<String> powerset(String[] set){
        ArrayList<String> ps=new ArrayList();
        int size=(int)Math.pow(2,set.length);
        String binRep;//Binary representation of the index
        String sub;//Place to hold working subsets
        for(int i=1;i<size;++i){
            binRep=Integer.toBinaryString(i);
            while(binRep.length()<set.length){
                binRep="0"+binRep;
            }
            sub = new String();
            for(int j=0; j<set.length; ++j){
                if(binRep.charAt(j)=='1') {
                    sub=sub+set[j]+" ";
                }
            }
            ps.add(sub);
        }
        return ps;
    }
    
    public void sort(){
        Collections.sort(simplices, new Comparator<Simplex>(){
            @Override
            public int compare(Simplex a, Simplex b){
                int as=a.describe().size();
                int bs=b.describe().size();
                if(as<bs) {
                    return -1;
                }
                else if(as==bs) {
                    return 0;
                }
                return 1;
                
            }
        });
    }
    
    
    /**
     * Returns a boolean identifying if the passed complex is already in the complex
     * @param simplex A String identifying the simplex to check for
     * @return boolean True if complex contains the simplex, else false
     */
    public boolean inComplex(Simplex s){
            return simplices.equals(s.describe());
    }
    
    public void print(){
        System.out.println("The complex currently contains: ");
        for(int i=0; i<simplices.size(); i++){
            simplices.get(i).print();
        }
    }
    
    /**
     * Returns the simplex at the provided index in the internal data structure
     * @param i An integer index
     * @return Simplex
     */
    public Simplex getSimplexAtIndex(int i){
        return simplices.get(i);
    }
    
    public int getIndexOfFirstSimplexOfDimension(int n){
        if(dimIndex==null){
            determineDimIndex();
        }
        if(n>dimension()){
            return dimIndex[dimIndex.length-1];
        }
        return dimIndex[n];
    }
    
    private void determineDimIndex() {
        dimension=simplices.get(simplices.size()-1).dimension();
        dimIndex = new int[dimension+1];
        int currentDim=-1;
        for(int i=0; i<simplices.size(); i++){
            if(simplices.get(i).dimension()>currentDim){
                dimIndex[++currentDim]=i;   
            }
        }
        searchTally = new int[dimension+1];
    }
    
    public int dimension(){
        if(dimension==-1){
            determineDimIndex();
        }
        return dimension;
    }
    
    /**
     * For each Simplex s in a, if a is not in this Complex, add it. Simplices 
     * are considered to be the same if the inComplex method returns true.
     * @param a 
     * @return 
     */
    public void union(Complex a){
        int maxIndex = a.getSimplices().size();
        Simplex s;
        for(int i=0; i<maxIndex; i++){
            s=a.getSimplexAtIndex(i);
            if(!inComplex(s))
                addSimplex(s);
        }
        sort();
    }
    
    
    public Simplex getRandomUnsearchedSimplexOfDimension(int n){
        if(searchTally[n]==this.numberOfSimplicesInDimension(n)){
            return new Simplex("Q");
        }
        incrementSearchTallyAtIndex(n);
        int first=getIndexOfFirstSimplexOfDimension(n);
        int last=getIndexOfFirstSimplexOfDimension(n+1);
        if(first==last){
            return getSimplexAtIndex(first);
        }
        Simplex candidate=null;
        do{
            candidate=getSimplexAtIndex(getRandomInt(first,last));
        }
        while(candidate.isSearched());
        candidate.setSearched(true);
        return candidate;
    }
    
    public int numberOfSimplicesInDimension(int n){
        if(n==this.dimension())
            return (getSimplices().size()-1)-getIndexOfFirstSimplexOfDimension(n);
        else
            return getIndexOfFirstSimplexOfDimension(n+1)-getIndexOfFirstSimplexOfDimension(n);
    }
    
    
    /**
     * Returns a random number between min(inclusive) and max(exclusive)
     * @param min minimum acceptable integer
     * @param max maximum acceptable integer
     * @return random int
     */
    private int getRandomInt(int min, int max){
        Random rand = new Random();
        int randMax=max-min;
        int ret = rand.nextInt(randMax)+min;
        System.out.println("Random value is: "+ret);
        return ret;
    }   
    
    private void incrementSearchTallyAtIndex(int i){
        searchTally[i]++;
    }
    
    //Instance variables
    private ArrayList<Simplex> simplices;
    private int[] dimIndex;
    private int dimension;
    private int[] searchTally;

}

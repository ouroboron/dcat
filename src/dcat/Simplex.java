/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dcat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
/**
 *
 * @author Brian Green
 */
public class Simplex {
  private String name;
  private int dimension;
  private ArrayList<String> description;
  private boolean searched;
  private int determineDimension(){
      return describe().size()-1;
  }
 
  public Simplex(Simplex s){
      description = new ArrayList();
      description.addAll(s.describe());
      name="";
      dimension=-1;
      searched=false;
  }
  
  public Simplex(String vertices){
      description=new ArrayList(Arrays.asList(vertices.split(" ")));
      Collections.sort(description);
      name ="";
      dimension=-1;
      searched=false;
  }
  
  public Simplex(ArrayList<String> vertices){
      description = vertices;
      Collections.sort(description);
      name="";
      dimension=-1;
      searched = false;
  }
  
  private String getNameFromDescription(){
      String n="";
      for(int i=0; i<description.size(); i++) {
          if(!n.equals("")){
              n=n+" ";
          }
          n=n+description.get(i);
      }
      return n;
  }
  
  public int dimension(){
      if(dimension==-1)
          dimension = determineDimension();
      return dimension;
  }
  public String name(){
      if(name.isEmpty()){
          name=getNameFromDescription();
      }
      return name;
  }
  
  public boolean hasSubsimplex(Simplex s){
      String ss;
      for(int i=0; i<s.describe().size();i++){
          ss=s.describe().get(i);
          if(!this.describe().contains(ss)){
              return false;
          }
      }
      return true;
  }
  
  public boolean contains(Simplex s){
      if(s.dimension()>this.dimension())
          return false;
      int check=s.dimension()+1;
      int pass=0;
      for(int i=0; i<this.dimension()-1; i++){
          for(int j=0; j<check; j++){
              if(s.describe().get(j).equals(this.describe().get(i))){
                  pass++;
              }
              if(pass==check){
                  return true;
              }
          }
      }
      return false;
  }
  
  public ArrayList<Simplex> getSkeleton(int k){
       ArrayList<Simplex> c = new ArrayList();
       int n = this.dimension();
       int size = (int) Math.pow(2, n);
       String binRep;
       String temp;
       for(int i=1; i<size; i++){
           binRep=Integer.toBinaryString(i);
           if((binRep.length()-1)==k){
            while(binRep.length()<n){
                    binRep="0"+binRep;
                }
            temp = new String();
            for(int j=0; j<n; ++j){
                if(binRep.charAt(j)=='1')
                    temp=temp+j+" ";
            }
            c.add(new Simplex(temp));  
        }
       }
       return c;
  }

  public boolean isSearched(){return searched;}
  public void setSearched(boolean b){searched=b;}
  public ArrayList<String> describe(){return description;}
  public void print(){System.out.println(name());}
}

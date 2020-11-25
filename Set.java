import java.util.ArrayList;

/**
 * Class for the set data structure.
 * This class can: 1/ remove the first occurrence of a prompted object in a set, 2/ remove all the occurrences of a prompted object
 * in a set, 3/ compare two sets, 4/ add an object to a set, 5/ return the union of two sets, 6/ return the intersection of two sets.
 * The format of the returned set is a string that represents a set without duplicates.
 */

public class Set {

    private ArrayList set; // arrayList to store the elements of the set


    /**
     * default constructor
     */
    public Set (){
        set = new ArrayList();
    }


    /**
     * Constructor that accepts an arrayList. The elements of the prompted arrayList become the elements of the set
     * @param arr is the arrayList prompted
     */
    public Set (ArrayList arr){
        set = arr;
    }


    /**
     * Method to add an object to the set
     * @param a is the object to add
     */
    public void add(Object a){
        set.add(a);
    }


    /**
     * Method to remove the first occurrence of a given object in the set
     * @param a is the object we wish to remove
     */
    public void remove(Object a){
        for(int i = 0; i < set.size(); i++){
            if(set.get(i)==a){
                set.remove(i);
                break;
            }
        }
    }


    /**
     * Method to remove all the occurrences of a given object in the set
     * @param a is/are the object(s) we wish to remove
     */
    public void removeAll(Object a){
        for(int i = 0; i < set.size(); i++){
            if(set.get(i)==a){
                set.remove(i);
            }
        }
    }


    /**
     * Method to remove duplicates in a set
     */
    public void removeDuplicates(){
        ArrayList container = new ArrayList();
        for(int i = 0; i < set.size(); i++){
            if (!container.contains(set.get(i))){
                container.add(set.get(i));
            }
        }
        set = container;
    }


    /**
     * Method to compare two sets
     * @param A is the first set
     * @param B is the second set
     * @return true if the two sets are identical
     */
    public Boolean compareSets(Set A, Set B){
        ArrayList test = new ArrayList();
        for (int i = 0; i < B.set.size(); i++){ // test if each element of B is in A
            if (A.set.contains(B.set.get(i))){
                test.add(B.set.get(i));
            }
        }
        for (int i = 0; i < A.set.size(); i++){ // test if each element of A is in B
            if (B.set.contains(A.set.get(i))){
                test.add(A.set.get(i));
            }
        }
        if(test.size() == B.set.size()+A.set.size()){
            return true;
        }
        return false;
    }


    /**
     * Method to have the union of two arrayLists
     * @param a is the arrayList prompted
     * @return an arrayList which is the union of this.set and a
     */
    public ArrayList Union (ArrayList a){
        ArrayList ret = new ArrayList();
        for (int i = 0; i < set.size(); i++){
            ret.add(set.get(i));
        }
        for (int i = 0; i < a.size(); i++){
            ret.add(a.get(i));
        }
        return ret;
    }


    /**
     * Method to have the union of two Sets
     * @param A is the first set
     * @param B is the second set
     * @return A union B without duplicates
     */
    public Set union(Set A, Set B){
        Set ret = new Set(A.Union(B.set));
        ret.removeDuplicates();
        return ret;
    }


    /**
     * Method to return the intersection of two sets
     * @param A is the first set
     * @param B is the second set
     * @return A intersection B without duplicates
     */
    public Set intersection(Set A, Set B){
        ArrayList ret = new ArrayList();
        for(int i = 0; i < B.set.size(); i++){
            if(A.set.contains(B.set.get(i))){
                ret.add(B.set.get(i));
            }
        }
        Set r = new Set(ret);
        r.removeDuplicates();
        return r;
    }


    /**
     * Method to return the set in a set format
     * @return the specified set as a string
     */
    public String toString(){
        String ret = "{";
        if(set.size()!=0){
            this.removeDuplicates(); // duplicates count only once
            for(Object a: set){
                ret = ret.concat(a.toString() + ",");
            }
            ret = ret.substring(0,ret.length()-1).concat("}");
        } else {
            ret = ret.concat("}"); // for empty sets {}
        }
        return ret;
    }


    // MAIN
    public static void main(String[] args) {

        Set demo = new Set();
        demo.add("a");
        demo.add("b");
        demo.add(4);
        demo.add(1);
        demo.add(9);

        Set theta = new Set();
        theta.add(1);
        theta.add(4);
        theta.add("a");
        theta.add("c");

        System.out.println("Union: " + demo.union(demo,theta));
        System.out.println("Intersection: " + demo.intersection(demo,theta));
        System.out.println("demo: " + demo.toString());
        System.out.println("theta: " + theta.toString());

    } // END OF MAIN
}

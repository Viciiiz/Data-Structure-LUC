/**
 * This class is a hash map of airplane linkedList. If the tail number of the airplane to be inserted isn't
 * already in the hash map, we insert the airplane at the beginning of the corresponding linkedList.
 */

public class AirplaneRegistry {

    private static final int DEFAULT_CAPACITY = 10; // default length of the Airplane array

    // fields
    private Airplane airplane [] = new Airplane[DEFAULT_CAPACITY]; // airplane array of initial size 10
    private int objectCount = 0;    // count of objects stored in the class
    private int elementCount = 0;   // count of the underlying array elements utilized

    /**
     * Inner class Airplane.
     */
    public class Airplane {
        private String tailNumber;  // key
        private String model;       // value
        private Airplane next;      // next plane in list
        // constructor
        public Airplane (String tailNumber, String model){
            this.tailNumber = tailNumber;
            this.model = model;
            this.next = null;
        }
        // second constructor
        public Airplane (String tailNumber, String model, Airplane next){
            this.tailNumber = tailNumber;
            this.model = model;
            this.next = next;
        }
    }   // end of class Airplane

    /**
     * Method to return the hash function of each key
     * @param tailNumber is the key
     * @return a positive integer between 0 and the length of the array Airplane
     */
    public int hashFunction (String tailNumber){
        return Math.abs(tailNumber.hashCode()) % airplane.length;
    }

    /**
     * Method to check if the hashmap contains an airplane with a given tail number
     * @param tailNumber is the tail number of the airplane
     * @return true if contains
     */
    public boolean contains (String tailNumber){
        Boolean contains = false;
        int bucket = hashFunction(tailNumber);
        Airplane current = airplane [bucket];
        while(current != null){
            if (current.tailNumber == tailNumber){
                contains = true;
            }
            current = current.next;
        }
        return contains;
    }

    /**
     * Method to insert an airplane object in the registry
     * @param tailNumber is the tail number of the airplane
     * @param model is the model of the airplane
     * @return true if the insertion is successful
     */
    public boolean insert(String tailNumber, String model){
        Boolean success = false;
        if(!contains(tailNumber)){
            int bucket = hashFunction(tailNumber);
            if (airplane[bucket] == null) {
                elementCount++; // there is one more element in the array of linkedLists
            }
            airplane[bucket] = new Airplane(tailNumber, model, airplane[bucket]); // LIFO -- the last element inserted is placed at the beginning of the linkedList
            objectCount++; // there is one more object in the data structure
            success = true;
        }
        return success;
    }

    /**
     * Method to display the hash map
     */
    public void display(){
        System.out.printf("\nThe following hash map has %d buckets of which %d are occupied, and %d values.\n", airplane.length, elementCount, objectCount);
        for (int i = 0; i < airplane.length; i++) {
            System.out.printf("\nList at position [%d]: ", i);
            if (airplane[i] == null) {
                System.out.printf("EMPTY!");
            } else {
                Airplane current = airplane[i];
                while (current != null) {
                    System.out.printf("( %s, %s )  ", current.tailNumber, current.model);
                    current = current.next;
                }
            }
        }
    }

    // MAIN
    public static void main(String[] args) {
        AirplaneRegistry test = new AirplaneRegistry();

        // insert airplanes
        test.insert("N4335K", "Piper Archer");
        test.insert("N5399K", "Cessna Skyhawk");
        test.insert("N5266D", "Piper Archer");
        test.insert("N21175", "Cessna Skyhawk");
        test.insert("N52D", "Piper Archer");    // to test for airplanes with same hashFunction

        // display
        test.display();

    } // end of MAIN
}

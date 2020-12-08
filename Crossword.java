import java.io.*;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.*;

public class Crossword {

    /**
     * importWords: 1. read a dictionary file from website
     *              2. put the words in a hashmap according to their length
     */

    private HashMap<Integer, ArrayList<String>> mapWords = new HashMap<>(); // put the words into this hashmap according to their length

    /**
     * method to import the words from the words.txt file into a hashmap according to their length
     */
    public void importWords(){
        StringBuilder str = new StringBuilder();

        try { // append the StringBuilder
            URL url = new URL("https://raw.githubusercontent.com/lgreco/DataStructures/master/CrossWords/src/words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                str.append(inputLine);
                str.append(System.lineSeparator());
            }
            in.close();
        } catch (IOException ex){ // if URL not found
            System.out.println("URL not found.");
        }

        // separate words by line and remove punctuation
        String[] words = str.toString().replaceAll("[^a-zA-Z ]", "").split("\n"); // every word from the file in this String[]

        // remove duplicate and store into an arrayList
        ArrayList <String> wordsList = new ArrayList<>();
        for (int i = 0; i < words.length; i++){
            if(!wordsList.contains(words[i])){
                wordsList.add(words[i]);
            }
        }

        // place words into a hashmap according to their values
    }


    public static void main(String[] args) {
       /* String [] alpha = new String[4];
        alpha[0]="a";
        alpha[1]="b";
        alpha[2]="a";
        alpha[3]="d";

        List<String> beta = Arrays.asList(alpha.clone());
        System.out.println(beta.indexOf("a"));*/





    }

}

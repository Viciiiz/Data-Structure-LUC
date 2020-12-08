import java.io.*;
import java.net.URL;
import java.util.*;

public class Crossword {

    /**
     * importWords: 1. read a dictionary file from website
     *              2. put the words in a hashmap according to their length
     */


    private String [] wordsArray; // array to store all the words from the words.txt file
    private String [][] puzzle; // two dimensional array to take the dimension of the puzzle


    /**
     * method to store the words from the words.txt file into an array using the URL to the file
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

        // separate words by line
        String[] words = str.toString().split("\n"); // every word from the file in this String[]
        wordsArray = words;

    }


    /**
     * Method to build a puzzle. It will select a word at random and place it in the middle row.
     * It will block the cells before and after the first inputted word, and then randomly block other cells of the puzzle.
     * And it will insert words in the puzzle to complete it.
     */
    public void buildCrossword(){

    }


    /**
     * Method to display the puzzle, its clues, its dimensions, the number of words it contains,
     * and the percentage of blocked cells.
     */
    public void showCrossword(){

    }


    /**
     * Method to interact with the user.
     */
    public void driver(){

    }


    // temporary test
    public static void main(String[] args) {

        Crossword demo = new Crossword();
        demo.importWords();
        System.out.println(demo.wordsArray[400000]);

    }

}

import java.io.*;
import java.net.URL;
import java.util.*;

public class Crossword {

    private String [] wordsArray; // array to store all the words from the words.txt file
    private String [][] puzzle; // two dimensional array to take the dimension of the puzzle
    private int numberOfRows; // number of rows in the puzzle
    private int numberOfColumns; // number of columns in the puzzle


    /**
     * method to store the words from the words.txt file into an array using the URL to the file
     */
    public String[] importWords(){
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
        String[] words = str.toString().split("\\r?\\n"); // every word from the file in this String[]
        wordsArray = words;

        return wordsArray;
    }


    /**
     * Method to build a puzzle. It will select a word at random and place it in the middle row.
     * It will block the cells before and after the first inputted word (if any),
     * and then randomly block other cells of the puzzle (if any).
     * And it will insert words in the puzzle to complete it.
     * @param rows is the number of rows in the 2 dimensional array
     * @param columns is the number of columns in the 2 dimensional array
     */
    public void buildCrossword(int rows, int columns){
       // importWords();
        numberOfRows = rows;
        numberOfColumns = columns;

        puzzle = new String[rows][columns];

        // random int from 0 to the number of words in the words.txt file
        Random random = new Random();
        int randInt = random.nextInt(wordsArray.length);

        // find the middle row
        int middleRow;
        double middlePosition = Math.random() * 10;
        if (rows % 2 == 0){
            // if the number of rows is even, the two middle rows will each have 50% chance of being the chosen middle row.
            if (middlePosition < 5)
                middleRow = rows/2 - 1;
            else
                middleRow = rows/2;
        } else {
            middleRow = rows/2;
        }

        // place word at the center of the puzzle
        // loop until the chosen word fits in the row
        int spacing; // number of cells to skip in the row (before placing the middle word)
        while (true) {
            // if word fits and word has more than one character
            spacing = (int) Math.ceil((columns - wordsArray[randInt].length())/2.0); // number of cells to skip in the row
            System.out.println(wordsArray[randInt]);
            System.out.println(wordsArray[randInt].length());
            if (wordsArray[randInt].length() <= columns && wordsArray[randInt].length() > 1) {
                for (int i = 0; i < wordsArray[randInt].length(); i++) {
                    puzzle[middleRow][i + spacing] = Character.toString(wordsArray[randInt].charAt(i)) + "    "; ////////// remove space
                }
                break;
            } else {
                randInt = random.nextInt(wordsArray.length);
            }
        }

        ArrayList <String> wordsUsed = new ArrayList<>();
        wordsUsed.add(wordsArray[randInt]);

        // block the cells before and after the middle word (if any).
        // after blocking those cells, if any cells are left on the middle row, there has to be at least
        // two consecutive cells because we cannot insert a single character word. If there is only one cell,
        // block that cell as well.
        int blockedCells = 0;
        if (spacing == 1){ // cells before the middle word
            puzzle [middleRow][0] = "block";
            blockedCells++;
        } else if (spacing == 2){
            puzzle [middleRow][0] = "block";
            puzzle [middleRow][1] = "block";
            blockedCells+=2;
        } else if (spacing > 2){
            puzzle [middleRow][spacing-1] = "block";
            blockedCells++;
        }
        if (columns - spacing - wordsArray[randInt].length() == 1){ // cells after the middle word
            puzzle [middleRow][spacing + wordsArray[randInt].length()] = "block";
            blockedCells++;
        } else if (columns - spacing - wordsArray[randInt].length() == 2){
            puzzle [middleRow][spacing + wordsArray[randInt].length()] = "block";
            puzzle [middleRow][spacing + wordsArray[randInt].length()+1] = "block";
            blockedCells+=2;
        } else if (columns - spacing - wordsArray[randInt].length() > 2){
            puzzle [middleRow][spacing + wordsArray[randInt].length()] = "block";
            blockedCells++;
        }

        // block the cells of the puzzle randomly.
        // We will avoid blocking the cells of the middle row unless there are more than 2 consecutive cells available.
        //int randomBlock = random.nextInt(((int) 0.25 * rows * columns - (int) 0.05 * rows * columns)) + (int) 0.05 * rows * columns;
        int numberOfCell = rows * columns;
        double percentageOfBlocked = (random.nextInt(16) + 10) / 100.0; // the percentage of blocked cells will be from 10 to 25%
        int numberOfBlockedCells = (int)(numberOfCell * percentageOfBlocked);
        int maxBlockedCellsPerRow = (int)(0.25*rows);
        System.out.println("num = " + numberOfBlockedCells);

        // block random cells in the middle row, if permitted. i.e. if there are enough cells
        for (int i = 1; i < columns-1; i++){
            if (puzzle[middleRow][i]==null){
                if (puzzle[middleRow][i]==null && puzzle[middleRow][i+1]==null && puzzle[middleRow][i-1]==null){
                    double randomBlock = Math.random() * 10; // if this number is below 2.5, block the current cell
                    if (randomBlock<2.5 && blockedCells < numberOfBlockedCells) {
                        puzzle[middleRow][i + 1] = "block";
                        blockedCells++;
                    }
                }
            }
        }
        // block random cells that are not in the middle row
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if(i!=middleRow){
                    if (puzzle[i][j]==null){
                        double randomBlock = Math.random() * 10; // if this number is below 2.5, block the current cell
                        if (randomBlock<2.5 && blockedCells < numberOfBlockedCells){
                            puzzle[i][j] = "block";
                            blockedCells++;
                        }
                    }
                }
            }
        }
        // block the single cells
        for (int i = 0; i < rows; i++) {
            for (int j = 1; j < columns-1; j++) {
                if(puzzle[i][j]==null && puzzle[i][j-1]=="block" && puzzle[i][j+1]=="block"){
                    puzzle[i][j] = "block";
                    blockedCells++;
                }
                if (j == 1 && puzzle[i][j]=="block" && puzzle[i][j-1]==null){
                    puzzle[i][j - 1] = "block";
                    blockedCells++;
                }
                if (j == columns-2 && puzzle[i][j+1]==null && puzzle[i][j]=="block") {
                    puzzle[i][j + 1] = "block";
                    blockedCells++;
                }
            }
        }

        for (String [] s : puzzle){
            System.out.println(Arrays.toString(s));
        }

        System.out.println();
        // insert words in the puzzle.
        // if the number of consecutive cells available in a row is greater than the longest word that can be inserted,
        // put a block in the middle of the consecutive cells and try to insert a word.
        int cells = 0;
        ArrayList <Integer> cell = new ArrayList<>();
        String currentWord = "";
        int next = 0;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                // check for the beginning of available consecutive cells
                if (puzzle[i][j]==null){
                    boolean found = false;
                    while(!found) { // have the word and the correct number of cells
                        cells = 0;
                        next = j;
                        // loop to find the number of consecutive empty cells
                        while (puzzle[i][next]==null && next != columns-1){
                            cells++;
                            next++;
                            if (next == columns-1){ // the last null cell of the row
                                cells++;
                            }
                        }
                        // choose a word at random with the same length as the available cells and check if the word
                        // hasn't been used yet.
                        for (int k = 0; k < wordsArray.length; k++){
                            randInt = random.nextInt(wordsArray.length);
                            currentWord = wordsArray[randInt];
                            if (currentWord.length() == cells && !wordsUsed.contains(currentWord)) {
                                break;
                            }
                        }
                        // if the number of available cells is greater than any available words in the words.txt file,
                        // place a block in the middle of these available cells. The boolean "found" is still false,
                        // which will lead to another loop where the program tries to find another word, until the number
                        // of available cells matches the length of the word.
                        if (currentWord.length() < cells){
                            int middleAvailableCell = (j+cells+j) / 2;
                            puzzle [i][middleAvailableCell] = "block";
                            blockedCells++;
                            System.out.println("added blocked cell on row " + i + " column " + middleAvailableCell);
                        } else if (currentWord.length() == cells){
                            found = true;
                        }
                    }
                    cell.add(cells);
                    // insert word in the available cells
                    int index = 0;
                    for (int k = j; k < j+cells; k++){
                            puzzle[i][k] = Character.toString(currentWord.charAt(index)) + "    "; ////////////// remove space
                            index++;
                    }
                    // add inserted word to the list of word used
                    wordsUsed.add(wordsArray[randInt]);
                    j = next; // skip to the next blocked cell
                }
            }
        }




        System.out.println(blockedCells);
        System.out.println(wordsUsed);
        System.out.println(cell);

       // return puzzle;

    }


    /**
     * Method to display the puzzle, its clues, its dimensions, the number of words it contains,
     * and the percentage of blocked cells.
     */
    public void showCrossword(){
        System.out.println();

        System.out.println("\n\n");

        String grid = "+------".repeat(numberOfColumns) + "+";
        int numbering = 0;
        // number of cells that will contain a numbering in the puzzle
        for (int i = 0; i < numberOfRows; i++){
            for (int j = 0 ; j < numberOfColumns; j++){
                // top row. If the cell under a given cell is blocked, don't number the given cell.
                // if the given cell is blocked, don't number it.
                if(i == 0 && !puzzle[i][j].equals("block")){
                    if (!puzzle[i+1][j].equals("block") || (j!=numberOfColumns-1 && !puzzle[i][j+1].equals("block"))) {
                        numbering++;
                        puzzle[i][j] = puzzle[i][j].concat("1");
                    }
                }
                // first column. If the given cell is blocked, don't number it.
                else if(i!=0 && j==0 && !puzzle[i][j].equals("block")){
                    numbering++;
                    puzzle[i][j] = puzzle[i][j].concat("2");
                }
                // below a blocked cell AND above a blocked cell
                else if ((i!=0 && j!=0) && i != numberOfRows-1 && !puzzle[i][j].equals("block") && (puzzle[i-1][j].equals("block") && puzzle[i+1][j].equals("block"))){
                    // if the cell before the given cell is blocked, number the given cell
                    if (puzzle[i][j-1].equals("block")){
                        numbering++;
                        puzzle[i][j] = puzzle[i][j].concat("3");
                    }
                    // if the cell after the given cell is blocked, don't number the given cell
                }
                // below a blocked cell AND after a blocked cell.
                // number the given cell only once
                else if ((i!=0 && j!=0) && !puzzle[i][j].equals("block") && (puzzle[i][j-1].equals("block") && puzzle[i-1][j].equals("block"))){
                    numbering++;
                    puzzle[i][j] = puzzle[i][j].concat("4");
                }
                // below a blocked cell
                // here, the cell before the given cell is not blocked.
                else if ((i!=0 && j!=0) && !puzzle[i][j].equals("block") && (!puzzle[i][j-1].equals("block") && puzzle[i-1][j].equals("block"))){
                    // if the cell following the given cell is blocked, don't number the given cell
                    if (i!=numberOfRows-1 && (j == numberOfColumns - 1 || !puzzle[i][j + 1].equals("block"))) {
                        numbering++;
                        puzzle[i][j] = puzzle[i][j].concat("5");
                    }
                }
                // after a blocked cell
                else if ((j!=numberOfColumns-1 && i!=0 && j!=0) && !puzzle[i][j].equals("block") && puzzle[i][j-1].equals("block")){
                    numbering++;
                    puzzle[i][j] = puzzle[i][j].concat("6");
                }

            }
        }
        for (String [] s : puzzle){
            System.out.println(Arrays.toString(s));
        }
        System.out.println(numbering);

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
        demo.buildCrossword(5,10);
        demo.showCrossword();

    }

}

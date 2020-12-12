import java.io.*;
import java.net.URL;
import java.util.*;

/***
 * A class to build an American styled crossword with a given number of rows (greater than 0) and columns (greater than 1, because the
 * program can only input words composed of 2 or more characters).
 * Only the across words are actual words. For now, the down words are not defined.
 * The user interacts with the program through the drive() method to create a crossword. The words that are generated in the crossword can be
 * stored in a file named crossword_dictionary.txt if the user wishes to do so.
 * I decided not to remove the punctuations from the chosen words.
 */
public class Crossword {

    private String [] wordsArray; // array to store all the words from the words.txt file
    private String [][] puzzle; // two dimensional array to take the dimension of the puzzle
    private int numberOfRows; // number of rows in the puzzle
    private int numberOfColumns; // number of columns in the puzzle
    private ArrayList<String> wordsInPuzzle = new ArrayList(); // keep track of the words that are in the current puzzle


    /**
     * method to store the words from the words.txt file into an array using the URL to the file.
     * The words are stored in wordsArray to be used in the other methods.
     * @return wordsArray
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
    } // end of importWords()


    /**
     * Method to build a puzzle. It will select a word at random and place it in the middle row.
     * It will block the cells before and after the first inputted word (if any),
     * and then randomly block other cells of the puzzle (if any).
     * And it will insert words in the puzzle with the correct numbering to complete it.
     * The output will be a two dimensional array that will be handled in the showCrossword() method
     * to display they actual crossword.
     * @param rows is the number of rows in the 2 dimensional array
     * @param columns is the number of columns in the 2 dimensional array
     */
    public void buildCrossword(int rows, int columns){
        numberOfRows = rows;
        numberOfColumns = columns;

        // initialize puzzle
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
            if (wordsArray[randInt].length() <= columns && wordsArray[randInt].length() > 1) {
                for (int i = 0; i < wordsArray[randInt].length(); i++) {
                    puzzle[middleRow][i + spacing] = Character.toString(wordsArray[randInt].charAt(i));
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

        // insert words in the puzzle.
        // if the number of consecutive cells available in a row is greater than the longest word that can be inserted,
        // put a block in the middle of the consecutive cells and try to insert a word.
        int cells = 0;
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
                        } else if (currentWord.length() == cells){
                            found = true;
                        }
                    }
                    // insert word in the available cells
                    int index = 0;
                    for (int k = j; k < j+cells; k++){
                            puzzle[i][k] = Character.toString(currentWord.charAt(index));
                            index++;
                    }
                    // add inserted word to the list of word used
                    wordsUsed.add(wordsArray[randInt]);
                    j = next; // skip to the next blocked cell
                }
            }
        }

    } // end of buildCrossword()


    /**
     * Method to display the puzzle, its clues, its dimensions, the number of words it contains,
     * and the percentage of blocked cells.
     */
    public void showCrossword(){
        System.out.println();

        // cells that will contain a numbering in the puzzle
        for (int i = 0; i < numberOfRows; i++){
            for (int j = 0 ; j < numberOfColumns; j++){
                // top row. If the cell under a given cell is blocked, don't number the given cell.
                // if the given cell is blocked, don't number it.
                if(i == 0 && !puzzle[i][j].equals("block")){
                    if ((numberOfRows>2 && !puzzle[i+1][j].equals("block")) || (j!=numberOfColumns-1 && !puzzle[i][j+1].equals("block") && (j != 0 && puzzle[i][j-1].equals("block")))
                        || (j==0)) {
                        puzzle[i][j] = puzzle[i][j].concat("1"); // append string for reference later
                    }
                }
                // first column. If the given cell is blocked, don't number it.
                else if(i!=0 && j==0 && !puzzle[i][j].equals("block")){
                    puzzle[i][j] = puzzle[i][j].concat("2");
                }
                // below a blocked cell AND above a blocked cell
                else if ((i!=0 && j!=0) && i != numberOfRows-1 && !puzzle[i][j].equals("block") && (puzzle[i-1][j].equals("block") && puzzle[i+1][j].equals("block"))){
                    // if the cell before the given cell is blocked, number the given cell
                    if (puzzle[i][j-1].equals("block")){
                        puzzle[i][j] = puzzle[i][j].concat("3");
                    }
                }
                // below a blocked cell AND after a blocked cell.
                // number the given cell only once
                else if ((i!=0 && j!=0) && !puzzle[i][j].equals("block") && (puzzle[i][j-1].equals("block") && puzzle[i-1][j].equals("block"))){
                    puzzle[i][j] = puzzle[i][j].concat("4");
                }
                // below a blocked cell
                // here, the cell before the given cell is not blocked.
                else if ((i!=0 && j!=0) && !puzzle[i][j].equals("block") && (!puzzle[i][j-1].equals("block") && puzzle[i-1][j].equals("block"))){
                    // if the cell following the given cell is blocked, don't number the given cell, unless the given cell has more than
                    // two cells below it.
                    if (i!=numberOfRows-1 && (j == numberOfColumns - 1 || !puzzle[i][j + 1].equals("block"))) {
                        puzzle[i][j] = puzzle[i][j].concat("5");
                    } else if (i<=numberOfRows-2 && !puzzle[i+1][j].equals("block")){
                        puzzle[i][j] = puzzle[i][j].concat("5");
                    }
                }
                // after a blocked cell
                else if ((j!=numberOfColumns-1 && i!=0 && j!=0) && !puzzle[i][j].equals("block") && puzzle[i][j-1].equals("block")){
                    puzzle[i][j] = puzzle[i][j].concat("6");
                }

            }
        }


        // display the puzzle
        String grid = ("+" + "-".repeat(8)).repeat(numberOfColumns) + "+";
        int num = 0;
        for (int i = 0; i < numberOfRows; i++){
            System.out.println(grid);
            System.out.print("|");
            // numbering
            for(int j = 0; j < numberOfColumns; j++){
                if(puzzle[i][j].length()==2){ // if the cell is numbered
                    num++;
                    System.out.print(" " + num + " ".repeat(6 - Integer.toString(num).length()) + " |");
                } else if (puzzle[i][j].length()==5){ // if the cell is blocked
                    System.out.print(" " + "#".repeat(6) + " |");
                } else {
                    System.out.print(" ".repeat(7) + " |");
                }
            }
            // values
            System.out.print("\n|");
            for (int j = 0 ; j < numberOfColumns ; j++){
                if(puzzle[i][j].equals("block")){ // if the cell is blocked
                    System.out.print(" " + "#".repeat(6) + " |");
                } else {
                    System.out.print(" ".repeat(6) + Character.toString(puzzle[i][j].charAt(0)).toUpperCase() + " |");
                }
            }
            System.out.println();
        }
        System.out.println(grid +"\n"); // close the puzzle

        // CLUES
        // get the cells numbering
        // the cells numbering will be used to keep track of the numbering of the displayed clues
        int [][] cellsNum = new int[numberOfRows][numberOfColumns];
        int currentNum = 0;
        for (int i = 0 ; i < numberOfRows; i++){
            for ( int j = 0; j < numberOfColumns; j++){
                if (puzzle[i][j].length()==2){
                    currentNum++;
                    cellsNum[i][j] = currentNum;
                }
            }
        }
        // across
        String word;
        ArrayList<String> wordsUsed = new ArrayList<>();
        System.out.println("Across: ");
        // assess the puzzle to find the across words
        for(int i = 0; i < numberOfRows; i++){
            for (int j = 0; j < numberOfColumns; j++){
                if (puzzle[i][j].length()<3){
                    word = "";
                    int beginningOfWord = j;
                    do{
                        word = word.concat(Character.toString(puzzle[i][j].charAt(0)));
                        j++;
                    }
                    while(puzzle[i][j].length()<3 && j != numberOfColumns-1);
                    if (j == numberOfColumns-1 && puzzle[i][j].length()<3){ // last element of row
                        word = word.concat(Character.toString(puzzle[i][j].charAt(0)));
                    }
                    word = word.toLowerCase().toLowerCase(); // format string
                    String finalWord = word.substring(0,1).toUpperCase() + word.substring(1);
                    wordsInPuzzle.add(finalWord); // add word to arraylist of words used
                    finalWord = (cellsNum[i][beginningOfWord] + ". " + finalWord);
                    wordsUsed.add(finalWord);
                    System.out.println(finalWord);
                }
            }
        }

        System.out.println();

        //down
        ArrayList<String> downWords = new ArrayList<>();
        System.out.println("Down: ");
        for (int i = 0 ; i < numberOfColumns; i++){
            for (int j = 0 ; j < numberOfRows; j++){
                if(j<=numberOfRows-2 && puzzle[j][i].length()<3 && puzzle[j+1][i].length()<3){
                    word = "";
                    int beginningOfWord = j;
                    do{
                        word = word.concat(Character.toString(puzzle[j][i].charAt(0)));
                        j++;
                    } while (puzzle[j][i].length()<3 && j != numberOfRows-1);
                    if (j == numberOfRows-1 && puzzle[j][i].length()<3){ // last element of column
                        word = word.concat(Character.toString(puzzle[j][i].charAt(0)));
                    }
                    word = word.toLowerCase().toLowerCase(); // format string
                    String finalWord = word.substring(0,1).toUpperCase() + word.substring(1);
                    wordsInPuzzle.add(finalWord); // add word to list of words used
                    downWords.add(cellsNum[beginningOfWord][i] + ". " + finalWord);
                }
            }
        }

        // call method "sort" to display the down words in ascending order.
        sort(downWords);

        // dimensions
        System.out.println("\nNumber of rows: " + numberOfRows);
        System.out.println("Number of columns: " + numberOfColumns);
        System.out.println("Total number of cells: " + numberOfRows*numberOfColumns);

        // number of words in the puzzle
        int total = wordsUsed.size() + downWords.size();
        System.out.println("Total number of words: " + total);

        // percentage of blocked cells
        int blockCount = 0;
        for(int i = 0; i < numberOfRows; i++){
            for(int j = 0; j < numberOfColumns; j++){
                if(puzzle[i][j].equals("block")){
                    blockCount++;
                }
            }
        }
        int blockedPercentage = (blockCount*100)/(numberOfRows*numberOfColumns);
        System.out.println("Percentage of blocked cells: " + blockedPercentage + " %\n");

    } // end of showCrossword()


    /**
     * Method that takes an arrayList and sorts its elements according to the numbers in the beginning of each element.
     * This method will be used to sort the down clues generated by the puzzle, whose first characters are integers.
     * @param words is the arrayList to be sorted
     */
    private void sort(ArrayList<String> words){
        // isolate the numbers in another arrayList
        ArrayList<Integer> rank = new ArrayList<>();
        for (int i = 0; i < words.size(); i++){
            int index = 0;
            String current = "";
            while (words.get(i).charAt(index)!='.'){
                current = current.concat(Character.toString(words.get(i).charAt(index)));
                index++;
            }
            rank.add(Integer.parseInt(current));
        }
        // put the ints and value in a hashmap
        HashMap<Integer, String> map = new HashMap<>();
        for(int i = 0 ; i < rank.size(); i++){
            map.put(rank.get(i), words.get(i));
        }

        // sort the keys of the hashmap & print
        TreeMap<Integer, String> sorted = new TreeMap<>();
        sorted.putAll(map);
        for(Map.Entry<Integer,String> entry: sorted.entrySet()){
            System.out.println(entry.getValue());
        }
    } // end of sort()


    /**
     * Method to interact with the user.
     * The user will create a crossword with the number of rows and columns they wish, and add cues to the new words
     * generated into a file if they wish to do so. They can stop the program at anytime.
     */
    public void driver(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Welcome user! \nCreate below your American styled crossword puzzle.\n");
        boolean keepRunning = true;
        // ask user for a valid number of rows ( any number greater than 0).
        // ask user for a valid number of columns (any number greater than 1, because we are only allowed to input words that
        // have more than 2 characters).
        while (keepRunning) {
            System.out.println("Enter your desired row number (any number greater than 0) ~Enter 0 to quit~: ");
            int rows = keyboard.nextInt();
            int columns;
            if (rows == 0) { // if the user decides to quit
                keepRunning = false;
            } else {
                System.out.println("Enter your desired column number (any number greater than 1) ~Enter 0 to quit~: ");
                columns = keyboard.nextInt();
                if (columns == 0) { // if the user decides to quit
                    keepRunning = false;
                } else if (columns == 1){
                    while (columns == 1){
                        System.out.println("One is not a valid input.");
                        System.out.println("Enter your desired column number (any number greater than 1) ~Enter 0 to quit~: ");
                        columns = keyboard.nextInt();
                    }
                }
                if (columns > 1 && rows > 0){
                    importWords();
                    buildCrossword(rows, columns);
                    showCrossword();

                    // ask if user wishes to write cues to file
                    System.out.println("\nWould you like to enter the cues of the displayed words into a file? (Y or N)");
                    String file = keyboard.next();
                    while (!file.equalsIgnoreCase("n") && !file.equalsIgnoreCase("y")){
                        System.out.println("Invalid command. Please enter again.");
                        System.out.println("Would you like to enter the cues of the displayed words into a file? (Y or N)");
                        file = keyboard.next();
                    }
                    if (file.equalsIgnoreCase("y")){
                        obtainCues(); // call method to write cues to file
                    }

                    // ask if user wishes to build another puzzle
                    System.out.println("\n\nWould you like to build another crossword? (Y or N)");
                    String ask = keyboard.next();
                    while (!ask.equalsIgnoreCase("Y") && !ask.equalsIgnoreCase("N")){
                        System.out.println("Invalid command. Please enter again.");
                        System.out.println("Would you like to build another crossword? (Y or N)");
                        ask = keyboard.next();
                    }
                    if (ask.equalsIgnoreCase("N")){
                        keepRunning = false;
                    }
                }
            }
        } // end of creation of puzzle
        System.out.println("Thank you for using this program. See you again!");
    } // end of driver()


    /**
     * Method to ask user to type cues for every word in puzzle
     * This method stores all the words that are already in the file into an arraylist. Then, it stores the words that
     * were generated by the puzzle AND are not in the file into an arraylist. It will then ask the user to enter the cues
     * for every word if they wish to do so.
     */
    public void obtainCues(){
        try {
            File f = new File("crossword_dictionary.txt");
            if (!f.exists()) {
                f.createNewFile(); // if file doesn't exist, create a new file
            }
            // get the words from the file
            ArrayList<String> wordsInFile = new ArrayList<>();
            FileReader fileReader = new FileReader(f);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            if(bufferedReader.readLine()!=null) {
                while (bufferedReader.ready()) {
                    wordsInFile.add(bufferedReader.readLine().split(",")[0]);
                }
            }
            // check if the words used are already in the file
            ArrayList <String> wordsNotInFile = new ArrayList<>();
            for(int i = 0; i < wordsInPuzzle.size(); i++){
                if (!wordsInFile.contains(wordsInPuzzle.get(i))){
                    wordsNotInFile.add(wordsInPuzzle.get(i));
                }
            }
            // input the cues
            FileWriter fw = new FileWriter(f.getAbsoluteFile(), true); // append to file
            BufferedWriter bufferedWriter = new BufferedWriter(fw);
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Enter the corresponding cues: \n");
            for (int i = 0; i < wordsNotInFile.size(); i++){
                System.out.println("(enter 0 to quit) Enter the cues of the following word: " + wordsNotInFile.get(i));
                String cue = keyboard.nextLine();
                if(cue.equals("0")){
                    break;
                }
                bufferedWriter.write(wordsNotInFile.get(i) + ", ");
                bufferedWriter.write(cue + "\n");
            }
            bufferedWriter.close();
        } catch (IOException e){
            System.out.println("Error while handling the file.");
            e.printStackTrace();
        }
    } // end of obtainCues()


    //MAIN
    public static void main(String[] args) {
        Crossword demo = new Crossword();
        demo.driver();
    }

}

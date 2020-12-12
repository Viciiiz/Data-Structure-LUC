# Data-Structure-LUC
A repository for my data structure codes from class.
<html>
<b> AirplaneRegistry.java </b>


- A class that works as a hash map of airplane linkedList. If the tail number of the airplane to be inserted isn't already in the hash map, we insert the airplane at the beginning of the corresponding linkedList based on it's hash function (its key).







<b> Gazillion.java </b>

- Java has a max value of long integer that can be processed and used in operations. This class allows us to add or multiply very large numbers with an "unlimited" number of digits.


- The method recursiveMultiplication that returns an integer uses the Karatsuba algorithm to multiply the two integers prompted. It only works with integers.


- The method RecursiveMultiplication that returns a gazillion uses a principle similar to the Karasuba algorithm. It can only take two numbers with the same number of digits and that number of digits has to be a power of two. After testing, I noticed that the max number of digits of the gazillions that we pass to that method was 2^4 (=16 digits). ... to be updated... 


<b>Set</b>

- A class for the set data structure.

- This class can:
_ 1/ remove the first occurrence of a prompted object in a set
_ 2/ remove all the occurrences of a prompted object in a set
_ 3/ compare two sets
_ 4/ add an object to a set
_ 5/ return the union of two sets
_ 6/ return the intersection of two sets

- The format of the returned set is a string that represents a set without duplicates.


<b>Crossword.java</b>

- A class to create an American styled crossword with customable number of rows and columns, with words that are from a dictionary accessed via URL. The user interacts with the class to create a crossword and have the choice to create a .txt file to type in the clues for every word in the crossword. For now, only the <i>across</i> words are actual words from the dictionary. The <i>down</i> words are not defined yet.
</html>






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
</html>






import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple class to capture really large integer numbers. Each instance of
 * this class is a big integer. Doesn't work with negative numbers for now.
 */
public class Gazillion {

    /** The digits of a very big number */
    private ArrayList<Integer> digits;
    private String value; // the value of the gazillion entered by the user
    private String pureValue = ""; // the value of the gazillion entered by the user with digits only

    /**
     * Constructor with String parameter
     * @param s String representation of big number, e.g., s = "1234...."
     */
    public Gazillion(String s) {

        final char LOWEST_DIGIT = '0';
        digits = new ArrayList<Integer>(s.length()); // Initialize class field to size of given String
        char c;
        int digit;
        boolean numericDigitDetected = false; // flag in case passed argument s contains no numerical digits

        this.value = s; // I added this string to check for negative numbers

        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i); // scan passed argument s, one character at a time.
            if (Character.isDigit(c)) { // is this character a number digit?
                digit = c - LOWEST_DIGIT; // compute its value; should be 0, 1, ..., 9
                digits.add(digit); // add it to the digits ArrayList
                pureValue = pureValue.concat(Character.toString(c)); // value of the gazillion with digits only
                numericDigitDetected = true; // flag that we have at least one numeric digit!
            } // fi
        } // rof

        if (!numericDigitDetected) { // If no numeric digit found in String s, we have a problem!
            throw new IllegalArgumentException();
        }
    } // constructor with string parameter

    /**
     * Prepare a string for printing purposes.
     * @return the String representation of the digits array
     */
    public String toString() {
        ArrayList output = new ArrayList();
        int count = 0;
        // add each digit of the digits arrayList to the output arrayList with a comma every 3 digits starting from the right
        for (int i = digits.size()-1; i >= 0 ; i --){
            if (count % 3 == 0 && count != 0){
                output.add(",");
            }
            output.add(digits.get(i));
            count ++;
        }

        // reverse output arrayList
        Collections.reverse(output);

        // convert output to a string that will be returned
        String ret = "";
        for (int i = 0; i < output.size(); i++){
            ret = ret.concat(output.get(i).toString());
        }

        return ret;
    } // method toString

    /**
     * Method to add two big integers. Usage:
     *   Gazillion a = new Gazillion("123...");
     *   Gazillion b = new Gazillion("987...");
     *   a.add(b); // a now is assigned to a+b
     * From basic arithmetic, remember that for single digit numbers x and y:
     *   sum(x,y) = (x+y) % BASE
     *   carry(x,y) = (x+y) // BASE, where // is an integer division
     * @param gazillion second operate to add (first operand is accessed with operator)
     */
    public void add(Gazillion gazillion) {
        final int BASE = 10; // numerical base; obvious but necessary for carry and sum operations
        int largestSize; // size of the largest of two numbers
        int partialSum; // digit-by-digit sum
        int carry = 0; // initial value of carry

        // find the size of the largest number
        if (digits.size() > gazillion.digits.size()) {
            largestSize = digits.size();
        } else {
            largestSize = gazillion.digits.size();
        }

        // set up a temp ArrayList for the summation. It has space for as many digits as the largest
        // of the two numbers, PLUS one, in case of overflow.
        ArrayList<Integer> sumDigits = new ArrayList<Integer>(largestSize + 1);

        // Add numbers digit-by-digit
        for (int i = 0; i < largestSize; i++) {
            partialSum = least(i) + gazillion.least(i) + carry; // bring carry from previous digit-by-digit operation
            carry = partialSum / BASE; // integer division
            sumDigits.add(partialSum % BASE); // append the sum digit to the temporary ArrayList
        } // rof

        if (carry == 1) {
            sumDigits.add(carry); // overflow carry, if present
        }

        Collections.reverse(sumDigits); // reverse for proper representation
        digits = sumDigits; // copy sum to digits
    } // method add


    /**
     * Method to subtract two positive gazillions
     * @param gazillion second operate to subtract
     */
    public void sub(Gazillion gazillion){
        int partialSub;
        final int BASE = 10;
        int carry = 1;
      //  int a = Integer.parseInt(this.pureValue);
      //  int b = Integer.parseInt(gazillion.pureValue);

        Gazillion greatest = null, smallest = null;
        if(this.pureValue==gazillion.pureValue){ // if they have the same value, return 0
            digits.clear();
            digits.add(0);
        } else {
            // check which gazillion is greater
            if(this.pureValue.length() <= 2 && gazillion.pureValue.length() <= 2){
                int a = Integer.parseInt(this.pureValue);
                int b = Integer.parseInt(gazillion.pureValue);
                if (a<b){
                    greatest = gazillion;
                    smallest = this;
                } else {
                    greatest = this;
                    smallest = gazillion;
                }
            } else if (this.pureValue.length() > 2 && gazillion.pureValue.length() <= 2){
                greatest = this;
                smallest = gazillion;
            } else if (this.pureValue.length() <= 2 && gazillion.pureValue.length() > 2){
                greatest = gazillion;
                smallest = this;
            } else {
                if (this.pureValue.compareTo(gazillion.pureValue) < 0) {
                    greatest = gazillion;
                    smallest = this;
                } else {
                    greatest = this;
                    smallest = gazillion;
                }
            } // end of <<check which is greater>>

            ArrayList<Integer> subDigits = new ArrayList<Integer>(); // store the subtraction result here

            // initialize the elements of the arrayList to 0
            for(int i = 0; i < greatest.pureValue.length(); i++){
                subDigits.add(i, 0);
            }

            // subtract
            for(int i = 0; i < greatest.pureValue.length(); i++){
                // if in current subtraction of two numbers a[] and b[], if a[i] is smaller than b[i], add 10 to a[i] and add a carry=1 to b[i+1]
                // We will subtract the next current subtraction with the carry of that index if any.
                if (greatest.least(i)<smallest.least(i)) {
                    partialSub = greatest.least(i)+BASE-smallest.least(i);
                    subDigits.set(i,partialSub-subDigits.get(i));
                    subDigits.set(i+1, carry);
                } else {
                    // else, just do the subtraction
                    partialSub = greatest.least(i) -smallest.least(i);
                    subDigits.set(i,partialSub-subDigits.get(i));
                }
            }

            Collections.reverse(subDigits); // reverse arrayList
            digits = subDigits;
            // remove the zeros at the beginning of the arrayList
            for(int i = 0 ; i < digits.size(); i++){
                if(digits.get(i)==0){
                    digits.remove(i);
                } else {
                    break;
                }
            }

            // if all zeros, just return 0
            int count = 0;
            for (int i = 0; i < digits.size(); i++){
                if (digits.get(i)==0){
                    count++;
                }
            }
            if (count == digits.size()){
                digits.clear();
                digits.add(0,0);
            }
        }
    } // method sub


    /**
     * Method to obtain the i-th digit
     * @param i position of digit
     * @return digit
     */
    private int least(int i) {
        if ( i>= digits.size()) {
            return 0;
        } else {
            return digits.get(digits.size() - i - 1);
        }
    } // method least

    /**
     * Multiply this number with passed gazillion. Usage:
     *   Gazillion a = new Gazillion(" ... ");
     *   Gazillion b = new Gazillion(" ... ");
     *   a.multiply(b); // a is now a x b
     *
     * Multiplication, though trivial on paper, requires some work as a program.
     * You may want to check out the Karatsuba algorithm for multiplication, to
     * appreciate the complexity involved. However, for this problem you can use
     * the grade school multiplication algorithm. Consider two numbers written
     * as:  a2 a1 a0 (e.g., 214, a2 = 2, a1 = 1, a0 = 4) and
     *         b1 b0 (e.g., 53, b1 = 5, b0 = 3)
     * Multiplying them step by step:
     *         214
     *        x 53
     *    --------
     *         642   .... ( this is 3x214, i.e, b0 x (a2 a1 a0) )
     *       1070    .... ( this is 5x214, i.e., b1 x (a2 a1 a0) )
     *    ========
     *       11342   .... ( this is column-by-column addition )
     *
     * @param gazillion number to multiply this with
     *
     */
    public void multiply(Gazillion gazillion) {

        final int BASE = 10; // numerical base
        int partialMultiplication; // digit-by-digit multiplication
        int carry;
        int indent; // as we multiply, the numbers that are added together are indented to the left by one space
        String currentMult; // will serve as a parameter of the Gazillion(String s) constructor, which will be used when the add(Gazillion gazillion) method is called.
        Boolean thisIsNegative; // check if this gazillion is negative
        Boolean gazillionIsNegative; // check if the gazillion in the parameter is negative
        String alpha;

        // check if the gazillions are negative
        gazillionIsNegative = isNegative(gazillion);
        thisIsNegative = isNegative(this);

        // store the result in the digits of this Gazillion
        Gazillion multiplication = new Gazillion("0");

        // store current multiplication result in this ArrayList
        ArrayList<Integer> current  = new ArrayList<Integer>();

        // gazillion
        // * this
        // = result
        for (int i = 0; i < digits.size(); i++){ // this
            current.clear(); // empty the current multiplication ArrayList
            carry = 0; // initial value of carry

            // we are adding the indent needed for each multiplication
            alpha = "0".repeat(i);
            indent = alpha.length();
            for (int k = 0; k < indent; k++) {
                current.add(0); // add zero to current as the indent
            }

            // multiplication digit by digit
            for (int j = 0; j < gazillion.digits.size(); j++){ // gazillion
                partialMultiplication = least(i) * gazillion.least(j) + carry;
                carry = partialMultiplication / BASE;
                current.add(partialMultiplication % BASE);
            } // we stored the [reversed] result of the current multiplication in the ArrayList current

            // last carry
            if(carry!=0){
                current.add(carry);
            }

            // convert current to a Gazillion
            currentMult = "";
            Collections.reverse(current); // reverse the current multiplication result
            for (int j = 0; j < current.size(); j++){ // convert it to a string
                currentMult = currentMult.concat(Integer.toString(current.get(j)));
            }
            Gazillion omega = new Gazillion(currentMult); // convert to Gazillion

            // add the current multiplication result with the [sum of the] previous multiplications
            multiplication.add(omega);
        }

        // if all the digits in the final result are "0", print only one 0
        int count = 0;
        Boolean isZero = false;
        for(int i = 0; i < multiplication.digits.size(); i++){
            if (multiplication.digits.get(i)==0){
                count++;
            }
        }
        if (count == multiplication.digits.size()){
            multiplication.digits.clear();
            multiplication.digits.add(0);
            isZero = true;
        }

        // add a "-" in front of the output if ONE of the gazillions is negative
        if (!isZero){
            if (gazillionIsNegative && !thisIsNegative){
                System.out.print("-");
            }
            if (!gazillionIsNegative && thisIsNegative){
                System.out.print("-");
            }
        }

        // set final digit after all the multiplication operations
        digits = multiplication.digits;

    } // method multiply


    /**
     * Method to check if a gazillion is negative
     * A number is negative when there is at least one "-" anywhere before the first digit of the number
     * @param gazillion is the gazillion to be checked
     * @return true if negative
     */
    public boolean isNegative(Gazillion gazillion){
        Boolean isNeg = false;
        int countNonDigit = 0;
        if(gazillion.value.contains("-")){
            for (int i = 0; i < gazillion.value.indexOf("-"); i++){
                if(!Character.isDigit(gazillion.value.charAt(i))){
                    countNonDigit++;
                }
            }
            if ( countNonDigit == gazillion.value.indexOf("-")){
                isNeg = true;
            }
        }
        return isNeg;
    }

    /***
     * Method to return a Gazillion using the add() method
     * @param a first Gazillion
     * @param b second Gazillion
     * @return a gazillion that is the sum of the two above
     */
    public Gazillion adder(Gazillion a, Gazillion b){
        a.add(b);
        return a;
    }


    /***
     * Method to return a Gazillion using the sub() method
     * @param a first Gazillion
     * @param b second Gazillion
     * @return a gazillion that is the difference of the two above (positive only)
     */
    public Gazillion subGaz(Gazillion a, Gazillion b){
        a.sub(b);
        return a;
    }


    /***
     * Method to return a Gazillion using the multiply() method
     * @param a first Gazillion
     * @param b second Gazillion
     * @return a gazillion that is the product of the two above
     */
    public Gazillion multiplier(Gazillion a, Gazillion b){
        a.multiply(b);
        return a;
    }

    /**
     * A recursive implementation of the multiplication operation for two Gazillion objects
     * x and y, representing numbers written as:
     *
     *      x = 10^N * a + b
     *      y = 10^N * c + d (N is a power of 2)
     *
     * where N is an int primitive and correspond to the number of digits in x and y;
     * (we assume that x and y have the same number of digits),  and a, b, c, and d
     * are Gazillion objects. As discussed in class, the pseudocode is:
     *
     *    RecursiveMultiplication:
     *     Input: x, y with N and M digits respectively
     *    Output: the product of x and y as a Gazillion object
     *
     *    if ( N==1 ):
     *       return new Gazillion(Integer.toString(x[0]*y[0])
     *    else:
     *      a, b <--- first and second halves of x (both are Gazillion objects)
     *      c, d <--- first and second halves of y (both are Gazillion objects)
     *      ac <--- RecursiveMultiplication(a,c);
     *      ad <--- RecursiveMultiplication(a,d);
     *      bc <--- RecursiveMultiplication(b,c);
     *      bd <--- RecursiveMultiplication(b,d);
     *      return (as Gazillion object) 10^N * ac + 10^(N/2) * (ad+bc) + bd
     *
     *  Notice that for very large values of N (e.g., N=100), the quantities 10^N (ten to the
     *  power N) and 10^(N/2) may be beyond Java's range for long primitives and you may have
     *  to express them as Gazillion objects.
     *
     *
     * @param x Gazillion operand
     * @param y Gazillion operand
     * @return product x*y as a Gazillion object
     */

 //   private ArrayList A = new ArrayList();
   // private ArrayList B = new ArrayList();
   // private ArrayList C = new ArrayList();
 //   private ArrayList D = new ArrayList();

    public Gazillion RecursiveMultiplication(Gazillion x, Gazillion y) {
    /*   // Gazillion product = null;
        // work you code magic here!
        // access the gazillions' values as an arrayList
        ArrayList<Integer> first = x.digits;
        ArrayList<Integer> second = y.digits;

        //System.out.println("hereeee: "+first);

        Gazillion a,b,c,d,ac,ad,bc,bd;
        ArrayList A,B,C,D;
        A = new ArrayList();
      //  System.out.println("A : " + A);
        B = new ArrayList();
        C = new ArrayList();
        D = new ArrayList();

        // find the length of the two gazillions (number of digits)
        int firstLen = first.size(); // N
       // System.out.println(firstLen);
        int secondLen = second.size(); // M

        if(x.digits.size() == 1 && y.digits.size() == 1){
            x.multiply(y);
            return x;
        } else {
            int powerOfTen1 = (int) Math.pow(10,firstLen/2);
            int powerOfTen2 = (int) Math.pow(10,secondLen/2);
            // here, for the first gazillion (because the two gazillions may not have the same length)
            if(firstLen!=1){
                A.clear();
                B.clear();
                //int powerOfTen1 = (int) Math.pow(10,firstLen/2);
                int halfOfX = (int) Math.ceil(firstLen/2.0); // half of the number of digits of the first gazillion
                // assign first half of the first gazillion to A, and the second half of the first gazillion to B
                for(int i = 0; i < firstLen; i++){
                    int current = first.get(i);
                    if(i<halfOfX){
                        A.add(current);
                    } else {
                        B.add(current);
                    }
                } // rof
            }

            // here, for the second gazillion (because the two gazillions may not have the same length)
            if(secondLen!=1){
                C.clear();
                D.clear();
                //int powerOfTen2 = (int) Math.pow(10,secondLen/2);
                int halfOfY = (int) Math.ceil(secondLen/2.0); // half of the number of digits of the second gazillion
                // assign first half of the second gazillion to A, and the second half of the second gazillion to B
                for(int i = 0; i < secondLen; i++){
                    int current = second.get(i);
                    if(i<halfOfY){
                        C.add(current);
                    } else {
                        D.add(current);
                    }
                } // rof
            }

            a = new Gazillion(A.toString());
            System.out.println("addeeedd a: " + a.toString());
            b = new Gazillion(B.toString());
            System.out.println("addeeedd b: " + b.toString());
            c = new Gazillion(C.toString());
            System.out.println("addeeedd c: " + c.toString());
            d = new Gazillion(D.toString());
            System.out.println("addeeedd d: " + d.toString() + "\n\n");

            if(a.digits.size()==1 && b.digits.size()==1 && c.digits.size()==1 && d.digits.size()==1){
                Gazillion storeA, storeB;
                storeA = a;
                storeB = b;
                a.multiply(c);
                ac = a;
                storeA.multiply(d);
                ad = storeA;
                b.multiply(c);
                bc = b;
                storeB.multiply(d);
                bd = storeB;

            } else {
                ac = RecursiveMultiplication(a, c);
                ad = RecursiveMultiplication(a, d);
                bc = RecursiveMultiplication(b, c);
                bd = RecursiveMultiplication(b, d);
            }

            System.out.println("ac: " + ac);
            System.out.println("ad: " + ad);
            System.out.println("bc: " + bc);
            System.out.println("bd: " + bd + "\n\n");

            System.out.println(powerOfTen1);
            System.out.println(powerOfTen2+"\n\n");

            // convert the powers of ten to strings
            String power1 = Integer.toString(powerOfTen1);
            String power2 = Integer.toString(powerOfTen2);

            // gazillions to store the two powers of ten
            Gazillion P1,P2;
            P1 = new Gazillion(power1);
            P2 = new Gazillion(power2);

            // gazillions to store the operations in the return statement
            Gazillion mul1,mul2,mul3,add1,add2,add3;
            P1.multiply(P2);
            mul1 = P1;      // P1 * P2 = mul1
            mul1.multiply(ac);
            mul2 = mul1;    // P1 * P2 * ac = mul2
            ad.add(bc);
            add1 = ad;      // ad + bc = add1
            P2.multiply(add1);
            mul3 = P2;      // P2 * (ad + bc) = mul3
            bd.add(mul3);
            add2 = bd;      // P2 * (ad + bc) + bd = add2
            mul2.add(add2);
            add3 = mul2;    // P1 * P2 * ac + P2 * (ad + bc) + bd = add3

            return add3;
        }
*/

        Gazillion product;
       //length of the gazillion
        int N = x.digits.size();
        Gazillion a,b,c,d,ac,ad,bc,bd,p,q,pq;
        ArrayList AA,BB,CC,DD;
        AA = new ArrayList();
        BB = new ArrayList();
        CC = new ArrayList();
        DD = new ArrayList();

        System.out.println("N: " + N);

        if(N == 1){
            product = multiplier(x,y);
        } else {
            int powerOfTen = (int) Math.pow(10,N/2);
            // separate halves
            for(int i = 0; i < N; i++){
                if(i<(int)Math.ceil(N/2)){
                    AA.add(x.digits.get(i));
                    CC.add(y.digits.get(i));
                } else {
                    BB.add(x.digits.get(i));
                    DD.add(y.digits.get(i));
                }
            }
            System.out.println("AA= "+AA);
            System.out.println("BB= "+BB);
            System.out.println("CC= "+CC);
            System.out.println("DD= "+DD+"\n");

            // put the halves back in gazillions
            a = new Gazillion(AA.toString());
            b = new Gazillion(BB.toString());
            c = new Gazillion(CC.toString());
            d = new Gazillion(DD.toString());
           // System.out.println("a= "+a);
           // System.out.println("b= "+b);
           // System.out.println("c= "+c);
            //System.out.println("d= "+d+"\n");

         /*   // recursive calls
            ac = RecursiveMultiplication(a,c);
            System.out.println("ac= "+ac);
            //a = new Gazillion(AA.toString());
            ad = RecursiveMultiplication(a,d);
            System.out.println("ad= "+ad);
           // c = new Gazillion(CC.toString());
            bc = RecursiveMultiplication(b,c);
            System.out.println("bc= "+bc);
           // b = new Gazillion(BB.toString());
           // d = new Gazillion(DD.toString());
            bd = RecursiveMultiplication(b,d);
            System.out.println("bd= "+bd+"\n");

            // calculations
            Gazillion mul1,mul2,mul3,add1,add2,add3,P1,storeP1;
            P1 = new Gazillion(Integer.toString(powerOfTen));
            System.out.println("P1: "+P1.toString());
            storeP1 = P1;
            P1.multiply(P1);
            System.out.println("P1*P1: " + P1.toString());
            mul1 = P1;      // P1 * P1 = mul1
            mul1.multiply(ac);
            mul2 = mul1;    // P1 * P1 * ac = mul2
            System.out.println("P1 * P1 * ac: " + mul2);
            ad.add(bc);
            add1 = ad;      // ad + bc = add1
            System.out.println("ad + bc: " + add1);
            storeP1.multiply(add1);
            mul3 = storeP1;      // P1 * (ad + bc) = mul3
            System.out.println("P1 * (ad + bc): " + mul3);
            bd.add(mul3);
            add2 = bd;      // P1 * (ad + bc) + bd = add2
            System.out.println("P1 * (ad + bc) + bd: " + add2);
            mul2.add(add2);
            add3 = mul2;    // P1 * P1 * ac + P1 * (ad + bc) + bd = add3
            System.out.println("P1 * P1 * ac + P1 * (ad + bc) + bd: " + add3);

            return add3;*/
            // recursive calls
            ac = RecursiveMultiplication(new Gazillion(AA.toString()),new Gazillion(BB.toString()));
            bd = RecursiveMultiplication(new Gazillion(CC.toString()),new Gazillion(DD.toString()));
            ad = RecursiveMultiplication(new Gazillion(AA.toString()),new Gazillion(DD.toString()));
            bc = RecursiveMultiplication(new Gazillion(BB.toString()),new Gazillion(CC.toString()));
          //  p = adder(a,b);
          //  q = adder(c,d);
           // pq = RecursiveMultiplication(p,q);

            // convert the powerOfTen to a gazillion
            Gazillion P1 = new Gazillion(Integer.toString(powerOfTen));

            // final output
            Gazillion mul1,mul2,mul3,sub1,sub2,add1,add2;
            mul1 = multiplier(P1, P1);      // powerOfTen * powerOfTen
            mul2 = multiplier(mul1, ac);    // powerOfTen * powerOfTen * ac
            //sub1 = subGaz(pq,ac);           // pq - ac
           // sub2 = subGaz(sub1,bd);         // pq - ac - bd
           // mul3 = multiplier(P1, sub2);    // powerOfTen * ( pq - ac - bd )
            //add1 = adder(mul2,mul3);        // powerOfTen * powerOfTen * ac + powerOfTen * ( pq - ac - bd )
            //add2 = adder(add1,bd);          // powerOfTen * powerOfTen * ac + powerOfTen * ( pq - ac - bd ) + bd

            Gazillion addA,addB,addC,mul4;
            addA = adder(ad,bc);  // ad + bc
            mul4 = multiplier(P1,addA); // powerOf10 * ( ad + bc )
            addB = adder(mul2,mul4); // powerOfTen * powerOfTen * ac + powerOf10 * ( ad + bc )
            addC = adder(addB,bd); //powerOfTen * powerOfTen * ac + powerOf10 * ( ad + bc ) + bd

            product = addC;

            // powerOf10*powerOf10*ac + powerOf10*(ad+bc) + bd
            //product = add2;
            //powerOfTen*powerOfTen*ac + powerOfTen*(pq-ac-bd) + bd
        }

       return product;

    } // method RecursiveMultiplication


    /***
     * Method that implements the Karatsuba algorithm for integers
     * @param x first input
     * @param y second input
     * @return the product of x and y
     */
    public int recursiveMultiplication (int x, int y){

        int N = (int) Math.log10(x)+1; //number of digits in x (and y)
        int a, b, c, d;

        if (N == 1){
            return x*y;
        } else {
            int powerOfTen = (int) Math.pow(10,N/2);
            a = x / powerOfTen; // left half of x
            b = x - a*powerOfTen; // right half of x
            c = y / powerOfTen; // left half of y
            d = y - c*powerOfTen; // right half of y


            // recursive calls
            int ac = recursiveMultiplication(a,c);
            //int ad = recursiveMultiplication(a,d);
            //int bc = recursiveMultiplication(b,c);
            int bd = recursiveMultiplication(b,d);
            int p = a+b;
            int q = c+d;
            int pq = recursiveMultiplication(p,q);

            return powerOfTen*powerOfTen*ac + powerOfTen*(pq-ac-bd) + bd;
        }
    }

    /** Driver */
    public static void main(String[] args) {

        // test add()
        Gazillion a = new Gazillion("9798298283984209823944820970792685297384298249828402");
        Gazillion b = new Gazillion("2424245242988922424339283493752037827348728782472787");
        a.add(b);
        System.out.println("\nadd: " + a.toString()+"\n");


        // test multiply()
        Gazillion c = new Gazillion("9798298283984209823944820970792685297384298249828402");
        Gazillion d = new Gazillion("2424245242988922424339283493752037827348728782472787");
        c.multiply(d);
        System.out.println("multiply: " + c.toString()+"\n");


        // test multiply() (for negative numbers: A number is negative when there is at least one "-" anywhere before the first digit of the number)
        Gazillion e = new Gazillion("o-i8uy-7077");
        Gazillion f = new Gazillion("0-i-819");
        System.out.print("multiply (negative): ");
        e.multiply(f);
        System.out.println(e.toString()+"\n");


        // test of lab int recursiveMultiplication()
        Gazillion gazillion = new Gazillion("0");
        System.out.println("Lab (int recursiveMultiplication()): " + gazillion.recursiveMultiplication(9788,9999)+"\n");


        // test subtraction
        Gazillion gazillion1 = new Gazillion("9798298283984209823944820970792685297384298249828402");
        Gazillion gazillion2 = new Gazillion("2424245242988922424339283493752037827348728782472787");
        gazillion1.sub(gazillion2);
        System.out.println("Subtraction: " + gazillion1.toString()+"\n");

        // test Gazillion RecursiveMultiplication()
        System.out.println("\n\n");
        Gazillion omega = new Gazillion("9788");
        Gazillion theta = new Gazillion("9999");
        Gazillion test = new Gazillion("1");
        omega = omega.RecursiveMultiplication(theta,omega);
        System.out.println(omega.toString()+"\n");


    } // main
} // class Gazillion
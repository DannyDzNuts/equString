// Breaks down a given equation into sections and solves them using the PEMDAS rule set.

import java.util.Scanner;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {

        String equStr = ""; // Given equation in string format
        String section = ""; // Stores the currently resolving section of the equation

        boolean finishedFlag = false;

        long result = 0; // Stores the final result of the given equation

        int minIndexA = -1; // Lowest minimum index
        int maxIndexA = -1; // Highest index
        int parOpen = -1; // Index of opening parenthesis
        int parClose = -1; // Index of closing parenthesis
        int intA = 0; // Equation integer storage
        int intB = 0; // Secondary equation integer storage

        // ArrayList<Integer> opFindExp = new ArrayList<Integer> (); // Operator index array of EXPONENT operators within a given range
        ArrayList<Integer> opFindMult = new ArrayList<Integer> (); // Operator index array of MULTIPLICATION operators within a given range
        ArrayList<Integer> opFindDiv = new ArrayList<Integer> (); // Operator index array of DIVISION operators within a given range
        ArrayList<Integer> opFindAdd = new ArrayList<Integer> (); // Operator index array of ADDITION operators within a given range
        ArrayList<Integer> opFindSub = new ArrayList<Integer> (); // Operator index array of SUBTRACTION operators within a given range

        // Equation Input
        // Come up with an input length and content filter later *

        System.out.print ("Equation To Solve: ");
        Scanner input = new Scanner(System.in);
        equStr = input.next();
        input.close();

        maxIndexA = equStr.length();
        minIndexA = 0;

        do {

            // Parenthesis Detection
            if (equStr.contains("(") && equStr.contains(")")) {
                parOpen = par(equStr, minIndexA, maxIndexA)[0];
                parClose = par(equStr, minIndexA, maxIndexA)[1];
            }

            System.out.println("Parenthesis Detection: Check!");

            // Parenthesis Equation Prioritization
            if (parOpen != -1 && parClose != -1) {
                minIndexA = parOpen + 1;
                maxIndexA = parClose - 1;
                section = equStr.substring(parOpen, parClose + 1);
            } else {
                section = equStr.substring(minIndexA, equStr.length());
            }

            System.out.println("Parenthesis Equation Prioritization: Check!");

            // Mathematical Operator Detection (within given range)
            if (section.contains("*")) {
            opFindMult = opArray(equStr, minIndexA, maxIndexA, '*');
            } else if (section.contains("/")) {
            opFindDiv = opArray(equStr, minIndexA, maxIndexA, '/');
            } else if (section.contains("+")) {
            opFindAdd = opArray(equStr, minIndexA, maxIndexA, '+');
            } else if (section.contains("-")) {
            opFindSub = opArray(equStr, minIndexA, maxIndexA, '-');
            } else if (!opFindMult.isEmpty() && !opFindDiv.isEmpty() && !opFindAdd.isEmpty() && !opFindSub.isEmpty()) {
                System.out.println("No valid operators found in equation.");
                System.exit(0);
            }

            System.out.println("Mathematical Operator Detection: Check!");

            // Resolving Equation...

            // equStr = expReturn(intA, intB, opFindExp, minIndexA, maxIndexA, equStr);
            equStr = multReturn(intA, intB, opFindMult, minIndexA, maxIndexA, equStr);
            equStr = divReturn(intA, intB, opFindDiv, minIndexA, maxIndexA, equStr);
            equStr = addReturn(intA, intB, opFindAdd, minIndexA, maxIndexA, equStr);
            equStr = subReturn(intA, intB, opFindAdd, minIndexA, maxIndexA, equStr);
            equStr = parReturn(intA, intB, parOpen, parClose, minIndexA, maxIndexA, equStr); // LOOKS FOR OPERATORS BETWEEN PAR, Otherwise resolves

            System.out.println("Equation Resolution: Check!");

            // Detecting Remaining Operators...

            if (!equStr.contains("*") || !equStr.contains("/") || !equStr.contains("+") || !equStr.contains("-")) {
                finishedFlag = true;
                equStr = equStr.replace("(", "");
                equStr = equStr.replace(")", "");
                result = Long.parseLong(equStr);
            }

            System.out.println("Remaining Operators Detection: Check!");

        } while (finishedFlag == false);

        System.out.println("Result: " + result);

    }

    public static int[] par(String equStr, int minIndexA, int maxIndexA) {
        int[] par = new int[2];
         for (int i = minIndexA; i < equStr.length(); i++) {
             if (equStr.charAt(i) == ')') {
                 par[1] = (i);
                 maxIndexA = i;
                 break;
             }
         }

         for (int i = maxIndexA; i >= 0; i--) {
            if (equStr.charAt(i) == '(') {
                par[0] = (i);
                break;
            }
            
         }
        return par;
    }
    public static ArrayList<Integer> opArray(String equStr, int minIndexA, int maxIndexA, char searchOp) {
        ArrayList<Integer> opMap = new ArrayList<Integer>();

        for (int i = minIndexA; i < maxIndexA; i++) {
            if (equStr.charAt(i) == searchOp) {
                opMap.add(i);
            }
        }
        return opMap;
    }
    public static int add(int inta, int intb) {
        return (inta + intb);
    }
    public static int sub(int inta, int intb) {
        return (inta - intb);
    }
    public static int div(int inta, int intb) {
        return (inta / intb);
    }
    public static int mult(int intA, int intB) {
        return (intA * intB);
    }
    public static int intReverse(int intA) {
        int result = 0;
        if (intA > 10) {
            while(intA != 0) {
                int remainder = intA % 10;
                result = result * 10 + remainder;
                intA = intA / 10;
            }
        } else {
        result = intA;
    }
        return result;
    }
    public static String expReturn(int intA, int intB, ArrayList<Integer> opFindExp, int minIndexA, int maxIndexA, String equStr) {
        String section = "";
        String tempResult = "";
        

        if (!opFindExp.isEmpty()) {
            for (int i = 0; i <= opFindExp.size() - 1; i++) { // Loops through the stored indeces of the multiplication operators within a given range
                for (int b = opFindExp.get(i) - 1; b >= minIndexA; b--) { // Loops backwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the first int
                        intA = ((intA * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                for (int b = opFindExp.get(i) + 1; b <= maxIndexA; b++) { // Loops forwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the second int
                        intB =((intB * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                intA = intReverse(intA);
                section = (intA + "*" + intB);
                tempResult = Integer.toString(mult(intA, intB));
                equStr = equStr.replace(section, tempResult);
            }
        }
        return equStr;
    }
    public static String multReturn(int intA, int intB, ArrayList<Integer> opFindMult, int minIndexA, int maxIndexA, String equStr) {
        String section = "";
        String tempResult = "";
        

        if (!opFindMult.isEmpty()) {
            for (int i = 0; i <= opFindMult.size() - 1; i++) { // Loops through the stored indeces of the multiplication operators within a given range
                for (int b = opFindMult.get(i) - 1; b >= minIndexA; b--) { // Loops backwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the first int
                        intA = ((intA * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                for (int b = opFindMult.get(i) + 1; b <= maxIndexA; b++) { // Loops forwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the second int
                        intB =((intB * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                intA = intReverse(intA);
                section = (intA + "*" + intB);
                tempResult = Integer.toString(mult(intA, intB));
                equStr = equStr.replace(section, tempResult);
            }
        }
        return equStr;
    }
    public static String divReturn(int intA, int intB, ArrayList<Integer> opFindDiv, int minIndexA, int maxIndexA, String equStr) {
        String section = "";
        String tempResult = "";
        

        if (!opFindDiv.isEmpty()) {
            for (int i = 0; i <= opFindDiv.size() - 1; i++) { // Loops through the stored indeces of the multiplication operators within a given range
                for (int b = opFindDiv.get(i) - 1; b >= minIndexA; b--) { // Loops backwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the first int
                        intA = ((intA * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                for (int b = opFindDiv.get(i) + 1; b <= maxIndexA; b++) { // Loops forwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the second int
                        intB =((intB * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                intA = intReverse(intA);
                section = (intA + "/" + intB);
                tempResult = Integer.toString(div(intA, intB));
                equStr = equStr.replace(section, tempResult);
            }
        }
        return equStr;
    }
    public static String addReturn(int intA, int intB, ArrayList<Integer> opFindAdd, int minIndexA, int maxIndexA, String equStr) {
        String section = "";
        String tempResult = "";
        

        if (!opFindAdd.isEmpty()) {
            for (int i = 0; i <= opFindAdd.size() - 1; i++) { // Loops through the stored indeces of the multiplication operators within a given range
                for (int b = opFindAdd.get(i) - 1; b >= minIndexA; b--) { // Loops backwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the first int
                        intA = ((intA * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                for (int b = opFindAdd.get(i) + 1; b <= maxIndexA; b++) { // Loops forwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the second int
                        intB =((intB * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                intA = intReverse(intA);
                section = (intA + "+" + intB);
                tempResult = Integer.toString(add(intA, intB));
                equStr = equStr.replace(section, tempResult);
            }
        }
        return equStr;
    }
    public static String subReturn(int intA, int intB, ArrayList<Integer> opFindSub, int minIndexA, int maxIndexA, String equStr) {
        String section = "";
        String tempResult = "";
        

        if (!opFindSub.isEmpty()) {
            for (int i = 0; i <= opFindSub.size() - 1; i++) { // Loops through the stored indeces of the subtraction operators within a given range
                for (int b = opFindSub.get(i) - 1; b >= minIndexA; b--) { // Loops backwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the first int
                        intA = ((intA * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                for (int b = opFindSub.get(i) + 1; b <= maxIndexA; b++) { // Loops forwards through the equation until it runs into another operator
                    if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the second int
                        intB =((intB * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    } else {
                        break;
                    }
                }
                intA = intReverse(intA);
                section = (intA + "+" + intB);
                tempResult = Integer.toString(add(intA, intB));
                equStr = equStr.replace(section, tempResult);
            }
        }
        return equStr;
    }
    public static String parReturn(int intA, int intB, int parOpen, int parClose, int minIndexA, int maxIndexA, String equStr) {
        String tempResult = "";
        String section = "";
        
        boolean hasOperatorA = false;
        boolean isEndPar = false;
        boolean isPar = false;

        if (equStr.contains("(") && equStr.contains(")")) {
            isPar = true;
            for (int b = parOpen - 1; b >= minIndexA; b--) { // Loops backwards through the equation until it runs into another operator
                System.out.println("Current Index: " + b);
                if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the first int
                    intA = ((intA * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    System.out.println("intA: " + intA);
                } else if (equStr.charAt(b) == '*') {
                    hasOperatorA = true;
                    continue;
                } else {
                    break;
                }
            }

            for (int b = parClose + 1; b <= maxIndexA; b++) { // Loops forwards through the equation until it runs into another operator

                if (Character.isDigit(equStr.charAt(b))) { // Psuedo math to add the new number to the second int
                    intB =((intB * 10) + Integer.parseInt(String.valueOf(equStr.charAt(b))));
                    System.out.println("intB: " + intB);
                } else if (equStr.charAt(b) == '*') {
                    continue;
                } else {
                    if (equStr.charAt(b) == ')') {
                        isEndPar = true;
                    }
                    break;
                }
            }
            
            if (!hasOperatorA) {
                if (isEndPar) {
                    section = (intA + "(" + intB + ")");
                } else {
                    section = (intA + "(" + intB);
                } 
            } else {
                if (isEndPar) {
                    section = (intA + "*(" + intB + ")");
                } else {
                    section = (intA + "*(" + intB);
                }
            }

            tempResult = Integer.toString(mult(intA, intB));
            System.out.println("intA: " + intA + "\nintB: " + intB);
            System.out.println("Temp Result: " + tempResult);
            System.out.println("Found Parenthesis: " + isPar);
            equStr = equStr.replace(section, tempResult);

        } else {
            return equStr;
        }
        return equStr;
    }
}
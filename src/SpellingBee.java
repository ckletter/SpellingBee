import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // Calls recursive method to generate all possible permutations of letters
        tryLetter("", letters);
    }

    /**
     * Recursive method
     * Finds all substrings and permutations of letters and adds each to the ArrayList words
     * @param newWord
     * @param initialWord
     */
    public void tryLetter(String newWord, String initialWord) {
        // Base case - all letters have been removed from the initialWord string and added to newWord
        if (initialWord.isEmpty()) {
            // Adds final permutations with all letters used to ArrayList words
            words.add(newWord);
            return;
        }
        // Loops through each letter left in the string initialWord
        // Tries adding each of the letters as the next letter in the permutation of newWord
        for (int i = 0; i < initialWord.length(); i++) {
            // Adds the letter at index i in initialWord to a new word permutation
            String wordPermutation = newWord + initialWord.charAt(i);
            // Recursive call - passes the new word permutation with the added letter from initialWord
            // Removes that added letter from the initialWord string and passes in the updated initialWord string
            tryLetter(wordPermutation, (initialWord.substring(0,i) + initialWord.substring(i + 1)));
        }
        // Adds permutation of letters to ArrayList words
        words.add(newWord);
    }
    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Calls recursive method to sort the ArrayList words using merge sort
        words = mergeSort(words, 0, words.size() - 1);
    }

    /**
     * Sorts the ArrayList words alphabetically using the sorting algorithm Merge Sort
     * Recursively breaks the arrays into smaller array lists until there are many arrays of index one
     * Calls method merge() which combines and simultaneously sorts the smaller array lists
     * @param words
     * @param low
     * @param high
     */
    public ArrayList<String> mergeSort(ArrayList<String> words, int low, int high) {
        // Base case - the array lists have been broken into sizes of one element
        if (high - low == 0) {
            // Creates a new ArrayList and adds the element at index low to it
            ArrayList<String> newArr = new ArrayList<String>();
            newArr.add(words.get(low));
            // returns the ArrayList of size one with element added
            return newArr;
        }
        // Finds the middle of the ArrayList
        int med = (high + low) / 2;
        // Splits the ArrayList into two smaller ArrayLists
        // Creates one to the left of the middle index and one to the right of the middle index
        ArrayList<String> arr1 = mergeSort(words, low, med);
        ArrayList<String> arr2 = mergeSort(words, med + 1, high);
        // Sorts these smaller ArrayLists, combines into one big ArrayList using the merge() function and returns it
        return merge(arr1, arr2);
    }

    /**
     * Given two ArrayLists, sorts them into one larger ArrayList
     * returns the sorted ArrayList
     * @param arr1
     * @param arr2
     */
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        // Creates one index for each ArrayList passed in
        // Increments index each time an element from that ArrayList added to bigger ArrayList
        int i = 0, j = 0;

        ArrayList<String> newArr = new ArrayList<String>();
        int sizeOne = arr1.size();
        int sizeTwo = arr2.size();

        // Keeps adding elements and sorting into larger ArrayList until one of the ArrayLists has reached its final index
        while (i < sizeOne && j < sizeTwo)
        {
            // Gets the strings at the i index of the first ArrayList and the j index of the second Arraylist
            String stringOne = arr1.get(i);
            String stringTwo = arr2.get(j);
            // If the string in the first ArrayList is smaller, adds it to the larger ArrayList and increments the corresponding index
            if (stringOne.compareTo(stringTwo) < 0) {
                newArr.add(stringOne);
                i++;
            }
            // If the string in the second ArrayList is smaller, adds it to the larger ArrayList and increments the corresponding index
            else {
                newArr.add(stringTwo);
                j++;
            }
        }
        // Loops through each index left in the first ArrayList and adds them all to the larger ArrayList (we know it's in sorted order)
        while (i < sizeOne)
        {
            newArr.add(arr1.get(i));
            i++;
        }
        // Loops through each index left in the second ArrayList and adds them all to the larger ArrayList (we know it's in sorted order)
        while (j < sizeTwo)
        {
            newArr.add(arr2.get(j));
            j++;
        }
        // Returns the larger and fully sorted ArrayList
        return newArr;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // Loops through each word in the ArrayList and checks if it is in the dictionary
        for (int i = 0; i < words.size(); i++)
        {
            // Uses binary search to check if the word is in the dictionary
            // If the word is not in the dictionary, removes it from the ArrayList
            if (!binarySearch(DICTIONARY, words.get(i), 0, DICTIONARY_SIZE - 1)) {
                words.remove(i);
                i--;
            }
        }
    }

    /**
     * Implements binary search given a sorted string list and a target
     * Recursively cuts the indexes of the string list in half until the word is found (or not)
     * @param stringList
     * @param target
     * @param low
     * @param high
     */
    public boolean binarySearch(String[] stringList, String target, int low, int high) {
        // Finds middle index of the string list
        int med = (high + low) / 2;
        String stringMed = stringList[med];
        // Base case - if the string list has been narrowed to only having one element left
        if (high == med) {
            // Returns true if that final element is the target, false otherwise
            return (target.compareTo(stringMed) == 0);
        }
        // Returns true if the element at the middle index is the target string
        if (target.compareTo(stringMed) == 0)
        {
            return true;
        }
        // If the target string is less than the string at the middle index,
        // narrows the string list to be searched to only the left half
        if  (target.compareTo(stringMed) < 0) {
            // Recursive method - calls binary search again but with upper bound limited to not redundantly search right half
            return binarySearch(stringList, target, low, med - 1);
        }
        // If the target string is greater than the string at the middle index,
        // narrows the string list to be searched to only the right half
        // Recursive method - calls binary search again but with lower bound to not redundantly search left half
        return binarySearch(stringList, target, med + 1, high);
    }
    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Dictionary {
    private LinkedList<String>[] hashedWords;

    private static int MODULO = 12809;
    private static String filename = "words_alpha.txt";
    private static String searchterm = "munster";
    int counter = 0;

    public static void main(String[] args)throws IOException {
        Dictionary dic = new Dictionary();
    }

    public Dictionary() throws IOException {
        hashedWords = new LinkedList[MODULO];
        int collisions = 0;
        int biggestSize = 0;
        for (int i = 0; i < MODULO; i++) {
            hashedWords[i]  = new LinkedList<>();
        }
        readFile();

        for(int i = 0; i< hashedWords.length; i++){
            int size = hashedWords[i].size();
            if(size > 1) collisions += size;
            if(size > biggestSize){
                biggestSize = size;
            }
            System.out.println(hashedWords[i].toString());
        }
        System.out.println("Wordcount: " + counter);
        System.out.println("There are "+ collisions + " collisions in the HashTable");
        System.out.println("The longest linked list has " + biggestSize + " chains");

        //testing the lookup method:
        System.out.println("");

        //exercise 2
        String[] words = lookup(searchterm);
        if(words.length > 1) {
            System.out.println("All the words at the same position as " + searchterm + ":");
            for (String s : words) {
                System.out.println(s);
            }
        } else {
            System.out.println("There are no other words at the searched position");
        }

        //exercise 3 + isPermutation
        System.out.println("");
        int pos = calculatePos(searchterm);
        System.out.println("List of permutations:");
        for(int i = 0; i < hashedWords[pos].size(); i++){
            String compare = hashedWords[pos].get(i);
            isPermutation(searchterm, compare);
            if(isPermutation(searchterm, compare)){
                System.out.println(compare);
            }
        }

    }

    public void readFile() throws IOException{
        String textLine;
        FileReader reader = new FileReader(filename);
        BufferedReader buffer = new BufferedReader(reader);

        while (buffer.ready()) {
            textLine = buffer.readLine();
            //if the words are longer than this, our calculation for the hashValue before modulo is too large
            if(textLine.length() == 7) {

                counter++;
                /*long hashValue = calculatePos(textLine);*/
                hashedWords[calculatePos(textLine)].add(textLine);
                /*System.out.println(textLine);*/
            }
        }
    }

    public int calculatePos(String line){
        long hashValue = 0;
        line = line.trim().toLowerCase();
        char[] ca = line.toCharArray();
        Arrays.sort(ca); //sorts the char array by ascii values and therefore alphabetically as well.
        for(int i = 0; i<ca.length; i++){
            double temp = Math.pow(26, (double)i); //cast necessary for math.pow
            hashValue = hashValue + (long)temp*((long)ca[i]-96);
        }
        return (int)(hashValue%MODULO);
    }

    public String[] lookup(String search){
        int pos = calculatePos(search);
        int size = hashedWords[pos].size();
        String[] words = new String[size];
        for(int i = 0; i < size; i++){
            words[i] = hashedWords[pos].get(i);
        }
        return words;
    }

    public boolean isPermutation(String original, String compare){
        boolean bool = false;
        char[] originalA = original.toCharArray();
        Arrays.sort(originalA);
        char[] compareA = compare.toCharArray();
        Arrays.sort(compareA);

        for(int i= 0; i < originalA.length; i++){
            if(originalA[i] != compareA[i]) return bool;
        }
        bool = true;
        return bool;
    }

//notes for the report:
    /*
    * Words couldn't be longer than 13 letters. -> overflow of the long variable
    * for 100 array positions: longest chain 2600
    * for 60501 array positions the longest chain was 17 (336065 words) and there were only a few empty spots
    *
    * with only the seven letter words, there are roughly 40k words, we managed to get the smallest chain to be 16
    * with an array size of 12809 (just trying out different prime numbers in that range until we got this one)
    *
    * lookup method takes a word and calculate the position for that word the same way that positions always get
    * calculated, then we can take all the words in the linked list on that position and put them into the array
    *
     * */
}


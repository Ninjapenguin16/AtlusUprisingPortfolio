import java.io.File;
import java.util.*;

public class day{

    public static void main(String[] args) throws Exception{

        Scanner file = new Scanner(new File("day.dat")); // Reads the whole file into the "file" scanner

        do{
            Scanner sentance = new Scanner(file.nextLine()); // Reads each line from the file scanner into a "sentance" scanner

            while(sentance.hasNext()){
                String TempWord = sentance.next(); // Gets next word to be translated
                String TempPigLatin = ""; // Sets up and clears TempPigLatin for new translation

                //Start word convert to piglatin
                for(int i = TempWord.length() - 2; i > -1; i--) // Reverses all characters except for last character
                    TempPigLatin += TempWord.charAt(i);

                TempPigLatin += TempWord.charAt(TempWord.length() - 1); // Moves last character into the back
                TempPigLatin += "ay"; // adds "ay" to the end of the word
                //end of word convert to piglatin

                System.out.print(TempPigLatin + " "); // Prints out the translated piglatin word and a space for next word
            }

            System.out.println(""); // Prints to a new line when end of sentance is reached

            sentance.close(); // Closes the sentance scanner 

        } while(file.hasNextLine()); // Repeat while there is another line in the file

        file.close(); // Closes the file scanner

    }

}
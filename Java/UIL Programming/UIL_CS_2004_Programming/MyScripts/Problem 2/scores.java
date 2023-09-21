import java.io.File;
import java.net.Socket;
import java.util.*;

public class scores {
	
	public static void main2(String[] args)throws Exception{ // This was the first attempt, while it workes it is long for no reason

        char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}; // List of numbers to check string later

        Scanner in = new Scanner(new File("scores.dat")); // tells scanner to load scores.dat into the 'in' scanner object

        int CurChar = 0; // Sets the first character to be 0 (or the first character in the string) by default
        String TempNumber = ""; // Sets there to be no TempNumber by default

        while(in.hasNextLine()){ // Will continue as long as there is another line to read in the scores.dat file
            String CurLine = in.nextLine(); // Gets next line in the scores.dat file
            boolean ThatsANumber = false; //Sees if last character checked was a number
            boolean Once = true; // Used for first pass allowance
            while(ThatsANumber || Once){
                Once = false; // Turns off the first pass allowance
                ThatsANumber = false; // Resets value from last loop run
                for(int i = 0; i < 10; i++) //Indexes through all 10 possible number characters
                    if(CurLine.charAt(CurChar) == numbers[i]) //Checks if current character is a number
                        ThatsANumber = true; // If number, go through loop again
                
                if(ThatsANumber) // Goes to next character in the string if the last was a number
                    CurChar++;
                else // Else break the loop (Not really needed since conditions would return false anyways but it's nice)
                    break;
            }

            for(int i = 0; i < CurChar; i++) // Copies the number from the string and moves it into a temp string
                TempNumber += CurLine.charAt(i);

            CurLine = CurLine.substring(CurChar + 1); // Deletes the number and the space in the line, leaving only the name

            CurChar = 0; // Resets CurChar count for next line read

            System.out.println(CurLine + " " + TempNumber); // Constructs final line output
            
            TempNumber = ""; // Resets TempNumber to hold next number

        }
        
    }

    public static void main(String[] args)throws Exception{ //"better" but can only handle 10 lines unline the code above, an arraylist in the place of the arrays would fix this

        int[] ScoreNumbers = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] ScoreNames = {"", "", "", "", "", "", "", "", "", ""};

        Scanner in = new Scanner(new File("scores.dat"));

        int lines = 0;

        for(int i = 0; in.hasNextLine(); i++){
            ScoreNumbers[i] = in.nextInt();
            ScoreNames[i] = in.next();
            lines++;
        }

        for(int i = 0; i < lines; i++)
            System.out.println(ScoreNames[i] + " " + ScoreNumbers[i]);

    }



}
import java.io.*;
import java.util.*;

public class golf{

    public static void main(String[] args) throws Exception{

        int P1Score = 1;
        int P2Score = 1;
        int TempPoints = 0;
        int TotRolls = 0;
        boolean CurPlayer = false;

        Scanner in = new Scanner(new File("golf.dat")); // Read in golf.dat into the in object

        while(in.hasNextLine()){ // Keeps going while there are still lines in the .dat file

            TotRolls = in.nextInt(); // Gets number of rolls for the next line so overflow into next set doesn't happen

            for(int i = 0; i < TotRolls; i++){ // loops while the current line has another number
                switch(in.nextInt()){ // Converts rolled number into score
                    case 2:
                        TempPoints = 9;
                        break;
                    case 3:
                        TempPoints = -2;
                        break;
                    case 4:
                        TempPoints = 2;
                        break;
                    case 5:
                        TempPoints = 2;
                        break;
                    case 6:
                        TempPoints = 1;
                        break;
                    case 7:
                        TempPoints = -3;
                        break;
                    case 8:
                        TempPoints = 1;
                        break;
                    case 9:
                        TempPoints = -2;
                        break;
                    case 10:
                        TempPoints = -2;
                        break;
                    case 11:
                        TempPoints = 1;
                        break;
                    case 12:
                        TempPoints = 1;
                        break;
                    default:
                        TempPoints = 0; // Award no points if rolled number was out of range
                        CurPlayer = !CurPlayer; // Swaps player boolean to counter the one below, effectivly skipping the invalid number and keeping on the same player
                }

                CurPlayer = !CurPlayer; // Swaps players

                if(CurPlayer) // Checks which players turn it is and awards points to them
                    P1Score += TempPoints;
                else
                    P2Score += TempPoints;

                if(P1Score < 1) // Checks if either player has gone below hole 1 and sets them back
                    P1Score = 1;
                if(P2Score < 1)
                    P2Score = 1;

                if(P1Score > 9 || P2Score > 9) // Checks if any player has 10 or more points or if a Hole In One happened and break the loop if they do
                    break;

            }

            if(in.hasNextLine()) // Checks if golf.dat has another line. If it does, continue. If it doesn't break the loop and end program
                in.nextLine();

            if(P1Score > 9) // Checks which player broke the loop and prints out correct message
                System.out.println("Player 1 wins!");
            else if(P2Score > 9)
                System.out.println("Player 2 wins!");

            P1Score = 1; // Resets player scores for possible next line
            P2Score = 1;
            CurPlayer = false; // Resets turn boolean

        }

        in.close();

    }

}
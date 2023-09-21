import java.io.File;
import java.util.Scanner;

public class matrix{

    public static void main(String[] args) throws Exception{

        Scanner in = new Scanner(new File("matrix.dat")); // Reads the matrix.dat file into the "in" scanner object

        int[][] CurMatrix = new int[10][10]; // Creates an integer array with the max allowed array input size

        int TotalMatrices = in.nextInt(); // Takes in first number which is number of matricies
        int MatrixSize = 0; // Defines MatrixSize which is the length of the square matrix
        int LineTot = -1;
        int CurLineTot = 0;
        boolean Magic = true;

        for(int n = 0; n < TotalMatrices; n++){ // Goes through the indicated number of matrices

            MatrixSize = in.nextInt(); // Finds length of next matrix to be read

            for(int y = 0; y < MatrixSize; y++)
                for(int x = 0; x < MatrixSize; x++)
                    CurMatrix[x][y] = in.nextInt(); // Reads numbers into matrix array from top to bottom, left to right

            Magic = true; // Reset values for new check
            LineTot = -1;
            CurLineTot = 0;

            // Checking all the rows for complacency
            for(int x = 0; x < MatrixSize; x++){
                for(int y = 0; y < MatrixSize; y++)
                    CurLineTot += CurMatrix[x][y];

                if(LineTot < 0)
                    LineTot = CurLineTot; // Checks if a LineTot value has been entered yet, and if not then just set the current value as the check value
                else if(LineTot != CurLineTot)
                    Magic = false;

                CurLineTot = 0; // Resets CurLineTot for next line check
            }

            // Checking all the columns for complacency
            for(int y = 0; y < MatrixSize; y++){
                for(int x = 0; x < MatrixSize; x++)
                    CurLineTot += CurMatrix[x][y];

                if(LineTot != CurLineTot)
                    Magic = false;

                CurLineTot = 0;
            }

            // Checks top left to bottom right line
            for(int i = 0; i < MatrixSize; i++)
                CurLineTot += CurMatrix[i][i] + CurMatrix[i][MatrixSize - i - 1];


            if(LineTot * 2 != CurLineTot)
                Magic = false;



            // Prints out final determination and magic square sum if applicable
            if(Magic)
                System.out.println("This magic square has sum = " + LineTot + ".");
            else
                System.out.println("This isn't a magic square.");




        }

        in.close();
    }

}
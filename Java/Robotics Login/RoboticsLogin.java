import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class RoboticsLogin{

    public static ArrayList<Integer> IDs = new ArrayList<Integer>(); // Array list of student IDs
    public static ArrayList<String> Names = new ArrayList<String>(); // Array list of names
    public static ArrayList<Boolean> InOut = new ArrayList<Boolean>(); // Array list of student IDs
    public static Scanner BarcodeScanner; // Makes the user input public for use in other functions than main
    public static Scanner InOutScan;
    public static String ScannerInput = ""; // User input from the scanner
    public static String InOutLine = "";
    public static int InputInt = 0; // Declares the integer to hold the user provided ID number
    public static DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Sets date formatter
    public static DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Sets time formatter
    public static LocalDateTime now = LocalDateTime.now(); // Declares the "now" object and puts the current time into it
    public static boolean OutputsMade = false; // Tracks if the output files have been made yet
    public static BufferedWriter InOutwriter; // Declares the InOut output object
    public static BufferedWriter TXTwriter; // Declares the txt output object
    public static BufferedWriter HTMLwriter; // Declares the html output object
    public static String InOrOutPrint = "";
    public static String PrintOut = "";
    public static String HTMLContent = "";
    public static String TXTOutContent = "";
    public static String InOutString = "";
    public static Scanner TXTOutReader;
    public static Scanner HTMLReader;

    public static void main(String[] args)throws Exception{

        System.out.println("Loading Data, Please Wait...");

        String TempNameInput = new String(); // Temporary name holder
        Scanner IDInput = new Scanner(new File("IDs.txt")); // Opens the IDs.txt file

        while(IDInput.hasNextLine()){ // Loads all names and IDs into their arrays
            TempNameInput = IDInput.next();
            TempNameInput += " " + IDInput.next();
            Names.add(TempNameInput);
            IDs.add(IDInput.nextInt());
        }
        IDInput.close(); // Closes the IDInput object

        
        try{
            InOutScan = new Scanner(new File("InOut.txt")); // Opens the InOut.txt file
            InOutLine = InOutScan.nextLine(); // Takes in the only line in the file into a string
            InOutScan.close(); // Closes the InOut scanner
        }
        catch(Exception e){
            System.out.println("InOut file error");
        }

        String Date = DateFormatter.format(LocalDateTime.now());

        // This block loads the Output TXT file if it exists
        File TXTOutObj = new File("Outputs/TXTs/" + Date + ".txt");
        if(TXTOutObj.exists()){ // Checks if the TXT output file already exist and gets the contents if it does
            TXTOutReader = new Scanner(TXTOutObj); // Creates TXTOutReader
            while(TXTOutReader.hasNextLine())
                TXTOutContent += TXTOutReader.nextLine() + "\n";
        }

        // This block loads the HTML file if it exists and sets a blank HTML template if it doesn't
        File HTMLObj = new File("Outputs/HTMLs/" + Date + ".html");
        if(HTMLObj.exists()){ // Checks if the HTML file already exist and gets the contents if it does
            HTMLReader = new Scanner(HTMLObj);
            while(HTMLReader.hasNextLine())
                HTMLContent += HTMLReader.nextLine() + "\n";
        }
        else // If file didn't already exist, put the HTML template in its place
            HTMLContent = "<html>\n<body>\n<h1>" + Date + " Logs</h1>\n<h2>\n</h2>\n</body>\n</html>\n";

        for(int i = 0; i < IDs.size(); i++) // Assigns each student in the database a boolean (false by default)
            InOut.add(false);

        System.out.println(InOutLine.length());

        for(int i = 0; i < InOutLine.length() && i < IDs.size(); i++) // Goes through all characters in the string or IDs length
            if(InOutLine.charAt(i) == '1') // Checks if the current character is a 1
                InOut.set(i, true); // Sets the students boolean to 1

        for(int i = 0; i < IDs.size(); i++)
            if(InOut.get(i))
                InOutString += "1";
            else
                InOutString += "0";

        clearConsole(); // Makes sure screen is clear

        BarcodeScanner = new Scanner(System.in); // Gets user input

        while(true){ // This is the part that loops and checks user input

            System.out.println("Scan Student ID"); // Prompts user to scan their ID
            ScannerInput = BarcodeScanner.nextLine(); // Gets the user's input as a string

            clearConsole(); // Clears the screen for new outputs for user

            if(ScannerInput.equals("Exit"))
                System.exit(0);

            if(ScannerInput.equals("Restart"))
                Restart();

            if(ScannerInput.equals("EnterAdmin"))
                AdminInput();
            
            if(ScannerInput.equals("FlushInOut"))
                FlushInOut();
            
            else if(isNumeric(ScannerInput) && ScannerInput.length() == 6) // Checks if the input is numeric and runs the MakeEntry function
                MakeEntry();
            else
                System.out.println("\033[0;31mInput was not valid\033[0m"); // Tells the user that the givin input matched none of the supported functions

        }

    }

    public static void MakeEntry()throws Exception{

        InputInt = Integer.parseInt(ScannerInput); // Converts the verified ID string to an integer

        String Date = DateFormatter.format(LocalDateTime.now()); // Gets the current date in correct format

        boolean IDFound = false; // Resets the IDFound variable from the last user input

        for(int i = 0; i < IDs.size(); i++) // Goes through all registered student IDs
            if(InputInt == IDs.get(i)){
                IDFound = true;

                //Makes and clears the writers
                InOutwriter = new BufferedWriter(new FileWriter("InOut.txt")); // Opens the InOut.txt file
                TXTwriter = new BufferedWriter(new FileWriter("Outputs\\TXTs\\" + Date + ".txt")); // Makes and opens current day's txt file
                HTMLwriter = new BufferedWriter(new FileWriter("Outputs\\HTMLs\\" + Date + ".html")); // Makes and opens current day's html file

                InOut.set(i, !InOut.get(i)); // Flips the boolean in the InOut array for the user index

                if(InOut.get(i)) // Builds the new string with the updated booleans
                    InOutString = InOutString.substring(0, i) + "1" + InOutString.substring(i + 1);
                else
                    InOutString = InOutString.substring(0, i) + "0" + InOutString.substring(i + 1);

                InOutwriter.write(InOutString); // Writes the InOut array to buffer

                InOutwriter.close(); // Closes the InOutwriter object and writes the buffer to the file

                if(InOut.get(i)) // Changes output string depending on new user boolean
                    InOrOutPrint = "In";
                else
                    InOrOutPrint = "Out";

                //Makes string for output and putting into files
                PrintOut = String.format("%d %s (Signed_%s) %s", IDs.get(i), Names.get(i), InOrOutPrint, TimeFormatter.format(LocalDateTime.now()));
                System.out.println("\033[0;33m" + PrintOut + "\033[0m"); // Prints output for user

                TXTOutContent += PrintOut + "\n"; // Builds the newest TXTOut string
                TXTwriter.write(TXTOutContent); // Updates the output TXT file buffer
                TXTwriter.close(); // Closes the TXTwriter object and writes the buffer to the file

                if(InOut.get(i)) // Builds the new HTML file string with the newest line
                    HTMLContent = InsertString(HTMLContent, "<font color=\"green\">" + PrintOut + "</font><br>" + "\n", 23, "Back");
                else
                    HTMLContent = InsertString(HTMLContent, "<font color=\"red\">" + PrintOut + "</font><br>" + "\n", 23, "Back");

                HTMLwriter.write(HTMLContent); // Writes the newest HTML string to the buffer
                HTMLwriter.close(); // Closes the HTMLwriter object and writes the buffer to the file
                
            }

        if(!IDFound)
            System.out.println("\033[0;31mID was not found in database\033[0m"); // Reports to the user in red text that the ID they gave was not found

    }

    public static void AdminInput()throws Exception{ // Used for scanning in an ID with only having the numbert

        String AdminString = ""; // Makes a blank string to input ID manually

        System.out.println("Scan or type in the ID number by number"); // Prompts admin for ID with instructions

        for(int i = 0; i < 6; i++){ // Repeats 6 times for each number in the ID
            AdminString += BarcodeScanner.nextLine(); // Gets the scanned number
            clearConsole();
            System.out.println(AdminString); // Prints out the ID as inputted so far
        }

        ScannerInput = AdminString; // Passes the build ID to the main Input string to be checked

    }

    public static void FlushInOut()throws Exception{

        InOutString = ""; // Empties InOutString for easier changing

        for(int i = 0; i < InOut.size(); i++){ // Repeats through the length of the InOut array list
            InOut.set(i, false); // Sets the array list to all 0's
            InOutString += "0"; // Sets the InOut string to all 0's
        }

        InOutwriter = new BufferedWriter(new FileWriter("InOut.txt")); // Opens the InOut.txt file
        InOutwriter.write(InOutString); // Writes the cleared InOut string to the file buffer
        InOutwriter.close(); // Writes the buffer to the file

        System.out.println("\033[0;33mInOut array has been cleared\033[0m");

    }

    public static void Restart()throws Exception{

        try{
            if (System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("KickStart.bat").start();
                System.exit(0);
            }
            else
                System.out.println("Linux Restart command not inegrated");
                //new ProcessBuilder("clear").start();
        } catch (Exception e){
            System.out.println("\033[0;31mError: Could not restart program, " + e + "\033[0m");
        }

    }




    // Bellow this comment is funtions that are meant for utility and have been at least partially taken from somewhere else

    // Takes a two strings and an int then inserts the seconds string at the int value of the first string
    public static String InsertString(String originalString, String stringToBeInserted, int index, String FrontOrBack){

        String CombinedOutput = new String();

        if(FrontOrBack.equals("Back")) // Checks if the user wants to count from the back instead of the front
            index = originalString.length() - index;
  
        for(int i = 0; i < originalString.length(); i++){
            // Insert the original string characters
            CombinedOutput += originalString.charAt(i);
            if(i == index)
                // Insert the second string
                CombinedOutput += stringToBeInserted;
        }
  
        // return the Combined String
        return CombinedOutput;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum); // Might need to write this to an int to work put prob not
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static void clearConsole(){ // Clears the screen

        try{
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                new ProcessBuilder("clear").inheritIO().start().waitFor();

        } catch (IOException | InterruptedException ex) {}

    }


    /*

    // Lists of color codes to put into strings
    public class ConsoleColors {
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset
    
        // Regular Colors
        public static final String BLACK = "\033[0;30m";   // BLACK
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String PURPLE = "\033[0;35m";  // PURPLE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE = "\033[0;37m";   // WHITE
    
        // Bold
        public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
        public static final String RED_BOLD = "\033[1;31m";    // RED
        public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
        public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
        public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
        public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
        public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
        public static final String WHITE_BOLD = "\033[1;37m";  // WHITE
    
        // Underline
        public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
        public static final String RED_UNDERLINED = "\033[4;31m";    // RED
        public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
        public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
        public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
        public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
        public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
        public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE
    
        // Background
        public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
        public static final String RED_BACKGROUND = "\033[41m";    // RED
        public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
        public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
        public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
        public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
        public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
        public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE
    
        // High Intensity
        public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
        public static final String RED_BRIGHT = "\033[0;91m";    // RED
        public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
        public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
        public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
        public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
        public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
        public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE
    
        // Bold High Intensity
        public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
        public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
        public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
        public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
        public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
        public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
        public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
        public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
    
        // High Intensity backgrounds
        public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
        public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
        public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
        public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
        public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
        public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
        public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
    }

    */

}
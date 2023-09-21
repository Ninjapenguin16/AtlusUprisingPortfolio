import java.util.*;
import java.io.File;

public class Hashing{

    public static void main(String[] args)throws Exception{

        HashMap<Integer, String> studentMap = new HashMap<>();

        String TempName = "";

        Scanner FileIn = new Scanner(new File("IDs.txt"));

        while(FileIn.hasNextLine()){

            TempName = FileIn.next();
            TempName += " " + FileIn.next();
            studentMap.put(FileIn.nextInt(), TempName);

        }
        
        FileIn.close();

        Scanner UserIn = new Scanner(System.in);

        while(true){

            System.out.print("What ID name do you want?: ");

            try{
                System.out.println(studentMap.get(UserIn.nextInt()));
            }
            catch(Exception e){
                System.out.println("That wasn't a number dumbass");
            }

            UserIn.nextLine();

        }



    }

}
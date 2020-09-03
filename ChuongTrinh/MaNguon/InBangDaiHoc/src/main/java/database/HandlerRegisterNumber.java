package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HandlerRegisterNumber {

    public static String readFile(File file){
        String data = "";
        try {
            if (!file.exists()){
                file.createNewFile();
            }else {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    data = scanner.nextLine();
                }
                scanner.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

    public static void  writeFile(File file, String content){
        try {
            if (!file.exists()){
                file.createNewFile();
            }else {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(content);
                fileWriter.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

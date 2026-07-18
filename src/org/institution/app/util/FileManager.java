package org.institution.app.util;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class FileManager {
    

    public FileManager() {}

    public Enum.Error writeToFile(String path, String data) throws IOException {
        File f = new File("data/" + path);

        if (!f.exists()) {
            f.createNewFile();
        }

        try (FileWriter writer = new FileWriter(f)) {
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            return Enum.Error.COULD_NOT_WRITE_TO_FILE;
        }

        return null;
    }


    public ArrayList<ArrayList<String>> readFromFile(String path) throws FileNotFoundException {
        File f = new File("data/" + path);
        Scanner reader = new Scanner(f);
        ArrayList<ArrayList<String>> array = new ArrayList<>();

        if (!f.exists()) {
            reader.close();
            return null;
        }

        while (reader.hasNextLine()) {
            ArrayList<String> str = new ArrayList<>();
            String s = reader.nextLine();

            String string = "";
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != ',') {
                    string += s.charAt(i);
                } else {
                    str.add(string);
                    string = "";
                }
            }
            str.add(string);
            array.add(str);
        }

        reader.close();
        return array;

    }

}

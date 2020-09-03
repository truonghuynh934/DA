package database;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.collections.FXCollections;
import model.Major;

import java.io.*;
import java.util.List;

public class MajorDAO {
    private File file = new File("major.json");

    public MajorDAO() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Major> readJsonFile() {
        List<Major> listMajors = FXCollections.observableArrayList();
        try {
            InputStream inputStream = new FileInputStream(file.getPath());
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            if (file.length() > 0) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    Major Major = new Major();

                    String idMajor = reader.nextName();
                    Major.setIdMajor(reader.nextString());
                    String vietnam = reader.nextName();
                    Major.setVietnam(reader.nextString());
                    String english = reader.nextName();
                    Major.setEnglish(reader.nextString());
                    reader.endObject();
                    listMajors.add(Major);
                }

                reader.endArray();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Read json file method");
        return listMajors;
    }

    public void writeUpdateJsonFile(List<Major> listMajors) {
        try {
            OutputStream outputStream = new FileOutputStream(file.getPath());
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            writer.beginArray();
            for (Major Major : listMajors) {
                writer.beginObject();
                writer.name("idMajor").value(Major.getIdMajor());
                writer.name("vietnam").value(Major.getVietnam());
                writer.name("english").value(Major.getEnglish());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Write update Json file method");
    }
}

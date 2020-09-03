package database;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.collections.FXCollections;
import model.ModeOfStudy;

import java.io.*;
import java.util.List;

public class ModeOfStudyDAO {
    private File file = new File("mode_of_study.json");

    public ModeOfStudyDAO(){
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public List<ModeOfStudy> readJsonFile(){
        List<ModeOfStudy> listModeOfStudies = FXCollections.observableArrayList();
        try {
            InputStream inputStream = new FileInputStream(file.getPath());
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            if (file.length() > 0) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    ModeOfStudy modeOfStudy = new ModeOfStudy();

                    String idModeOfStudy = reader.nextName();
                    modeOfStudy.setIdModeOfStudy(reader.nextString());
                    String vietnam = reader.nextName();
                    modeOfStudy.setVietnam(reader.nextString());
                    String english = reader.nextName();
                    modeOfStudy.setEnglish(reader.nextString());
                    reader.endObject();
                    listModeOfStudies.add(modeOfStudy);
                }

                reader.endArray();
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Read json file method");
        return listModeOfStudies;
    }

    public void writeUpdateJsonFile(List<ModeOfStudy> listModeOfStudies){
        try {
            OutputStream outputStream = new FileOutputStream(file.getPath());
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            writer.beginArray();
            for (ModeOfStudy modeOfStudy : listModeOfStudies) {
                writer.beginObject();
                writer.name("idModeOfStudy").value(modeOfStudy.getIdModeOfStudy());
                writer.name("vietnam").value(modeOfStudy.getVietnam());
                writer.name("english").value(modeOfStudy.getEnglish());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Write update Json file method");
    }
}

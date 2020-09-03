package database;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.collections.FXCollections;
import model.Rank;

import java.io.*;
import java.util.List;

public class RankDAO {
    private File file = new File("rank.json");

    public RankDAO() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Rank> readJsonFile() {
        List<Rank> listRanks = FXCollections.observableArrayList();
        try {
            InputStream inputStream = new FileInputStream(file.getPath());
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            if (file.length() > 0) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    Rank Rank = new Rank();

                    String idRank = reader.nextName();
                    Rank.setIdRank(reader.nextString());
                    String vietnam = reader.nextName();
                    Rank.setVietnam(reader.nextString());
                    String english = reader.nextName();
                    Rank.setEnglish(reader.nextString());
                    reader.endObject();
                    listRanks.add(Rank);
                }

                reader.endArray();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Read json file method");
        return listRanks;
    }

    public void writeUpdateJsonFile(List<Rank> listRanks) {
        try {
            OutputStream outputStream = new FileOutputStream(file.getPath());
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            writer.beginArray();
            for (Rank Rank : listRanks) {
                writer.beginObject();
                writer.name("idRank").value(Rank.getIdRank());
                writer.name("vietnam").value(Rank.getVietnam());
                writer.name("english").value(Rank.getEnglish());
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

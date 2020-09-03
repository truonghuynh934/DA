package database;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.collections.FXCollections;
import model.Major;
import model.Student;
//import sun.util.resources.LocaleData;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class StudentPrintedDAO {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public List<Student> readJsonFile(){
        List<Student> listStudents = FXCollections.observableArrayList();
        try {
            InputStream inputStream = new FileInputStream("student_printed.json");
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                Student student = new Student();

                String idStudent = reader.nextName();
                student.setIdStudent(reader.nextString());
                String name = reader.nextName();
                student.setName(reader.nextString());
                String dateOfBirth = reader.nextName();
                student.setDateOfBirth(reader.nextString());
                String sex = reader.nextName();
                student.setSex(reader.nextString());
                String hometown = reader.nextName();
                student.setHometown(reader.nextString());
                String gpa = reader.nextName();
                student.setGpa((float)reader.nextDouble());
                String rank = reader.nextName();
                student.setRank(reader.nextString());
                String licenseDate = reader.nextName();
                student.setLicenseDate(LocalDate.parse(reader.nextString(),dateTimeFormatter));
                String graduationYear = reader.nextName();
                student.setGraduationYear(reader.nextInt());
                String registerNumber = reader.nextName();
                student.setRegisterNumber(reader.nextString());
                String major = reader.nextName();
                student.setMajor(reader.nextString());
                String modeOfStudy = reader.nextName();
                student.setModeOfStudy(reader.nextString());
                String title = reader.nextName();
                student.setTitle(reader.nextString());
                String strClass = reader.nextName();
                student.setStrClass(reader.nextString());
                reader.endObject();
                listStudents.add(student);
            }

            reader.endArray();
            reader.close();
        }catch (IOException e){
            return FXCollections.observableArrayList();
//            e.printStackTrace();
        }
        System.out.println("Read json file method");
        return listStudents;
    }

    public void writeUpdateJsonFile(List<Student> listStudents){
        try {
            OutputStream outputStream = new FileOutputStream("student_printed.json");
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            writer.beginArray();
            for (Student student : listStudents) {
                writer.beginObject();
                writer.name("idMajor").value(student.getIdStudent());
                writer.name("name").value(student.getName());
                writer.name("dateOfBirth").value(student.getDateOfBirth());
                writer.name("sex").value(student.getSex());
                writer.name("hometown").value(student.getHometown());
                writer.name("gpa").value(student.getGpa());
                writer.name("rank").value(student.getRank());
                writer.name("licenseDate").value(dateTimeFormatter.format(student.getLicenseDate()));
                writer.name("graduationYear").value(student.getGraduationYear());
                writer.name("registerNumber").value(student.getRegisterNumber());
                writer.name("major").value(student.getMajor());
                writer.name("modeOfStudy").value(student.getModeOfStudy());
                writer.name("title").value(student.getTitle());
                writer.name("class").value(student.getStrClass());
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

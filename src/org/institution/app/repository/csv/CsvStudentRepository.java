package org.institution.app.repository.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.institution.app.repository.StudentRepository;
import org.institution.app.model.Student;
import org.institution.app.util.FileManager;
import org.institution.app.serializer.student.StudentSerializer;

public class CsvStudentRepository implements StudentRepository {
    private ArrayList<Student> students = new ArrayList<>();
    private FileManager reader = new FileManager();
    private FileManager writer = new FileManager();
    private int lastID;

    public CsvStudentRepository() {
        this.lastID = 0;
    }

    @Override
    public void newStudent(Student s) {
        students.add(s);
        this.lastID += 1;
    }

    @Override
    public ArrayList<Student> getStudentsByName(String name) {
        ArrayList<Student> _students = new ArrayList<>();

        for (Student s : students) {
            if (s.getName().equals(name)) {
                _students.add(s);
            }
                
        }

        return _students;
    }

    @Override
    public Student getStudentByID(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }

        return null;
    }

    @Override
    public Student getStudentByEmail(String email) {
        for (Student s : students) {
            if (s.getEmail().equals(email)){
                return s;
            }
        }

        return null;
    }

    @Override
    public void deleteStudent(Student s) {
        students.remove(s);
    }

    @Override
    public boolean save(StudentSerializer serializer) {
        String data = serializer.export(students);

        try {
            writer.writeToFile("csv/students.csv", data);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean load() {
        ArrayList<ArrayList<String>> studentsData; 
        
        try {
            studentsData = reader.readFromFile("csv/students.csv");
        } catch (FileNotFoundException e) {
            return false;
        }

        for (ArrayList<String> array : studentsData) {
            students.add(new Student(
                Integer.parseInt(array.get(0)),
                array.get(1),
                Integer.parseInt(array.get(2)),
                array.get(3),
                Double.parseDouble(array.get(4)),
                Boolean.parseBoolean(array.get(5))
            ));
        }

        int lastID = 0;
        for (Student s : students) {
            int studentID = s.getId();

            if (studentID > lastID) {
                lastID = studentID;
            }
        }

        this.lastID = lastID;

        return true;
    }

    // Getters
    @Override
    public int getLastID() { return lastID; }

    @Override
    public ArrayList<Student> getStudents() { return students; }

}

package org.institution.app.repository;

import java.io.IOException;
import java.util.ArrayList;
import org.institution.app.model.Student;
import org.institution.app.util.FileManager;
import org.institution.app.util.Helper;

public class StudentRepository {
    private ArrayList<Student> students = new ArrayList<>();
    private FileManager reader = new FileManager();
    private FileManager writer = new FileManager();
    private int lastID;

    public StudentRepository() {
        this.lastID = 0;
    }

    public void newStudent(int id, String name, int age, String email) {
        students.add(new Student(id, name, age, email));
        this.lastID += 1;
    }

    public ArrayList<Student> getStudentsByName(String name) {
        ArrayList<Student> _students = new ArrayList<>();

        for (Student s : students) {
            if (s.getName().equals(name)) {
                _students.add(s);
            }
                
        }

        return _students;
    }

    public Student getStudentByID(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }

        return null;
    }

    public Student getStudentByEmail(String email) {
        for (Student s : students) {
            if (s.getEmail().equals(email)){
                return s;
            }
        }

        return null;
    }

    public void deleteStudent(int id) {
        Student s = getStudentByID(id);
        
        students.remove(s);
    }

    public boolean saveStudentsToCSV() {
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();
        
        for (Student s : students) {
            studentsData.add(Helper.studentToStringArray(s));
        }

        try {
            writer.writeToFile("students.txt", studentsData);
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }

    public boolean loadStudentsFromCSV() {
        ArrayList<ArrayList<String>> studentsData; 
        
        try {
            studentsData = reader.readFromFile("students.txt");
        } catch (IOException e) {
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
    public int getLastID() { return lastID; }
    public ArrayList<Student> getStudents() { return students; }

}

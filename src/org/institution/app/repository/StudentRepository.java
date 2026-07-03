package org.institution.app.repository;

import java.util.ArrayList;
import org.institution.app.model.Student;

public interface StudentRepository {

    void newStudent(int id, String name, int age, String email);

    Student getStudentByID(int id);

    Student getStudentByEmail(String email);
    
    ArrayList<Student> getStudentsByName(String name);

    ArrayList<Student> getStudents();

    boolean save();

    boolean load();

    int getLastID();

    void deleteStudent(int id);

}

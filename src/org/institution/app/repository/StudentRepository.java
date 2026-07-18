package org.institution.app.repository;

import java.util.ArrayList;
import org.institution.app.model.Student;
import org.institution.app.serializer.student.StudentSerializer;

public interface StudentRepository {

    void newStudent(Student s);

    Student getStudentByID(int id);

    Student getStudentByEmail(String email);
    
    ArrayList<Student> getStudentsByName(String name);

    ArrayList<Student> getStudents();

    boolean save(StudentSerializer serializer);

    boolean load();

    int getLastID();

    void deleteStudent(Student s);

}

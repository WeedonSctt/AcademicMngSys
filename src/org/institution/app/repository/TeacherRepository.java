package org.institution.app.repository;

import java.util.ArrayList;
import org.institution.app.model.Teacher;

public interface TeacherRepository {

    void newTeacher(int id, String name, String department, String email);

    Teacher getTeacherByID(int id);

    Teacher getTeacherByName(String name);

    ArrayList<Teacher> getTeachers();

    boolean save();

    boolean load();

    int getLastID();

    void deleteTeacher(int id);
    
}

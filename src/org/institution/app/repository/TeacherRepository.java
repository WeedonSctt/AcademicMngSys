package org.institution.app.repository;

import java.util.ArrayList;
import org.institution.app.model.Teacher;

public interface TeacherRepository {

    void newTeacher(Teacher t);

    Teacher getTeacherByID(int id);

    Teacher getTeacherByName(String name);

    ArrayList<Teacher> getTeachers();

    boolean save();

    boolean load();

    int getLastID();

    void deleteTeacher(Teacher t);
    
}

package org.institution.app.repository.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.institution.app.repository.TeacherRepository;
import org.institution.app.model.Teacher;
import org.institution.app.util.FileManager;
import org.institution.app.serializer.teacher.TeacherSerializer;;

public class CsvTeacherRepository implements TeacherRepository {
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private FileManager reader = new FileManager();
    private FileManager writer = new FileManager();
    
    int lastID;

    public CsvTeacherRepository() {
        this.lastID = 0;
    }

    @Override
    public void newTeacher(Teacher t) {
        teachers.add(t);
        this.lastID += 1;
        return;
    }

    @Override
    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    @Override
    public Teacher getTeacherByID(int id) {
        for (Teacher t : teachers) {
            if (t.getID() == id) {
                return t;
            }
        }

        return null;
    }
    
    @Override
    public Teacher getTeacherByName(String name) {
        for (Teacher t : teachers) {
            if (t.getName().equals(name)) {
                return t;
            }
        }

        return null;
    }
    
    @Override
    public int getLastID() { return lastID; }

    @Override
    public void deleteTeacher(Teacher t) {        
        teachers.remove(t);
    }

    @Override
    public boolean save(TeacherSerializer serializer) {
        String data = serializer.export(teachers);

        try {
            writer.writeToFile("csv/teachers.csv", data);
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean load() {
        ArrayList<ArrayList<String>> teachersData; 
        
        try {
            teachersData = reader.readFromFile("csv/teachers.csv");
        } catch (FileNotFoundException e) {
            return false;
        }

        for (ArrayList<String> array : teachersData) {
            teachers.add(new Teacher(
                Integer.parseInt(array.get(0)),
                array.get(1),
                array.get(2),
                array.get(3)
            ));
        }

        int lastID = 0;
        for (Teacher t : teachers) {
            int teacherID = t.getID();

            if (teacherID > lastID) {
                lastID = teacherID;
            }
        }

        this.lastID = lastID;

        return true;
    }

}

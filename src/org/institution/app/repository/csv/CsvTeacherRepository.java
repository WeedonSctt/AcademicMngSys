package org.institution.app.repository.csv;

import java.io.IOException;
import java.util.ArrayList;
import org.institution.app.repository.TeacherRepository;
import org.institution.app.model.Teacher;
import org.institution.app.util.Helper;
import org.institution.app.util.FileManager;

public class CsvTeacherRepository implements TeacherRepository {
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private FileManager reader = new FileManager();
    private FileManager writer = new FileManager();
    
    int lastID;

    public CsvTeacherRepository() {
        this.lastID = 0;
    }

    @Override
    public void newTeacher(int id, String name, String department, String email) {
        teachers.add(new Teacher(id, name, department, email));
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
    public void deleteTeacher(int id) {
        Teacher t = getTeacherByID(id);
        
        teachers.remove(t);
    }

    @Override
    public boolean save() {
        ArrayList<ArrayList<String>> teachersData = new ArrayList<>();
        
        for (Teacher t : teachers) {
            teachersData.add(Helper.teacherToStringArray(t));
        }

        try {
            writer.writeToFile("teachers.txt", teachersData);
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }

    public boolean load() {
        ArrayList<ArrayList<String>> teachersData; 
        
        try {
            teachersData = reader.readFromFile("teachers.txt");
        } catch (IOException e) {
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

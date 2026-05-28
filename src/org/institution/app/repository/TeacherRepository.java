package org.institution.app.repository;

// JAVA LIBS
import java.io.IOException;
import java.util.ArrayList;

// PROJ PACKAGES
import org.institution.app.model.Teacher;
import org.institution.app.util.Helper;
import org.institution.app.util.FileManager;

public class TeacherRepository {
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private FileManager reader = new FileManager();
    private FileManager writer = new FileManager();
    
    int lastID;

    public TeacherRepository() {
        this.lastID = 0;
    }

    public void newTeacher(int id, String name, String department, String email) {
        teachers.add(new Teacher(id, name, department, email));
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public Teacher getTeacherByID(int id) {
        for (Teacher t : teachers) {
            if (t.getID() == id) {
                return t;
            }
        }

        return null;
    }
    
    public Teacher getTeacherByName(String name) {
        for (Teacher t : teachers) {
            if (t.getName().equals(name)) {
                return t;
            }
        }

        return null;
    }
    
    public int getLastID() { return lastID; }

    public boolean removeTeacher(int id) {
        for (Teacher t : teachers) {
            if (t.getID() == id) {
                teachers.remove(t);
                return true;
            }
        }

        return false;
    }

    public boolean saveTeachersToCSV() {
        ArrayList<ArrayList<String>> teachersData = new ArrayList<>();
        
        for (Teacher t : teachers) {
            teachersData.add(Helper.teacherToStringArray(t));
        }

        try {
            writer.writeToFile("teacher.txt", teachersData);
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }

    public boolean loadTeachersFromCSV() {
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

        return true;
    }

}

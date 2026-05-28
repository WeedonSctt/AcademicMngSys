package org.institution.app.repository;

// JAVA LIBS
import java.io.IOException;
import java.util.ArrayList;

// PROJ PACKAGES
import org.institution.app.model.Course;
import org.institution.app.util.FileManager;
import org.institution.app.util.Helper;

public class CourseRepository {
    private ArrayList<Course> courses = new ArrayList<>();
    private int lastID;
    private FileManager reader = new FileManager();
    private FileManager writer = new FileManager();

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public int getLastID() { return lastID; }

    public void newCourse(int id, String name, String description, int maximumStudents, int teacherID) {
        courses.add(new Course(id, name, description, maximumStudents, teacherID));
        this.lastID += 1;
        return;
    }

    public Course getCourseByID(int id) {
        for (Course c : courses) {
            if (c.getId() == id) {
                return c;
            }
        }

        return null;
    }

    public void removeCourse(Course c) {
        courses.remove(c);
    }

    public boolean saveCoursesToCSV() {
        ArrayList<ArrayList<String>> coursesData = new ArrayList<>();
        
        for (Course c : courses) {
            coursesData.add(Helper.courseToStringArray(c));
        }

        try {
            writer.writeToFile("courses.txt", coursesData);
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }

    public boolean loadCoursesFromCSV() {
        ArrayList<ArrayList<String>> coursesData; 
        
        try {
            coursesData = reader.readFromFile("courses.txt");
        } catch (IOException e) {
            return false;
        }

        for (ArrayList<String> array : coursesData) {
            courses.add(new Course(
                Integer.parseInt(array.get(0)),
                array.get(1),
                array.get(2),
                Integer.parseInt(array.get(3)),
                Integer.parseInt(array.get(4))
            ));
        }

        return true;
    }

}

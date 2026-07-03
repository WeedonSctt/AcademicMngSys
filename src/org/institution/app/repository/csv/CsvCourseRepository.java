package org.institution.app.repository.csv;

import java.io.IOException;
import java.util.ArrayList;
import org.institution.app.model.Course;
import org.institution.app.repository.CourseRepository;
import org.institution.app.util.FileManager;
import org.institution.app.util.Helper;

public class CsvCourseRepository implements CourseRepository {
    private ArrayList<Course> courses = new ArrayList<>();
    private int lastID;
    private FileManager reader = new FileManager();
    private FileManager writer = new FileManager();

    public CsvCourseRepository() {
        this.lastID = 0;
    }

    @Override
    public ArrayList<Course> getCourses() {
        return courses;
    }

    @Override
    public int getLastID() { return lastID; }

    @Override
    public void newCourse(int id, String name, String description, int maximumStudents, int teacherID) {
        courses.add(new Course(id, name, description, maximumStudents, teacherID));
        this.lastID += 1;
        return;
    }

    @Override
    public Course getCourseByID(int id) {
        for (Course c : courses) {
            if (c.getId() == id) {
                return c;
            }
        }

        return null;
    }

    @Override
    public Course getCourseByName(String name) {
        for (Course c : courses) {
            if (c.getName().equals(name)) {
                return c;
            }
        }

        return null;
    }

    @Override
    public void deleteCourse(Course c) {
        courses.remove(c);
    }

    @Override
    public boolean save() {
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

    @Override
    public boolean load() {
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

        int lastID = 0;
        for (Course c : courses) {
            int courseID = c.getId();

            if (courseID > lastID) {
                lastID = courseID;
            }
        }

        this.lastID = lastID;

        return true;
    }

}

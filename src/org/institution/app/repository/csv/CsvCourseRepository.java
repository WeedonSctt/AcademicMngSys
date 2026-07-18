package org.institution.app.repository.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.institution.app.model.Course;
import org.institution.app.repository.CourseRepository;
import org.institution.app.util.FileManager;
import org.institution.app.serializer.course.CourseSerializer;

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
    public void newCourse(Course c) {
        courses.add(c);
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
    public boolean save(CourseSerializer serializer) {
        String data = serializer.export(courses);

        try {
            writer.writeToFile("csv/courses.csv", data);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean load() {
        ArrayList<ArrayList<String>> coursesData; 
        
        try {
            coursesData = reader.readFromFile("csv/courses.csv");
        } catch (FileNotFoundException e) {
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

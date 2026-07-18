package org.institution.app.repository;

import java.util.ArrayList;
import org.institution.app.model.Course;
import org.institution.app.serializer.course.CourseSerializer;

public interface CourseRepository {

    void newCourse(Course c);

    Course getCourseByID(int id);

    Course getCourseByName(String name);

    ArrayList<Course> getCourses();

    boolean save(CourseSerializer serializer);

    boolean load();

    int getLastID();

    void deleteCourse(Course c);

}

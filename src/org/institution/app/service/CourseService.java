package org.institution.app.service;

import java.util.ArrayList;
import org.institution.app.repository.CourseRepository;
import org.institution.app.model.Course;
import org.institution.app.util.Enum;
import org.institution.app.util.*;

public class CourseService {
    private final CourseRepository repository;
    private final TeacherService teacherService;

    public CourseService(CourseRepository repo, TeacherService teacherService) {
        this.repository = repo;
        this.teacherService = teacherService;
    }

    public ArrayList<ArrayList<String>> getAssignedCourses(int teacherID) {
        ArrayList<Course> courses = repository.getCourses();
        ArrayList<ArrayList<String>> assignedCoursesData = new ArrayList<>();

        for (Course c : courses) {
            if (c.getTeacherId() == teacherID) {
                ArrayList<String> course = Helper.courseToStringArray(c);

                course = replaceTeacherIDWithName(course, c);

                assignedCoursesData.add(course);
            }
        }

        return assignedCoursesData;
    }

    public Enum.Error newCourse(String name, String description, int maximumStudents) {
        if (maximumStudents < 0 || maximumStudents > 50) {
            return Enum.Error.INVALID_STUDENT_QUOTA;
        }

        for (Course c : repository.getCourses()) {
            if (c.getName().equals(name)) {
                return Enum.Error.ALREADY_CREATED;
            }
        }

        repository.newCourse(repository.getLastID() + 1, name, description, maximumStudents, -1);
        
        return null;
    }

    public Enum.Error editCourse(int id, String name, String description, int maximumStudents) {
        if (maximumStudents < 0 || maximumStudents > 50) {
            return Enum.Error.INVALID_STUDENT_QUOTA;
        }

        for (Course c : repository.getCourses()) {
            if (c.getName().equals(name)) {
                return Enum.Error.REPEATED_COURSE_NAME;
            }
        }
        
        Course c = repository.getCourseByID(id);

        c.setName(name);
        c.setDescription(description);
        c.setMaximumStudents(maximumStudents);

        return null;
    }

    public Enum.Error assignTeacher(int courseID, int teacherID) {
        if (!existCourse(courseID)) {
            return Enum.Error.COURSE_NOT_FOUND;
        }

        if (!teacherService.existTeacher(teacherID)) {
            return Enum.Error.TEACHER_NOT_FOUND;
        }

        Course c = repository.getCourseByID(courseID);
        c.setTeacherId(teacherID);

        return null;
    }

    public Enum.Error removeCourse(int courseID) {
        if (!existCourse(courseID)) {
            return Enum.Error.COURSE_NOT_FOUND;
        }

        repository.removeCourse(repository.getCourseByID(courseID));

        return null;
    }

    public ArrayList<ArrayList<String>> getCourses() {
        ArrayList<ArrayList<String>> coursesData = new ArrayList<>();
        ArrayList<Course> courses = repository.getCourses();

        for (Course c : courses) {
            ArrayList<String> course = Helper.courseToStringArray(c);

            course = replaceTeacherIDWithName(course, c);

            coursesData.add(course);
        }

        return coursesData;
    }

    public Course getCourseByID(int id) {
        ArrayList<Course> courses = repository.getCourses();

        for (Course c : courses) {
            if (c.getId() == id) {
                return c;
            }
        }

        return null;
    }

    public boolean save() {
        return repository.saveCoursesToCSV();
    }

    public boolean loadRepo() {
        return repository.loadCoursesFromCSV();
    }

    public boolean existCourse(int id) {
        for (Course c : repository.getCourses()) {
            if (c.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<String> searchCourseByName(String name) {
        Course c = repository.getCourseByName(name);
        ArrayList<String> course = Helper.courseToStringArray(c);

        course = replaceTeacherIDWithName(course, c);

        return course;
    }

    public ArrayList<String> searchCourseByID(int id) {
        Course c = repository.getCourseByID(id);
        ArrayList<String> course = Helper.courseToStringArray(c);

        course = replaceTeacherIDWithName(course, c);

        return course;
    }

    private ArrayList<String> replaceTeacherIDWithName(ArrayList<String> course, Course c) {
        course.removeLast();
        course.add(teacherService.getTeacherByID(c.getTeacherId()).getName());

        return course;
    }

}

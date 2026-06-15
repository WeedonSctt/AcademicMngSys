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
        ArrayList<Course> assignedCourses = new ArrayList<>();
        ArrayList<ArrayList<String>> assignedCoursesData = new ArrayList<>();

        for (Course c : courses) {
            if (c.getTeacherId() == teacherID) {
                assignedCourses.add(c);
            }
        }

        for (Course c : assignedCourses) {
            assignedCoursesData.add(Helper.courseToStringArray(c));
        }

        return assignedCoursesData;
    }

    public Enum.Error newCourse(String name, String description, int maximumStudents, int teacherID) {
        if (maximumStudents < 0 || maximumStudents > 50) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        if (!teacherService.existTeacher(teacherID) && teacherID != -1) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.newCourse(repository.getLastID() + 1, name, description, maximumStudents, teacherID);
        
        return null;
    }

    public Enum.Error editCourse(int id, String name, String description, int maximumStudents) {
        if (maximumStudents < 0 || maximumStudents > 50) {
            return Enum.Error.WRONG_INPUT_DATA;
        }
        
        Course c = repository.getCourseByID(id);

        c.setName(name);
        c.setDescription(description);
        c.setMaximumStudents(maximumStudents);

        return null;
    }

    public Enum.Error assignTeacher(int courseID, int teacherID) {
        Course c = repository.getCourseByID(courseID);

        if (!existCourse(courseID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        if (!teacherService.existTeacher(teacherID)) {
            return Enum.Error.TEACHER_NOT_FOUND;
        }

        c.setTeacherId(teacherID);

        return null;
    }

    public Enum.Error removeCourse(int courseID) {
        if (!existCourse(courseID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.removeCourse(repository.getCourseByID(courseID));

        return null;
    }

    public ArrayList<ArrayList<String>> getCourses() {
        ArrayList<ArrayList<String>> coursesData = new ArrayList<>();
        ArrayList<Course> courses = repository.getCourses();

        for (Course c : courses) {
            coursesData.add(Helper.courseToStringArray(c));
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

}

package org.institution.app.service;

import java.io.IOException;
import java.util.ArrayList;
import org.institution.app.repository.CourseRepository;
import org.institution.app.model.Course;
import org.institution.app.util.Enum;
import org.institution.app.util.*;
import org.institution.app.serializer.course.CourseSerializer;

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

        Course course = new Course(repository.getLastID() + 1, name, description, maximumStudents, -1);

        repository.newCourse(course);
        
        return null;
    }

    public Enum.Error editCourse(int id, String name, String description, int maximumStudents) {
        if (!existCourse(id)) {
            return Enum.Error.COURSE_NOT_FOUND;
        }

        if ((maximumStudents < 0 || maximumStudents > 50) && maximumStudents != -1) {
            return Enum.Error.INVALID_STUDENT_QUOTA;
        }

        for (Course c : repository.getCourses()) {
            if (c.getName().equals(name)) {
                return Enum.Error.REPEATED_COURSE_NAME;
            }
        }
        
        Course c = repository.getCourseByID(id);

        if (name != null) {
            c.setName(name);
        }

        if (description != null) {
            c.setDescription(description);
        }

        if (maximumStudents != -1) {
            c.setMaximumStudents(maximumStudents);
        }

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

        repository.deleteCourse(repository.getCourseByID(courseID));

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

    public boolean save(CourseSerializer serializer) {
        return repository.save(serializer);
    }

    public boolean loadRepo() {
        return repository.load();
    }

    public boolean existCourse(int id) {
        for (Course c : repository.getCourses()) {
            if (c.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public boolean exportData(CourseSerializer serializer) {
        String data = serializer.export(repository.getCourses());
        String extension = serializer.getExtension();

        try {
            new FileManager().writeToFile("exported/" + extension + "/courses." + extension, data);
        } catch (IOException e) {
            return false;
        }

        return true;
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
        int teacherID = c.getTeacherId();

        if (teacherID == -1) {
            course.add("NOT ASSIGNED");
        } else {
            course.add(teacherService.getTeacherByID(teacherID).getName());

        }


        return course;
    }

    public void removeTeacherAssignedCourses(int teacherID) {
        for (Course c : repository.getCourses()) {
            if (c.getTeacherId() == teacherID) {
                c.setTeacherId(-1);
            }
        }
    }

}

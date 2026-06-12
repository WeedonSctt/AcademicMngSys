package org.institution.app.service;

// JAVA LIBS
import java.util.ArrayList;

// PROJ PACKAGES
import org.institution.app.repository.CourseRepository;
import org.institution.app.model.Course;
import org.institution.app.util.Enum;
import org.institution.app.util.*;

public class CourseService {
    private CourseRepository repository = new CourseRepository();
    private Validator validator = new Validator();

    public ArrayList<ArrayList<String>> getAssignedCourses(int teacherID) {
        ArrayList<Course> courses = repository.getCourses();
        ArrayList<Course> assignedCourses = new ArrayList<>();
        ArrayList<ArrayList<String>> assignedCoursesString = new ArrayList<>();

        for (Course c : courses) {
            if (c.getTeacherId() == teacherID) {
                assignedCourses.add(c);
            }
        }

        for (int i = 0; i < assignedCourses.size(); i++) {
            assignedCoursesString.add(Helper.courseToStringArray(assignedCourses.get(i)));
        }

        return assignedCoursesString;
    }

    public Enum.Error newCourse(String name, String description, int maximumStudents, int teacherID) {
        if (!validator.validateCourseInputData(maximumStudents, teacherID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.newCourse(repository.getLastID(), name, description, maximumStudents, teacherID);
        
        return null;
    }

    public Enum.Error editCourse(int id, String name, String description, int maximumStudents) {
        if (!validator.validateCourseInputData(maximumStudents, -1)) {
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

        // Missing verification if course exists

        if (!Helper.existTeacher(teacherID)) {
            return Enum.Error.TEACHER_NOT_FOUND;
        }

        c.setTeacherId(teacherID);

        return null;
    }

    public int getRemainingQuota(int courseID) {
        Course c = repository.getCourseByID(courseID);

        int currentRegistrations = Helper.getRegistrationsForCourse(courseID);

        int remain = c.getMaximumStudents() - currentRegistrations;

        return remain;
    }

    public boolean removeCourse(int courseID) {
        Course c = repository.getCourseByID(courseID);
        
        if (c == null) {
            return false;
        }

        repository.removeCourse(c);

        return true;
    }

    public ArrayList<ArrayList<String>> getCourses() {
        ArrayList<ArrayList<String>> coursesData = new ArrayList<>();
        ArrayList<Course> courses = repository.getCourses();

        for (Course c : courses) {
            coursesData.add(Helper.courseToStringArray(c));
        }

        return coursesData;
    }

    public ArrayList<ArrayList<String>> getCoursesByRegistration(int studentID) {
        ArrayList<ArrayList<String>> coursesData = new ArrayList<>();

        ArrayList<Integer> coursesID = new RegistrationService().getCourseByStudent(studentID);

        for (int i : coursesID) {
            coursesData.add(Helper.courseToStringArray(repository.getCourseByID(i)));
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

}

package org.institution.app.service;

// JAVA LIBS
import java.util.ArrayList;

// PROJ PACKAGES
import org.institution.app.model.*;
import org.institution.app.repository.RegistrationRepository;
import org.institution.app.util.Enum;
import org.institution.app.util.*;

public class RegistrationService {
    private final RegistrationRepository repository;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TeacherService teacherService;

    public RegistrationService(RegistrationRepository repo, CourseService cSer, StudentService sSer, TeacherService tSer) {
        this.repository = repo;
        this.courseService = cSer;
        this.studentService = sSer;
        this.teacherService = tSer;
    }

    public Enum.Error newRegistration(int studentID, int courseID) {
        // Validator
        // Missing to validate if there's yet a registration with same id's

        if (!studentService.existStudent(studentID) || !courseService.existCourse(courseID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.newRegistration(studentID, courseID);

        return null;
    }

    // maybe stupid
    public void setAverageGrade(int studentID, ArrayList<Double> grades) {
        
        double sum = 0;
        for (double g : grades) {
            sum += g;
        }

        studentService.setAverageGrade(studentID, sum/grades.size());
    }

    public void grade(int studentID, int courseID, double grade) {
        // Missing Validator
        Registration reg = repository.getRegistration(studentID, courseID);

        reg.setGrade(grade);
        ArrayList<Double> grades = repository.getStudentGrades(studentID);
        setAverageGrade(studentID, grades);
    }

    public void removeStudentRegistrations(int studentID) {
        ArrayList<Registration> registrations = repository.getRegistrations();
        ArrayList<Integer> indexes = new ArrayList<>();

        int index = 0;
        for (Registration r : registrations) {
            if (r.getStudentId() == studentID) {
                indexes.add(index);
            }

            index++;
        }

        repository.removeRegistrationsIndexes(indexes);
    }

    public ArrayList<ArrayList<String>> getEnrolledStudentsInCourse(int courseID) {
        if (!courseService.existCourse(courseID)) {
            return null;
        }

        ArrayList<Registration> registrations = repository.getRegistrations();
        ArrayList<Registration> registrationsAtCourse = new ArrayList<>();
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();

        for (Registration r : registrations) {
            if (r.getCourseId() == courseID) {
                registrationsAtCourse.add(r);
            }
        }

        for (Registration r : registrationsAtCourse) {
            studentsData.add(Helper.studentToStringArray(studentService.getStudentByID(r.getStudentId())));
        }

        return studentsData;

    }    

    public Enum.Error cancelRegistration(int studentID, int courseID) {
        if (!studentService.existStudent(studentID) || !courseService.existCourse(courseID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.removeRegistration(studentID, courseID);

        return null;
    }

    public ArrayList<ArrayList<String>> getAcademicHistory(int studentID) {
        ArrayList<Registration> registrations = repository.getRegistrations();
        ArrayList<String> resume = new ArrayList<>();
        ArrayList<ArrayList<String>> academicHistory = new ArrayList<>();

        for (Registration r : registrations) {
            if (r.getStudentId() == studentID) {
                int courseID = r.getCourseId();

                Course c = courseService.getCourseByID(courseID);

                resume.add(c.getName());

                int teacherID = c.getTeacherId();

                resume.add(teacherService.getTeacherByID(teacherID).getName());

                double grade = r.getGrade();

                resume.add(String.valueOf(grade));


                String state;
                if (grade >= 6.0) {
                    state = "APPROVED";
                } else {
                    state = "FAILED";
                }

                resume.add(state);

                academicHistory.add(resume);
            }
        }

        return academicHistory;
    }

    public boolean save() {
        return repository.saveRegistrationsToCSV();
    }

    public boolean loadRepo() {
        return repository.loadRegistrationsFromCSV();
    }

    public ArrayList<ArrayList<String>> getCoursesEnrolledByStudent(int studentID) {
        ArrayList<ArrayList<String>> coursesData = new ArrayList<>();
        ArrayList<Integer> coursesID = new ArrayList<>();

        for (Registration r : repository.getRegistrations()) {
            if (r.getStudentId() == studentID) {
                coursesID.add(r.getCourseId());
            }
        }

        for (int i : coursesID) {
            coursesData.add(Helper.courseToStringArray(courseService.getCourseByID(i)));
        }

        return coursesData;
    }

    public int getCourseRemainingQuota(int courseID) {
        if (!courseService.existCourse(courseID)) {
            return -1;
        }

        ArrayList<Registration> registrations = repository.getRegistrations();

        int enrolled = 0;
        for (Registration r : registrations) {
            if (r.getCourseId() == courseID) {
                enrolled += 1;
            }
        }

        int maxQuota = courseService.getCourseByID(courseID).getMaximumStudents();

        return maxQuota - enrolled;
    }

}

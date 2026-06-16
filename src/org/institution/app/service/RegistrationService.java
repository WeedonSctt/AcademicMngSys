package org.institution.app.service;

import java.util.ArrayList;
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
        if (!studentService.existStudent(studentID) || !courseService.existCourse(courseID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        if (!studentService.getStudentByID(studentID).isActive()) {
            return Enum.Error.INACTIVE_STUDENT;
        }

        Course c = courseService.getCourseByID(courseID);

        if (c.getTeacherId() == -1) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        for (Registration r : repository.getRegistrations()) {
            if (r.getCourseId() == courseID && r.getStudentId() == studentID) {
                return Enum.Error.ALREADY_CREATED;
            }
        }

        repository.newRegistration(studentID, courseID);

        return null;
    }

    public void setAverageGrade(int studentID, ArrayList<Double> grades) {
        
        double sum = 0;
        for (double g : grades) {
            sum += g;
        }

        studentService.setAverageGrade(studentID, sum/grades.size());
    }

    public Enum.Error grade(int studentID, int courseID, double grade) {
        if (!teacherService.existTeacher(courseService.getCourseByID(courseID).getTeacherId())) {
            return Enum.Error.TEACHER_NOT_FOUND;
        }

        if (grade < 0.0 || grade > 10.0) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        Registration reg = repository.getRegistration(studentID, courseID);

        if (reg == null) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        reg.setGrade(grade);
        setAverageGrade(studentID, repository.getStudentGrades(studentID));

        return null;
    }

    public Enum.Error removeStudentRegistrations(int studentID) {
        if (!studentService.existStudent(studentID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        ArrayList<Integer> indexes = new ArrayList<>();

        int index = 0;
        for (Registration r : repository.getRegistrations()) {
            if (r.getStudentId() == studentID) {
                indexes.add(index);
            }

            index++;
        }

        repository.removeRegistrationsIndexes(indexes);

        return null;
    }

    public Enum.Error removeCourseRegistrations(int courseID) {
        if (!courseService.existCourse(courseID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        ArrayList<Integer> indexes = new ArrayList<>();
        ArrayList<Integer> studentIDs = new ArrayList<>();

        int index = 0;
        for (Registration r : repository.getRegistrations()) {
            if (r.getCourseId() == courseID) {
                indexes.add(index);
                studentIDs.add(r.getStudentId());
            }

            index ++;
        }

        repository.removeRegistrationsIndexes(indexes);

        // update students avg grade
        for (int i : indexes) {
            setAverageGrade(i, repository.getStudentGrades(i));
        }

        return null;
    }

    public ArrayList<ArrayList<String>> getEnrolledStudentsInCourse(int courseID) {
        if (!courseService.existCourse(courseID)) {
            return null;
        }

        ArrayList<Registration> registrationsAtCourse = new ArrayList<>();
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();

        for (Registration r : repository.getRegistrations()) {
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

        if (repository.getRegistration(studentID, courseID) == null) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.removeRegistration(studentID, courseID);
        setAverageGrade(studentID, repository.getStudentGrades(studentID));

        return null;
    }

    public ArrayList<ArrayList<String>> getAcademicHistory(int studentID) {
        if (!studentService.existStudent(studentID)) {
            return null;
        }

        ArrayList<Registration> registrations = repository.getRegistrations();
        ArrayList<ArrayList<String>> academicHistory = new ArrayList<>();

        for (Registration r : registrations) {
            if (r.getStudentId() == studentID) {
                ArrayList<String> resume = new ArrayList<>();
                int courseID = r.getCourseId();

                Course c = courseService.getCourseByID(courseID);

                resume.add(c.getName());

                int teacherID = c.getTeacherId();

                if (teacherService.existTeacher(teacherID)) {
                    resume.add(teacherService.getTeacherByID(teacherID).getName());
                } else {
                    resume.add("NO_TEACHER");
                }

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

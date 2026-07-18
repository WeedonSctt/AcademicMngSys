package org.institution.app.service;

import java.io.IOException;
import java.util.ArrayList;
import org.institution.app.model.*;
import org.institution.app.repository.RegistrationRepository;
import org.institution.app.util.Enum;
import org.institution.app.util.*;
import org.institution.app.serializer.registration.RegistrationSerializer;

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
            return Enum.Error.STUDENT_OR_COURSE_NOT_FOUND;
        }

        if (!studentService.getStudentByID(studentID).isActive()) {
            return Enum.Error.INACTIVE_STUDENT;
        }

        if (getCourseRemainingQuota(courseID) <= 0) {
            return Enum.Error.QUOTA_AT_LIMIT;
        }

        if (courseService.getCourseByID(courseID).getTeacherId() == -1) {
            return Enum.Error.NOT_ASSIGNED_TEACHER;
        }

        for (Registration r : repository.getRegistrations()) {
            if (r.getCourseId() == courseID && r.getStudentId() == studentID) {
                return Enum.Error.ALREADY_CREATED;
            }
        }

        Registration r = new Registration(studentID, courseID);
        repository.newRegistration(r);

        return null;
    }

    public void setAverageGrade(int studentID, ArrayList<Double> grades) {
        double avg;

        if (grades.isEmpty()) {
            avg = 0;
        } else {
            double sum = 0;
            for (double g : grades) {
                sum += g;
            }

            avg = sum/grades.size();
        }


        studentService.setAverageGrade(studentID, avg);
    }

    public Enum.Error grade(int studentID, int courseID, double grade) {
        if (!studentService.existStudent(studentID)) {
            return Enum.Error.STUDENT_NOT_FOUND;
        }

        if (!teacherService.existTeacher(courseService.getCourseByID(courseID).getTeacherId())) {
            return Enum.Error.TEACHER_NOT_FOUND;
        }

        if (grade < 0.0 || grade > 100.0) {
            return Enum.Error.INVALID_GRADE;
        }

        Registration reg = repository.getRegistration(studentID, courseID);

        if (reg == null) {
            return Enum.Error.REGISTRATION_NOT_FOUND;
        }

        reg.setGrade(grade);
        setAverageGrade(studentID, repository.getStudentGrades(studentID));

        return null;
    }

    public Enum.Error removeStudentRegistrations(int studentID) {
        ArrayList<Registration> regs = new ArrayList<>();

        for (Registration r : repository.getRegistrations()) {
            if (r.getStudentId() == studentID) {
                regs.add(r);
            }
        }

        repository.deleteRegistrations(regs);

        return null;
    }

    public Enum.Error removeCourseRegistrations(int courseID) {
        if (!courseService.existCourse(courseID)) {
            return Enum.Error.COURSE_NOT_FOUND;
        }

        ArrayList<Registration> regs = new ArrayList<>();
        ArrayList<Integer> studentIDs = new ArrayList<>();

        for (Registration r : repository.getRegistrations()) {
            if (r.getCourseId() == courseID) {
                regs.add(r);
                studentIDs.add(r.getStudentId());
            }
        }

        repository.deleteRegistrations(regs);

        // update students avg grade
        for (int i : studentIDs) {
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
            return Enum.Error.STUDENT_OR_COURSE_NOT_FOUND;
        }

        if (repository.getRegistration(studentID, courseID) == null) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        Registration r = repository.getRegistration(studentID, courseID);
        repository.deleteRegistration(r);
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

    public boolean save(RegistrationSerializer serializer) {
        return repository.save(serializer);
    }

    public boolean loadRepo() {
        return repository.load();
    }

    public boolean exportData(RegistrationSerializer serializer) {
        String data = serializer.export(repository.getRegistrations());
        String extension = serializer.getExtension();

        try {
            new FileManager().writeToFile("exported/" + extension + "/registrations." + extension, data);
        } catch (IOException e) {
            return false;
        }

        return true;
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

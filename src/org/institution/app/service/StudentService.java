package org.institution.app.service;

import java.util.ArrayList;
import org.institution.app.util.*;
import org.institution.app.util.Enum;
import org.institution.app.repository.StudentRepository;
import org.institution.app.model.Student;

public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repo) {
        this.repository = repo;
    }

    public Enum.Error newStudent(String name, int age, String email) {
        if (!Validator.studentInputData(age, email)) {
            return Enum.Error.INVALID_AGE_OR_EMAIL;
        }

        if (existEmail(email)) {
            return Enum.Error.EMAIL_ALREADY_IN_USE;
        }

        repository.newStudent(repository.getLastID() + 1, name, age, email);

        return null;
    }

    public Enum.Error editStudentData(int id, String name, int age, String email, boolean isActive) {
        if (!existStudent(id)) {
            return Enum.Error.STUDENT_NOT_FOUND;
        }

        if (!Validator.studentInputData(age, email)) {
            return Enum.Error.INVALID_AGE_OR_EMAIL;
        }

        if (existEmail(email)) {
            return Enum.Error.EMAIL_ALREADY_IN_USE;
        }
        
        Student s = repository.getStudentByID(id);

        if (name != null) {
            s.setName(name);
        }

        if (age != -1) {
            s.setAge(age);
        }

        if (email != null) {
            s.setEmail(email);
        }

        s.setIsActive(isActive);

        return null;
    }

    public Enum.Error deleteStudent(int studentID) {
        if (!existStudent(studentID)) {
            return Enum.Error.STUDENT_NOT_FOUND;
        }

        Student s = repository.getStudentByID(studentID);
        
        if (s.isActive()) {
            return Enum.Error.ACTIVE_STUDENT;
        }

        repository.deleteStudent(studentID);
        
        return null;
    }

    public ArrayList<String> searchByName(String name) {
        Student s = repository.getStudentByName(name);

        return Helper.studentToStringArray(s);
        
    }

    public ArrayList<String> searchByID(int id) {
        Student s = repository.getStudentByID(id);

        return Helper.studentToStringArray(s);
    }

    public void setAverageGrade(int id, double avg) {
        Student s = repository.getStudentByID(id);
        s.setAverageGrade(avg);
    }

    public ArrayList<ArrayList<String>> sortAlphabetically() {
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();

        ArrayList<Student> students = Helper.sortAlphabetically(repository.getStudents());

        for (Student s : students) {
            studentsData.add(Helper.studentToStringArray(s));
        }

        Helper.sortByID(students);

        return studentsData;
    }

    public ArrayList<ArrayList<String>> sortByAverageGrade() {
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();

        ArrayList<Student> students = Helper.sortByAverageGrade(repository.getStudents());

        for (Student s : students) {
            studentsData.add(Helper.studentToStringArray(s));
        }

        Helper.sortByID(students);

        return studentsData;
    }

    public ArrayList<ArrayList<String>> getStudents() {
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();
        ArrayList<Student> students = repository.getStudents();

        for (Student s : students) {
            ArrayList<String> student = Helper.studentToStringArray(s);

            if (s.getAverageGrade() == -0.1) {
                student.remove(4);
                student.add(4, "UNDEFINED");
            }

            studentsData.add(student);
        }
        
        return studentsData;
    }

    public boolean save() {
        return repository.saveStudentsToCSV();
    }

    public boolean loadRepo() {
        return repository.loadStudentsFromCSV();
    }

    public boolean existStudent(int id) {
        for (Student s : repository.getStudents()) {
            if (s.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public Student getStudentByID(int id) {
        ArrayList<Student> students = repository.getStudents();

        for (Student s: students) {
            if (s.getId() == id) {
                return s;
            }
        }

        return null;
    }

    public boolean existEmail(String email) {
        for (Student s : repository.getStudents()) {
            if (s.getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }

}

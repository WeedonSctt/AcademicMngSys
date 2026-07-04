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

        Student s = new Student(repository.getLastID() + 1, name, age, email);
        repository.newStudent(s);

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

        repository.deleteStudent(s);
        
        return null;
    }

    public ArrayList<ArrayList<String>> searchByName(String name) {
        ArrayList<Student> students = repository.getStudentsByName(name);

        if (students == null) {
            return null;
        }

        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();

        for (Student s : students) {
            studentsData.add(Helper.studentToStringArray(s));
        }

        return studentsData;
    }

    public ArrayList<String> searchByID(int id) {
        if (!existStudent(id)) {
            return null;
        }

        Student s = repository.getStudentByID(id);

        return Helper.studentToStringArray(s);
    }

    public ArrayList<String> searchByEmail(String email) {
        Student s = repository.getStudentByEmail(email);

        if (s == null) {
            return null;
        }

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
            studentsData.add(Helper.studentToStringArray(s));
        }
        
        return studentsData;
    }

    public boolean save() {
        return repository.save();
    }

    public boolean loadRepo() {
        return repository.load();
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
        if (repository.getStudentByEmail(email) != null) {
            return true;
        }

        return false;
    }

}

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
            return Enum.Error.INVALID_INPUT_DATA;
        }

        for (Student s : repository.getStudents()) {
            if (s.getEmail().equals(email)) {
                return Enum.Error.ALREADY_CREATED;
            }
        }

        repository.newStudent(repository.getLastID() + 1, name, age, email);

        return null;
    }

    public Enum.Error editStudentData(int id, String name, int age, String email, boolean isActive) {
        if (!Validator.studentInputData(age, email)) {
            return Enum.Error.WRONG_INPUT_DATA;
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

    // What the fuck is this??
    public void setAverageGrade(int id, double avg) {
        Student s = repository.getStudentByID(id);
        s.setAverageGrade(avg);
    }

    // Sort sorts all the array, not only the return statement. that should not happen
    public ArrayList<ArrayList<String>> sortAlphabetically() {
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();
        
        ArrayList<Student> students = repository.getStudents();
        students = Helper.sortAlphabetically(students);

        for (Student s : students) {
            studentsData.add(Helper.studentToStringArray(s));
        }

        return studentsData;
    }

    public ArrayList<ArrayList<String>> sortByAverageGrade() {
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();

        ArrayList<Student> students = repository.getStudents();
        students =  Helper.sortByAverageGrade(students);

        for (Student s : students) {
            studentsData.add(Helper.studentToStringArray(s));
        }

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

}

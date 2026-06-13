package org.institution.app.service;

// JAVA LIBS
import java.util.ArrayList;

// PROJ PACKAGES
import org.institution.app.util.*;
import org.institution.app.util.Enum;
import org.institution.app.repository.StudentRepository;
import org.institution.app.model.Student;

public class StudentService {
    Validator validator = new Validator();
    StudentRepository repository = new StudentRepository();

    // ENUM.ERROR PROVISIONAL
    public Enum.Error newStudent(String name, int age, String email) {
        if (!validator.validateStudentInputData(name ,age, email)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        // Maybe check if there's another student with same metadata?
        
        repository.newStudent(repository.getLastID() + 1, name, age, email);

        return null;

    }

    public Enum.Error editStudentData(int id, String name, int age, String email, boolean isActive) {
        if (!validator.validateStudentInputData(name, age, email)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }
        
        Student s = repository.getStudentByID(id);

        s.setName(name);
        s.setAge(age);
        s.setEmail(email);
        s.setIsActive(isActive);

        return null;
    }

    public Enum.Error deleteStudent(int studentID) {
        Student s = repository.getStudentByID(studentID);
        
        if (s.isActive() == true) {
            return Enum.Error.ACTIVE_STUDENT;
        }

        repository.deleteStudent(studentID);
        
        return null;
    }

    public ArrayList<String> searchByName(String name) {
        Student s = repository.getStudentByName(name);

        ArrayList<String> studentData = Helper.studentToStringArray(s);

        return studentData;
        
    }

    public ArrayList<String> searchByID(int id) {
        Student s = repository.getStudentByID(id);

        ArrayList<String> studentData = Helper.studentToStringArray(s);

        return studentData;
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

        for (int i = 0; i < students.size(); i++) {
            studentsData.add(Helper.studentToStringArray(students.get(i)));
        }

        return studentsData;
    }

    public ArrayList<ArrayList<String>> sortByAverageGrade() {
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();

        ArrayList<Student> students = repository.getStudents();
        students =  Helper.sortByAverageGrade(students);

        for (int i = 0; i < students.size(); i++) {
            studentsData.add(Helper.studentToStringArray(students.get(i)));
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

}

package org.institution.app.util;

import java.util.ArrayList;
import java.util.Comparator;
import org.institution.app.model.*;

public class Helper {
    public static ArrayList<String> studentToStringArray(Student s) {
        ArrayList<String> studentData = new ArrayList<>();

        studentData.add(String.valueOf(s.getId()));
        studentData.add(s.getName());
        studentData.add(String.valueOf(s.getAge()));
        studentData.add(s.getEmail());
        studentData.add(String.valueOf(s.getAverageGrade()));
        studentData.add(String.valueOf(s.isActive()));

        return studentData;
    }

    public static ArrayList<String> teacherToStringArray(Teacher t) {
        ArrayList<String> teacherData = new ArrayList<>();

        teacherData.add(String.valueOf(t.getID()));
        teacherData.add(t.getName());
        teacherData.add(t.getDepartment());
        teacherData.add(t.getEmail());

        return teacherData;
    }
    
    public static ArrayList<String> courseToStringArray(Course c) {
        ArrayList<String> courseData = new ArrayList<>();

        courseData.add(String.valueOf(c.getId()));
        courseData.add(c.getName());
        courseData.add(c.getDescription());
        courseData.add(String.valueOf(c.getMaximumStudents()));
        courseData.add(String.valueOf(c.getTeacherId()));

        return courseData;
    }

    public static ArrayList<String> registrationToStringArray(Registration r) {
        ArrayList<String> registrationData = new ArrayList<>();

        registrationData.add(String.valueOf(r.getStudentId()));
        registrationData.add(String.valueOf(r.getCourseId()));
        registrationData.add(String.valueOf(r.getGrade()));

        return registrationData;
    }

    public static ArrayList<Student> sortAlphabetically(ArrayList<Student> students) {
        students.sort(Comparator.comparing(Student::getName));
        return students;
    }

    public static ArrayList<Student> sortByAverageGrade(ArrayList<Student> students) {
        students.sort(Comparator.comparing(Student::getAverageGrade));
        return students;
    }

    public static ArrayList<Student> sortByID(ArrayList<Student> students) {
        students.sort(Comparator.comparing(Student::getId));
        return students;
    }

    public static String stringArrayToCSV(ArrayList<String> array) {
        StringBuilder str = new StringBuilder();
        
        for (String s : array) {
            str.append(s);
            str.append(",");
        }
        
        return str.toString();
    }
    
}

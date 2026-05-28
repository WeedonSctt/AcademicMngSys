package org.institution.app.util;

// JAVA LIBS
import java.util.ArrayList;
import java.util.Comparator;

// PROJ PACKAGES
import org.institution.app.model.*;
import org.institution.app.repository.*;

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
        ArrayList<String> regisrtationData = new ArrayList<>();

        regisrtationData.add(String.valueOf(r.getStudentId()));
        regisrtationData.add(String.valueOf(r.getCourseId()));
        regisrtationData.add(String.valueOf(r.getGrade()));

        return regisrtationData;
    }

    public static ArrayList<Student> sortAlphabetically(ArrayList<Student> students) {
        students.sort(Comparator.comparing(Student::getName));
        return students;
    }

    public static ArrayList<Student> sortByAverageGrade(ArrayList<Student> students) {
        students.sort(Comparator.comparing(Student::getAverageGrade));
        return students;
    }

    public static boolean existTeacher(int id) {
        for (Teacher t : new TeacherRepository().getTeachers()) {
            if (t.getID() == id) {
                return true;
            }
        }

        return false;
    }
    
    public static boolean existStudent(int id) {
        for (Student s : new StudentRepository().getStudents()) {
            if (s.getId() == id) {
                return true;
            }
        }

        return false;
    }
    
    public static boolean existCourse(int id) {
        for (Course c : new CourseRepository().getCourses()) {
            if (c.getId() == id) {
                return true;
            }
        }

        return false;
    }
    
    public static ArrayList<ArrayList<String>> studentsInCourse(ArrayList<Registration> courseRegistrations, int courseID) {
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<ArrayList<String>> studentsData = new ArrayList<>();

        for (Registration r : courseRegistrations) {
            students.add(new StudentRepository().getStudentByID(r.getStudentId()));
        }

        for (int i = 0; i < studentsData.size(); i++) {
            studentsData.add(studentToStringArray(students.get(i)));
        }

        return studentsData;

    }

    public static int getRegistrationsForCourse(int courseID) {
        ArrayList<Registration> registrations = new RegistrationRepository().getRegistrations();

        int count = 0;
        for (Registration r : registrations) {
            if (r.getCourseId() == courseID) {
                count++;
            }
        }

        return count;
    }

    public static String stringArrayToCSV(ArrayList<String> array) {
        String str = "";
        
        for (String s : array) {
            str += s;
            str += ",";
        }
        
        return str;
    }
    
}

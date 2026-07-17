package org.institution.app.ui;

import java.util.ArrayList;
import java.util.Scanner;
import org.institution.app.util.Enum;

public class UiHelper {


    public static void showStudent(ArrayList<String> studentData) {
        String active;
        String averageGrade;

        if (studentData.get(5).equals("true")) {
            active = "ACTIVE";
        } else {
            active = "INACTIVE";
        }

        if (studentData.get(4).equals("-0.1")) {
            averageGrade = "UNDEFINED";
        } else {
            averageGrade = studentData.get(4);
        }
        
        System.out.println(
            "|  " +
            studentData.get(0) + "  |  " +
            studentData.get(1) + "  |  " +
            studentData.get(2) + "  |  " +
            studentData.get(3) + "  |  " +
            averageGrade + "  |  " +
            active + "  |"
        );

    }
    public static void showTeacher(ArrayList<String> teacherData) {
        System.out.println(
            "|  " +
            teacherData.get(0) + "  |  " +
            teacherData.get(1) + "  |  " +
            teacherData.get(2) + "  |  " +
            teacherData.get(3) + "  |"
        );
    }
    public static void showCourse(ArrayList<String> courseData) {
        System.out.println(
            "|  " +
            courseData.get(0) + "  |  " +
            courseData.get(1) + "  |  " +
            courseData.get(2) + "  |  " +
            courseData.get(3) + "  |  " +
            courseData.get(4) + "  |  "
        );
    }

    public static void showStudents(ArrayList<ArrayList<String>> studentsData) {
        showResume("STUDENT");

        for (ArrayList<String> student : studentsData) {
            showStudent(student);
        }
    }
    public static void showTeachers(ArrayList<ArrayList<String>> teachersData) {
        showResume("TEACHER");

        for (ArrayList<String> array : teachersData) {
            showTeacher(array);
        }
    }
    public static void showCourses(ArrayList<ArrayList<String>> coursesData) {
        showResume("COURSE");

        for (ArrayList<String> course : coursesData) {
            showCourse(course);
        }
    }

    public static boolean manageError(Enum.Error error) {
        if (error != null) {
            System.out.println("Error>> " + error);
            return false;
        }

        return true;
    }

    public static void showResume(String model) {
        switch (model) {
            case "STUDENT":
                System.out.println("|  ID  |    NAME    |  AGE  |   E-MAIL   |  AVERAGE GRADE  |   STATE   |\n");
                break;
            case "TEACHER":
                System.out.println("|  ID  |    NAME    |    DEPARTMENT    |   E-MAIL   |\n");
                break;
            case "COURSE":
                System.out.println("|  ID  |    NAME    |       DESCRIPTION       |  MAX STUDENTS  |    TEACHER    |\n");
                break;
            default:
                System.out.println("ERROR IN SHOW RESUME");
        }
    }

    public static void pressToContinue(Scanner sc) {
        System.out.print("\n[PRESS <ENTER> TO CONTINUE]");
        sc.nextLine();
    }
}

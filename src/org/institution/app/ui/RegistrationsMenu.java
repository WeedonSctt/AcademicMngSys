package org.institution.app.ui;

import java.util.Scanner;
import org.institution.app.service.RegistrationService;
import org.institution.app.service.TeacherService;
import org.institution.app.service.CourseService;
import org.institution.app.service.StudentService;
import org.institution.app.util.InputHelper;
import java.util.ArrayList;

public class RegistrationsMenu {
    public static void registrationsMenu(InputHelper inputHelper, RegistrationService registrationService, StudentService studentService, CourseService courseService, TeacherService teacherService, Scanner sc) {
        while (true) {

            System.out.print("\n\n");
            System.out.print("                      R E G I S T R A T I O N S   M E N U");
            System.out.print("\n\n");

            System.out.println("                            <Choose an option>");

            System.out.println("1. Create Registration");
            System.out.println("2. Cancel Registration");
            System.out.println("3. Set Grade");
            System.out.println("4. Show Academic History");
            System.out.println("5. Back");

            int option = inputHelper.inputInteger(sc, "> ", false);

            switch (option) {
                case 1: {
                    UiHelper.showStudents(studentService.getStudents());
                    int studentID = inputHelper.inputInteger(sc, "ID of student to register> ", false);

                    System.out.println();

                    UiHelper.showCourses(courseService.getCourses());
                    int courseID = inputHelper.inputInteger(sc, "ID of course to register> ", false);

                    System.out.println();

                    UiHelper.manageError(registrationService.newRegistration(studentID, courseID));

                    break;
                }
                case 2: {
                    UiHelper.showStudents(studentService.getStudents());
                    int studentID = inputHelper.inputInteger(sc, "Student who cancels registration> ", false);

                    System.out.println();

                    UiHelper.showCourses(registrationService.getCoursesEnrolledByStudent(studentID));
                    int courseID = inputHelper.inputInteger(sc, "From which course you want to cancel registration> ", false);

                    System.out.println();

                    UiHelper.manageError(registrationService.cancelRegistration(studentID, courseID));

                    break;
                }
                case 3: {
                    UiHelper.showCourses(courseService.getCourses());
                    int courseID = inputHelper.inputInteger(sc, "From which course you want to set grade> ", false);

                    System.out.println();

                    UiHelper.showStudents(registrationService.getEnrolledStudentsInCourse(courseID));
                    int studentID = inputHelper.inputInteger(sc, "ID of student to grade> ", false);

                    System.out.println();

                    double grade = inputHelper.inputDouble(sc, "Grade> ");

                    UiHelper.manageError(registrationService.grade(studentID, courseID, grade));

                    break;
                }
                case 4: {
                    UiHelper.showStudents(studentService.getStudents());
                    int studentID = inputHelper.inputInteger(sc, "ID of student to show academic history> ", false);

                    ArrayList<ArrayList<String>> academicHistory = registrationService.getAcademicHistory(studentID);

                    if (academicHistory == null) {
                        System.out.println("Error: no student");
                        break;
                    }

                    System.out.println();

                    showAcademicHistory(academicHistory, studentService, studentID);

                    break;
                }
                case 5: {
                    return;
                }
                default:
                    System.out.println("Invalid Option!");
            }

            UiHelper.pressToContinue(sc);
        }
    }

    public static void showAcademicHistory(ArrayList<ArrayList<String>> academicHistory, StudentService studentService, int studentID) {
        System.out.print("\n                       A C A D E M I C   H I S T O R Y\n\n");

        System.out.println("<Student>");
        UiHelper.showResume("STUDENT");
        UiHelper.showStudent(studentService.searchByID(studentID));

        System.out.println("\n<Registered in>");

        for (ArrayList<String> resume : academicHistory) {
            System.out.println("Course's name: " + resume.get(0));
            System.out.println("Teacher's name: " + resume.get(1));
            System.out.println("Grade: " + resume.get(2));
            System.out.println("State: " + resume.get(3));
            
            System.out.println();
        }

        

    }
}

package org.institution.app.ui;

import java.util.Scanner;
import org.institution.app.service.RegistrationService;
import org.institution.app.service.StudentService;
import org.institution.app.util.InputHelper;
import java.util.ArrayList;

public class StudentsMenu {
    public static void studentsMenu(InputHelper inputHelper, StudentService studentService, RegistrationService registrationService, Scanner sc) {
        while (true) {

            System.out.print("\n\n");
            System.out.print("                              S T U D E N T S   M E N U");
            System.out.print("\n\n");
            System.out.println("                                <Choose an option>");

            System.out.println("1. Create Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Search By Name");
            System.out.println("5. Search By ID");
            System.out.println("6. Show All");
            System.out.println("7. List By Average");
            System.out.println("8. List Alphabetically");
            System.out.println("9. Back");

            int option = inputHelper.inputInteger(sc, "> ", false);
            System.out.println();

            switch (option) {
                case 1: {
                    String name = inputHelper.inputString(sc, "Student's name> ", false);
                    int age = inputHelper.inputInteger(sc, "Student's age> ", false);
                    String email = inputHelper.inputString(sc, "Student's email> ", false);
                    System.out.println();

                    if (UiHelper.manageError(studentService.newStudent(name, age, email))) {
                        UiHelper.showResume("STUDENT");
                        UiHelper.showStudent(studentService.searchByEmail(email));
                    }

                    break;
                }
                case 2: {
                    UiHelper.showStudents(studentService.getStudents());
                    int id = inputHelper.inputInteger(sc, "Student's id to edit> ", false);
                    System.out.println();

                    String name = inputHelper.inputString(sc, "Student's name> ", true);
                    int age = inputHelper.inputInteger(sc, "Student's age> ", true);
                    String email = inputHelper.inputString(sc, "Student's email> ", true);
                    boolean isActive = inputHelper.inputBoolean(sc, "Active user? [y/n]> ");
                    System.out.println();

                    if (UiHelper.manageError(studentService.editStudentData(id, name, age, email, isActive))) {
                        UiHelper.showResume("STUDENT");
                        UiHelper.showStudent(studentService.searchByID(id));
                    }

                    break;
                }
                case 3: {
                    UiHelper.showStudents(studentService.getStudents());
                    int id = inputHelper.inputInteger(sc, "Student's id to delete> ", false);
                    System.out.println();

                    if (UiHelper.manageError(studentService.deleteStudent(id))) {
                        UiHelper.manageError(registrationService.removeStudentRegistrations(id));
                    }

                    break;
                }
                case 4: {
                    String name = inputHelper.inputString(sc, "Name of student to search> ", false);

                    ArrayList<ArrayList<String>> students = studentService.searchByName(name);

                    if (students == null) {
                        System.out.println("Failed to search student");
                        break;
                    }

                    System.out.println();

                    UiHelper.showStudents(students);

                    break;
                }
                case 5: {
                    int id = inputHelper.inputInteger(sc, "ID of student to search> ", false);

                    ArrayList<String> student = studentService.searchByID(id);

                    if (student == null) {
                        System.out.println("Failed to search student");
                        break;
                    }

                    System.out.println();

                    UiHelper.showResume("STUDENT");
                    UiHelper.showStudent(student);

                    break;
                }
                case 6: {
                    UiHelper.showStudents(studentService.getStudents());

                    break;
                }
                case 7: {
                    System.out.println();

                    UiHelper.showStudents(studentService.sortByAverageGrade());

                    break;
                }
                case 8: {
                    System.out.println();

                    UiHelper.showStudents(studentService.sortAlphabetically());

                    break;
                }
                case 9: {
                    return;
                }
                default:
                    System.out.println("Invalid Option!");
            }

            UiHelper.pressToContinue(sc);
        }
    }
    
}

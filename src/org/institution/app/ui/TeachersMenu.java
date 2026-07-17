package org.institution.app.ui;

import java.util.Scanner;
import org.institution.app.service.TeacherService;
import org.institution.app.service.CourseService;
import org.institution.app.util.InputHelper;
import java.util.ArrayList;

public class TeachersMenu {
    public static void teachersMenu(InputHelper inputHelper, TeacherService teacherService, CourseService courseService, Scanner sc) {
        while (true) {
            System.out.print("\n\n");
            System.out.print("                      T E A C H E R S   M E N U");
            System.out.print("\n\n");

            System.out.println("                        <Choose an option>");
            System.out.println("1. Create Teacher");
            System.out.println("2. Edit Teacher Data");
            System.out.println("3. Delete Teacher");
            System.out.println("4. Search By Name");
            System.out.println("5. Search By ID");
            System.out.println("6. Show All");
            System.out.println("7. Show Assigned Courses");
            System.out.println("8. Back");

            int option = inputHelper.inputInteger(sc, "> ", false);

            switch (option) {
                case 1: {
                    String name = inputHelper.inputString(sc, "Teacher's name> ", false);
                    String department = inputHelper.inputString(sc, "Teacher's department> ", false);
                    String email = inputHelper.inputString(sc, "Teacher's email> ", false);

                    System.out.println();

                    if (UiHelper.manageError(teacherService.newTeacher(name, department, email))) {
                        UiHelper.showResume("TEACHER");
                        UiHelper.showTeacher(teacherService.searchTeacherByName(name));
                    }

                    break;
                }
                case 2: {
                    UiHelper.showTeachers(teacherService.getTeachers());
                    int id = inputHelper.inputInteger(sc, "Teacher's id to edit> ", false);
                    System.out.println();

                    String name = inputHelper.inputString(sc, "Teacher's name> ", true);
                    String department = inputHelper.inputString(sc, "Teacher's department> ", true);
                    String email = inputHelper.inputString(sc, "Teacher's email> ", true);

                    System.out.println();

                    if (UiHelper.manageError(teacherService.editTeacherData(id, name, department, email))) {
                        UiHelper.showResume("TEACHER");
                        UiHelper.showTeacher(teacherService.searchTeacherByID(id));
                    }

                    break;
                }
                case 3: {
                    UiHelper.showTeachers(teacherService.getTeachers());
                    int id = inputHelper.inputInteger(sc, "ID from teacher to delete> ", false);

                    System.out.println();

                    if (UiHelper.manageError(teacherService.removeTeacher(id))) {
                        courseService.removeTeacherAssignedCourses(id);
                    }

                    break;
                }
                case 4: {
                    String name = inputHelper.inputString(sc, "Teacher's name to search> ", false);

                    ArrayList<String> teacher = teacherService.searchTeacherByName(name);

                    if (teacher == null) {
                        System.out.println("Could not find teacher");
                        break;
                    }

                    System.out.println();

                    UiHelper.showResume("TEACHER");
                    UiHelper.showTeacher(teacher);

                    break;
                }
                case 5: {
                    int id = inputHelper.inputInteger(sc, "Teacher's ID to search> ", false);

                    ArrayList<String> teacher = teacherService.searchTeacherByID(id);

                    if (teacher == null) {
                        System.out.println("Could not find teacher");
                        break;
                    }

                    System.out.println();

                    UiHelper.showResume("TEACHER");
                    UiHelper.showTeacher(teacher);

                    break;
                }
                case 6: {
                    UiHelper.showTeachers(teacherService.getTeachers());
                    break;
                }
                case 7: {
                    UiHelper.showTeachers(teacherService.getTeachers());
                    int id = inputHelper.inputInteger(sc, "ID of teacher to show assigned courses> ", false);

                    if (!teacherService.existTeacher(id)) {
                        System.out.println("Error: TEACHER DOES NOT EXIST");
                        break;
                    }

                    System.out.println();

                    UiHelper.showCourses(courseService.getAssignedCourses(id));

                    break;
                }
                case 8: {
                    return;
                }
                default: {
                    System.out.println("Invalid Option!");
                }
            }

            UiHelper.pressToContinue(sc);
        }
    }
}

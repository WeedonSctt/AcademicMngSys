package org.institution.app.ui;

import java.util.Scanner;
import org.institution.app.service.RegistrationService;
import org.institution.app.service.TeacherService;
import org.institution.app.service.CourseService;
import org.institution.app.util.InputHelper;
import java.util.ArrayList;

public class CoursesMenu {
    public static void coursesMenu(InputHelper inputHelper, CourseService courseService, RegistrationService registrationService, TeacherService teacherService, Scanner sc) {
        while(true) {

            System.out.print("\n\n");
            System.out.print("                      C O U R S E S   M E N U");
            System.out.print("\n\n");

            System.out.println("                       <Choose an option>");

            System.out.println("1. Create Course");
            System.out.println("2. Edit Course");
            System.out.println("3. Delete Course");
            System.out.println("4. Assign Teacher to Course");
            System.out.println("5. Show Courses");
            System.out.println("6. Show Students in Course");
            System.out.println("7. Show Remaining Quote");
            System.out.println("8. Back");

            int option = inputHelper.inputInteger(sc, "> ", false);

            switch (option) {
                case 1: {
                    String name = inputHelper.inputString(sc, "Course's name> ", false);
                    String description = inputHelper.inputString(sc, "Course's description> ", false);
                    int maximumStudents = inputHelper.inputInteger(sc, "Course's maximum quota> ", false);
                    System.out.println();

                    if (UiHelper.manageError(courseService.newCourse(name, description, maximumStudents))) {
                        UiHelper.showResume("COURSE");
                        UiHelper.showCourse(courseService.searchCourseByName(name));
                    }

                    break;
                }
                case 2: {
                    UiHelper.showCourses(courseService.getCourses());
                    int id = inputHelper.inputInteger(sc, "ID of course to edit> ", false);
                    System.out.println();

                    String name = inputHelper.inputString(sc, "New course's name> ", true);
                    String description = inputHelper.inputString(sc, "New course's description> ", true);
                    int maximumStudents = inputHelper.inputInteger(sc, "New course's maximum quota> ", true);
                    System.out.println();

                    if (UiHelper.manageError(courseService.editCourse(id, name, description, maximumStudents))) {
                        UiHelper.showResume("COURSE");
                        UiHelper.showCourse(courseService.searchCourseByID(id));
                    }

                    break;
                }
                case 3: {
                    UiHelper.showCourses(courseService.getCourses());
                    int id = inputHelper.inputInteger(sc, "ID of course to delete> ", false);
                    System.out.println();

                    if (UiHelper.manageError(registrationService.removeCourseRegistrations(id))) {
                        UiHelper.manageError(courseService.removeCourse(id));
                    }

                    break;
                }
                case 4: {
                    UiHelper.showCourses(courseService.getCourses());
                    int courseID = inputHelper.inputInteger(sc, "ID of course to assign teacher> ", false);

                    System.out.println();

                    UiHelper.showTeachers(teacherService.getTeachers());
                    int teacherID = inputHelper.inputInteger(sc, "ID of teacher to assign course> ", false);

                    System.out.println();

                    UiHelper.manageError(courseService.assignTeacher(courseID, teacherID));

                    break;
                }
                case 5: {
                    UiHelper.showCourses(courseService.getCourses());
                    break;
                }
                case 6: {
                    UiHelper.showCourses(courseService.getCourses());
                    int courseID = inputHelper.inputInteger(sc, "ID from which course to show students> ", false);

                    ArrayList<ArrayList<String>> students = registrationService.getEnrolledStudentsInCourse(courseID);

                    if (students == null) {
                        System.out.println("Error: Null");
                        break;
                    }

                    System.out.println();

                    UiHelper.showStudents(students);

                    break;
                }
                case 7: {
                    UiHelper.showCourses(courseService.getCourses());
                    int courseID = inputHelper.inputInteger(sc, "ID of course to show remaining quota> ", false);

                    int remain = registrationService.getCourseRemainingQuota(courseID);

                    if (remain != -1) {
                        System.out.println("There are " + remain + " registrations left in course");
                    } else {
                        System.out.println("Error: no course");
                    }

                    System.out.println();

                    break;
                }
                case 8: {
                    return;
                }
                default:
                    System.out.println("Invalid Option!");
            }

            UiHelper.pressToContinue(sc);
        }
    }
}

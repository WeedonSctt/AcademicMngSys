package org.institution.app;

import java.util.Scanner;
import java.util.ArrayList;
import org.institution.app.repository.*;
import org.institution.app.service.*;
import org.institution.app.util.Enum;
import org.institution.app.util.*;

public class Main {
    final private static Scanner sc = new Scanner(System.in);

    // repositories initialization
    final private static StudentRepository studentRepository = new StudentRepository();
    final private static TeacherRepository teacherRepository = new TeacherRepository();
    final private static CourseRepository courseRepository = new CourseRepository();
    final private static RegistrationRepository registrationRepository = new RegistrationRepository();

    // services initialization | dependencies injection
    final private static StudentService studentService = new StudentService(studentRepository);
    final private static TeacherService teacherService = new TeacherService(teacherRepository);
    final private static CourseService courseService = new CourseService(courseRepository, teacherService);
    final private static RegistrationService registrationService = new RegistrationService(registrationRepository, courseService, studentService, teacherService);

    final private static InputHelper inputHelper = new InputHelper();

    static void main() {
        
        boolean flow = true;

        if (!studentService.loadRepo() |
        !teacherService.loadRepo() |
        !courseService.loadRepo() |
        !registrationService.loadRepo()) {
            System.out.println("> Error: Something went wrong while loading data.");
        } else {
            System.out.println("> Loading data...");
        }
        
        do {
            System.out.print("\n\n");
            System.out.print("                  A C A D E M I C   M A N A G E M E N T   S Y S T E M");
            System.out.print("\n\n");
            System.out.println("                                <Choose an option>");

            System.out.println("1. Students");
            System.out.println("2. Teachers");
            System.out.println("3. Courses");
            System.out.println("4. Registrations");
            System.out.println("5. Reports");
            System.out.println("6. Save");
            System.out.println("7. Exit");
            
            // "$>" is the placeholder to show
            int option = inputHelper.inputInteger(sc, "> ", false);

            switch (option) {
                case 1:
                    studentsMenu();
                    break;
                case 2:
                    teachersMenu();
                    break;
                case 3:
                    coursesMenu();
                    break;
                case 4:
                    registrationsMenu();
                    break;
                case 5:
                    reportsMenu();
                    break;
                case 6:
                    save();
                    pressToContinue();
                    break;
                case 7:
                    flow = false;
                    save();
                    break;
                default:
                    System.out.println("Invalid Option!");
                    pressToContinue();
                    break;
            }
        } while (flow);

        sc.close();
    }

    public static void studentsMenu() {
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

        switch (option) {
            case 1: {
                String name = inputHelper.inputString(sc, "Student's name> ", false);
                int age = inputHelper.inputInteger(sc, "Student's age> ", false);
                String email = inputHelper.inputString(sc, "Student's email> ", false);

                if (manageError(studentService.newStudent(name, age, email))) {
                    showResume("STUDENT");
                    showStudent(studentService.searchByName(name));
                }

                break;
            }
            case 2: {
                showStudents(studentService.getStudents());
                int id = inputHelper.inputInteger(sc, "Student's id to edit> ", false);

                String name = inputHelper.inputString(sc, "Student's name> ", true);
                int age = inputHelper.inputInteger(sc, "Student's age> ", true);
                String email = inputHelper.inputString(sc, "Student's email> ", true);
                boolean isActive = inputHelper.inputBoolean(sc, "Active user? [y/n]> ");
            
                if (manageError(studentService.editStudentData(id, name, age, email, isActive))) {
                    showResume("STUDENT");
                    showStudent(studentService.searchByID(id));
                }

                break;
            }
            case 3: {
                showStudents(studentService.getStudents());
                int id = inputHelper.inputInteger(sc, "Student's id to delete> ", false);

                if (manageError(studentService.deleteStudent(id))) {
                    manageError(registrationService.removeStudentRegistrations(id));
                }

                break;
            }
            case 4: {
                String name = inputHelper.inputString(sc, "Name of student to search> ", false);

                ArrayList<String> student = studentService.searchByName(name);

                if (student == null) {
                    System.out.println("Failed to search student");
                    break;
                }

                showResume("STUDENT");
                showStudent(student);

                break;
            }
            case 5: {
                int id = inputHelper.inputInteger(sc, "ID of student to search> ", false);

                ArrayList<String> student = studentService.searchByID(id);

                if (student == null) {
                    System.out.println("Failed to search student");
                    break;
                }

                showResume("STUDENT");
                showStudent(student);

                break;
            }
            case 6: {
                showStudents(studentService.getStudents());
                
                break;
            }
            case 7: { 
                showStudents(studentService.sortByAverageGrade());
                
                break; 
            }
            case 8: { 
                showStudents(studentService.sortAlphabetically());

                break;
            }
            case 9: {
                return;
            }
            default:
                System.out.println("Invalid Option!");
                pressToContinue();
                studentsMenu();
        }

        pressToContinue();
    }
    
    public static void teachersMenu() {
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

                if (manageError(teacherService.newTeacher(name, department, email))) {
                    showResume("TEACHER");
                    showTeacher(teacherService.searchTeacherByName(name));
                }

                break;
            }
            case 2: {
                showTeachers(teacherService.getTeachers());
                int id = inputHelper.inputInteger(sc, "Teacher's id to edit> ", false);

                String name = inputHelper.inputString(sc, "Teacher's name> ", true);
                String department = inputHelper.inputString(sc, "Teacher's department> ", true);
                String email = inputHelper.inputString(sc, "Teacher's email> ", true);

                if (manageError(teacherService.editTeacherData(id, name, department, email))) {
                    showResume("TEACHER");
                    showTeacher(teacherService.searchTeacherByID(id));
                }

                break;
            }
            case 3: {
                showTeachers(teacherService.getTeachers());
                int id = inputHelper.inputInteger(sc, "ID from teacher to delete> ", false);

                manageError(teacherService.removeTeacher(id));

                break;
            }
            case 4: {
                String name = inputHelper.inputString(sc, "Teacher's name to search> ", false);

                ArrayList<String> teacher = teacherService.searchTeacherByName(name);

                if (teacher == null) {
                    System.out.println("Could not find teacher");
                    break;
                }

                showResume("TEACHER");
                showTeacher(teacher);

                break;
            }
            case 5: {
                int id = inputHelper.inputInteger(sc, "Teacher's ID to search> ", false);

                ArrayList<String> teacher = teacherService.searchTeacherByID(id);

                if (teacher == null) {
                    System.out.println("Could not find teacher");
                    break;
                }

                showResume("TEACHER");
                showTeacher(teacher);

                break;
            }
            case 6: {
                showTeachers(teacherService.getTeachers());
                break;
            }
            case 7: {
                showTeachers(teacherService.getTeachers());
                int id = inputHelper.inputInteger(sc, "ID of teacher to show assigned courses> ", false);

                if (!teacherService.existTeacher(id)) {
                    System.out.println("Error: TEACHER DOES NOT EXIST");
                    break;
                }

                showCourses(courseService.getAssignedCourses(id));

                break;
            }
            case 8: {
                return;
            }
            default: {
                System.out.println("Invalid Option!");
                pressToContinue();
                teachersMenu();
            }
        }

        pressToContinue();
    }

    public static void coursesMenu() {
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

                if (manageError(courseService.newCourse(name, description, maximumStudents, -1))) {
                    showResume("COURSE");
                    showCourse(courseService.searchCourseByName(name));
                }

                break;
            }
            case 2: {
                showCourses(courseService.getCourses());
                int id = inputHelper.inputInteger(sc, "ID of course to edit> ", false);

                String name = inputHelper.inputString(sc, "New course's name> ", true);
                String description = inputHelper.inputString(sc, "New course's description> ", true);
                int maximumStudents = inputHelper.inputInteger(sc, "New course's maximum quota> ", true);

                if (manageError(courseService.editCourse(id, name, description, maximumStudents))) {
                    showResume("COURSE");
                    showCourse(courseService.searchCourseByID(id));
                }

                break;
            }
            case 3: {
                showCourses(courseService.getCourses());
                int id = inputHelper.inputInteger(sc, "ID of course to delete> ", false);

                if (manageError(registrationService.removeCourseRegistrations(id))) {
                    manageError(courseService.removeCourse(id));
                }

                break;
            }
            case 4: {
                showCourses(courseService.getCourses());
                int courseID = inputHelper.inputInteger(sc, "ID of course to assign teacher> ", false);

                showTeachers(teacherService.getTeachers());
                int teacherID = inputHelper.inputInteger(sc, "ID of teacher to assign course> ", false);

                manageError(courseService.assignTeacher(courseID, teacherID));

                break;
            }
            case 5: {
                showCourses(courseService.getCourses());
                break;
            }
            case 6: {
                showCourses(courseService.getCourses());
                int courseID = inputHelper.inputInteger(sc, "ID from which course to show students> ", false);

                ArrayList<ArrayList<String>> students = registrationService.getEnrolledStudentsInCourse(courseID);

                if (students == null) {
                    System.out.println("Error: Null");
                    break;
                }

                showStudents(students);

                break;
            }
            case 7: {
                showCourses(courseService.getCourses());
                int courseID = inputHelper.inputInteger(sc, "ID of course to show remaining quota> ", false);

                int remain = registrationService.getCourseRemainingQuota(courseID);

                if (remain != -1){
                    System.out.println(remain);
                } else {
                    System.out.println("Error: no course");
                }

                break;
            }
            case 8: {
                return;
            }
            default:
                System.out.println("Invalid Option!");
                pressToContinue();
                coursesMenu();
        }

        pressToContinue();
    }

    public static void registrationsMenu() {
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
                showStudents(studentService.getStudents());
                int studentID = inputHelper.inputInteger(sc, "ID of student to register> ", false);

                showCourses(courseService.getCourses());
                int courseID = inputHelper.inputInteger(sc, "ID of course to register> ", false);

                manageError(registrationService.newRegistration(studentID, courseID));

                break;
            }
            case 2: {
                showStudents(studentService.getStudents());
                int studentID = inputHelper.inputInteger(sc, "Student who cancels registration> ", false);

                showCourses(registrationService.getCoursesEnrolledByStudent(studentID));
                int courseID = inputHelper.inputInteger(sc, "From which course you want to cancel registration> ", false);

                manageError(registrationService.cancelRegistration(studentID, courseID));

                break;
            }
            case 3: {
                showCourses(courseService.getCourses());
                int courseID = inputHelper.inputInteger(sc, "From which course you want to set grade> ", false);

                showStudents(registrationService.getEnrolledStudentsInCourse(courseID));
                int studentID = inputHelper.inputInteger(sc, "ID of student to grade> ", false);

                double grade = inputHelper.inputDouble(sc, "Grade> ");

                manageError(registrationService.grade(studentID, courseID, grade));

                break;
            }
            case 4: {
                showStudents(studentService.getStudents());
                int studentID = inputHelper.inputInteger(sc, "ID of student to show academic history> ", false);

                ArrayList<ArrayList<String>> academicHistory = registrationService.getAcademicHistory(studentID);

                if (academicHistory == null) {
                    System.out.println("Error: no student");
                    break;
                }

                showAcademicHistory(academicHistory, studentID);

                break;
            }
            case 5: {
                return;
            }
            default:
                System.out.println("Invalid Option!");
                pressToContinue();
                registrationsMenu();
        }

        pressToContinue();
    }

    public static void reportsMenu() {}

    public static void showStudent(ArrayList<String> studentData) {
        String active;
        
        if (studentData.get(5).equals("true")) {
            active = "ACTIVE";
        } else {
            active = "INACTIVE";
        }
        
        System.out.println(
            studentData.get(0) + "  |  " +
            studentData.get(1) + ", " +
            studentData.get(2) + " years old  |  " +
            studentData.get(3) + "  |  " +
            studentData.get(4) + "  |  " +
            active + "  |"
        );

    }
    public static void showTeacher(ArrayList<String> teacherData) {
        System.out.println(
            teacherData.get(0) + "  |  " +
            teacherData.get(1) + "  |  " +
            teacherData.get(2) + "  |  " +
            teacherData.get(3) + "  |"
        );
    }    
    public static void showCourse(ArrayList<String> courseData) {
        System.out.println(
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

    public static void showAcademicHistory(ArrayList<ArrayList<String>> academicHistory, int studentID) {
        System.out.print("\n                       A C A D E M I C   H I S T O R Y\n\n");

        System.out.println("<Student>");
        showResume("STUDENT");
        showStudent(studentService.searchByID(studentID));

        System.out.println("\n<Registered in>");

        for (ArrayList<String> resume : academicHistory) {
            System.out.println("Course's name: " + resume.get(0));
            System.out.println("Teacher's name: " + resume.get(1));
            System.out.println("Grade: " + resume.get(2));
            System.out.println("State: " + resume.get(3));
            
            System.out.println();
        }

        

    }

    public static void save() {
        if (!studentService.save() |
                !courseService.save() |
                !teacherService.save() |
                !registrationService.save()) {
            System.out.println("ERROR: Something went wrong while saving data.");
        } else {
            System.out.println("Data saved correctly!");
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

    public static void pressToContinue() {
        System.out.print("\n[PRESS <ENTER> TO CONTINUE]");
        sc.nextLine();
    }

}

// FOR COMPILING> javac -d target -sourcepath src src/org/institution/app/Main.java
// FOR RUNNING> java -cp target org.institution.app.Main
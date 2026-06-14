package org.institution.app;

// JAVA LIBS
import java.util.Scanner;
import java.util.ArrayList;

// PROJECT PACKAGE
import org.institution.app.service.*;
import org.institution.app.util.InputHelper;
import org.institution.app.util.Enum;

public class Main {
    final private static Scanner sc = new Scanner(System.in);
    final private static StudentService studentService = new StudentService();
    final private static TeacherService teacherService = new TeacherService();
    final private static CourseService courseService = new CourseService();
    final private static RegistrationService registrationService = new RegistrationService();
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
            int option = inputHelper.inputInteger(sc, "> ");

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
                case 6:
                    save();
                    break;
                case 7:
                    flow = false;
                    save();
                    break;
                default:
                    System.out.println("Invalid Option!");
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

        int option = inputHelper.inputInteger(sc, "> ");

        switch (option) {
            case 1: {
                String name = inputHelper.inputString(sc, "Student's name> ");
                int age = inputHelper.inputInteger(sc, "Student's age> ");
                String email = inputHelper.inputString(sc, "Student's email> ");
                studentService.newStudent(name, age, email);
                // missing error management
                break;
            }

            case 2: {
                showStudents(studentService.getStudents());
                
                int id = inputHelper.inputInteger(sc, "Student's id to edit> ");

                String name = inputHelper.inputString(sc, "Student's name> ");
                int age = inputHelper.inputInteger(sc, "Student's age> ");
                String email = inputHelper.inputString(sc, "Student's email> ");
                boolean isActive = inputHelper.inputBoolean(sc, "Active user? [y/n]> ");
            
                studentService.editStudentData(id, name, age, email, isActive);

                break;
            }

            case 3: {
                showStudents(studentService.getStudents());

                int id = inputHelper.inputInteger(sc, "Student's id to delete> ");

                Enum.Error error = studentService.deleteStudent(id);

                if (error != null) {
                    System.out.println("Error: " + error);
                }

                break;

            }
            case 4: {
                String name = inputHelper.inputString(sc, "Name of student to search> ");

                ArrayList<String> student = studentService.searchByName(name);

                if (student == null) {
                    System.out.println("Failed to search student");
                    break;
                }

                showStudent(student);

                break;
            }
            case 5: {
                int id = inputHelper.inputInteger(sc, "ID of student to search> ");

                ArrayList<String> student = studentService.searchByID(id);

                if (student == null) {
                    System.out.println("Failed to search student");
                    break;
                }

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
                break;
            }
            default:
                System.out.println("Invalid Option!");
                break;
        }
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

        int option = inputHelper.inputInteger(sc, "> ");

        switch (option) {
            case 1: {
                String name = inputHelper.inputString(sc, "Teacher's name> ");
                String department = inputHelper.inputString(sc, "Teacher's department> ");
                String email = inputHelper.inputString(sc, "Teacher's email> ");

                teacherService.newTeacher(name, department, email);

                break;
            }
            case 2: {
                showTeachers(teacherService.getTeachers());

                int id = inputHelper.inputInteger(sc, "Teacher's id to edit> ");

                String name = inputHelper.inputString(sc, "Teacher's name> ");
                String department = inputHelper.inputString(sc, "Teacher's department> ");
                String email = inputHelper.inputString(sc, "Teacher's email> ");

                teacherService.editTeacherData(id, name, department, email);

                break;
            }
            case 3: {
                showTeachers(teacherService.getTeachers());

                int id = inputHelper.inputInteger(sc, "ID from teacher to delete> ");

                // Missing error handler
                teacherService.removeTeacher(id);

                break;
            }
            case 4: {
                String name = inputHelper.inputString(sc, "Teacher's name to search> ");

                // Missing error handler
                showTeacher(teacherService.searchTeacherByName(name));

                break;
            }
            case 5: {
                int id = inputHelper.inputInteger(sc, "Teacher's ID to search> ");

                // Missing error handler
                showTeacher(teacherService.searchTeacherByID(id));

                break;
            }
            case 6: {
                showTeachers(teacherService.getTeachers());
                break;
            }
            case 7: {
                showTeachers(teacherService.getTeachers());

                int id = inputHelper.inputInteger(sc, "ID of teacher to show assigned courses> ");

                if (!teacherService.existTeacher(id)) {
                    System.out.println("Error: TEACHER DOES NOT EXIST");
                    break;
                }

                showCourses(courseService.getAssignedCourses(id));

                break;
            }
            case 8: {
                break;
            }
            default: {
                // Maybe default could return to same menu?
                System.out.println("Invalid Option!");

                // Just to test?
                // teachersMenu(sc, inputHelper, studentService);

                break;
            }
        }
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

        int option = inputHelper.inputInteger(sc, "> ");

        switch (option) {
            case 1: {
                String name = inputHelper.inputString(sc, "Course's name> ");
                String description = inputHelper.inputString(sc, "Course's description> ");
                int maximumStudents = inputHelper.inputInteger(sc, "Course's maximum quota> ");

                if (courseService.newCourse(name, description, maximumStudents, -1) == Enum.Error.WRONG_INPUT_DATA) {
                    System.out.println("failed");
                }

                break;
            }
            case 2: {
                showCourses(courseService.getCourses());

                int id = inputHelper.inputInteger(sc, "ID of course to edit> ");

                String name = inputHelper.inputString(sc, "New course's name> ");
                String description = inputHelper.inputString(sc, "New course's description> ");
                int maximumStudents = inputHelper.inputInteger(sc, "New course's maximum quota> ");
                // Maybe teachers id could be edited?

                courseService.editCourse(id, name, description, maximumStudents);

                break;
            }
            case 3: {
                showCourses(courseService.getCourses());

                int id = inputHelper.inputInteger(sc, "ID of course to delete> ");

                courseService.removeCourse(id);

                break;
            }
            case 4: {
                // showTeachers();
                // showCourses();

                // Working on it...

                break;
            }
            case 5: {
                showCourses(courseService.getCourses());
                break;
            }
            case 6: {
                showCourses(courseService.getCourses());

                // int id = inputHelper.inputInteger(sc, "ID from which course to show students> ");

                // showStudentsInCourse(id);

                break;
            }
            case 7: {
                showCourses(courseService.getCourses());

                int id = inputHelper.inputInteger(sc, "ID of course to show remaining quota> ");

                // What the f?
                System.out.println(courseService.getRemainingQuota(id));
            }
            case 8: {
                break;
            }
            default:
                System.out.println("Invalid Option!");
                break;
        }
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

        int option = inputHelper.inputInteger(sc, "> ");

        switch (option) {
            case 1: {
                showStudents(studentService.getStudents());

                int studentID = inputHelper.inputInteger(sc, "ID of student to register> ");

                showCourses(courseService.getCourses());

                int courseID = inputHelper.inputInteger(sc, "ID of course to register");

                // VALIDATION MISSING !!!
                registrationService.newRegistration(studentID, courseID);

                break;
            }
            case 2: {
                showStudents(studentService.getStudents());

                int studentID = inputHelper.inputInteger(sc, "Student who cancels registration> ");

                showCourses(courseService.getCoursesByRegistration(studentID));

                int courseID = inputHelper.inputInteger(sc, "from which course you want to cancel registration> ");

                registrationService.cancelRegistration(studentID, courseID);

                break;
            }
            case 3: {
                showCourses(courseService.getCourses());
                int courseID = inputHelper.inputInteger(sc, "from which course you want to set grade");

                showStudents(registrationService.getStudentsByCourse(courseID));
                int studentID = inputHelper.inputInteger(sc, "id of student to grade");

                // Missing validation? in service
                double grade = inputHelper.inputDouble(sc, "grade> ");

                registrationService.grade(studentID, courseID, grade);

                break;
            }
            case 4: {
                showStudents(studentService.getStudents());
                int studentID = inputHelper.inputInteger(sc, "id of student to show academic history");

                showAcademicHistory(studentID);

                break;
            }
            case 5: {
                break;
            }
            default:
                System.out.println("Invalid Option!");
                break;
        }
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
            studentData.get(2) + " YO  |  " +
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
        // A better print design should be set
        System.out.println(
            courseData.get(0) + "  |  " +
            courseData.get(1) + "  |  " +
            courseData.get(2) + "  |  " +
            courseData.get(3) + "  |  " +
            courseData.get(4) + "  |  "
        );
    }

    public static void showStudents(ArrayList<ArrayList<String>> studentsData) {
        for (ArrayList<String> student : studentsData) {
            showStudent(student);
        }
    }
    public static void showTeachers(ArrayList<ArrayList<String>> teachersData) {
        for (ArrayList<String> array : teachersData) {
            showTeacher(array);
        }
    }
    public static void showCourses(ArrayList<ArrayList<String>> coursesData) {
        for (ArrayList<String> course : coursesData) {
            showCourse(course);
        }
    }

    public static void showAcademicHistory(int studentID) {
        System.out.print("\n                       A C A D E M I C   H I S T O R Y\n\n");

        System.out.println("<Student>");
        showStudent(studentService.searchByID(studentID));

        System.out.println("\n<Registered in>");
        
        ArrayList<ArrayList<String>> academicHistory = registrationService.getAcademicHistory(studentID);

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

}

// FOR COMPILING> javac -d target -sourcepath src src/org/institution/app/Main.java
// FOR RUNNING> java -cp target org.institution.app.Main
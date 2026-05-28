package org.institution.app;

// JAVA LIBS
import java.util.Scanner;
import java.util.ArrayList;

// PROJECT PACKAGE
import org.institution.app.service.*;
import org.institution.app.util.InputHelper;
import org.institution.app.util.Enum;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService studentService = new StudentService();
        TeacherService teacherService = new TeacherService();
        CourseService courseService = new CourseService();
        RegistrationService registrationService = new RegistrationService();
        InputHelper inputHelper = new InputHelper();
        boolean flow = true;
        
        do {
            IO.print("\n\n");
            IO.print("                  A C A D E M I C   M A N A G E M E N T   S Y S T E M");
            IO.print("\n\n");
            IO.println("                                <Choose an option>");

            IO.println("1. Students");
            IO.println("2. Teachers");
            IO.println("3. Courses");
            IO.println("4. Registrations");
            IO.println("5. Reports");
            IO.println("6. Save");
            IO.println("7. Exit");
            
            // "$>" is the placeholder to show
            int option = inputHelper.inputInteger(sc, "> ");

            switch (option) {
                case 1:
                    studentsMenu(sc, inputHelper, studentService);
                    break;
                case 2:
                    teachersMenu(sc, inputHelper, teacherService);
                    break;
                case 3:
                    coursesMenu(sc, inputHelper, courseService);
                    break;
                case 4:
                    registrationsMenu(sc, inputHelper, registrationService);
                    break;
                case 5:
                    reportsMenu(sc);
                case 6:
                    saveMenu(sc);
                case 7:
                    flow = false;
                    break;
                default:
                    IO.println("Invalid Option!");
                    break;
            }
        } while (flow);

        sc.close();
    }

    public static void studentsMenu(Scanner sc, InputHelper inputHelper, StudentService service) {
        IO.print("\n\n");
        IO.print("                              S T U D E N T S   M E N U");
        IO.print("\n\n");
        IO.println("                                <Choose an option>");
        
        IO.println("1. Create Student");
        IO.println("2. Edit Student");
        IO.println("3. Delete Student");
        IO.println("4. Search By Name");
        IO.println("5. Search By ID");
        IO.println("6. Show All");
        IO.println("7. List By Average");
        IO.println("8. List Alphabetically");
        IO.println("9. Back");

        int option = inputHelper.inputInteger(sc, "> ");

        switch (option) {
            case 1: {
                String name = inputHelper.inputString(sc, "Student's name> ");
                int age = inputHelper.inputInteger(sc, "Student's age> ");
                String email = inputHelper.inputString(sc, "Student's email> ");
                service.newStudent(name, age, email);
                // missing error management
                break;
            }

            case 2: {
                showStudents(service.getStudents());
                
                int id = inputHelper.inputInteger(sc, "Student's id to edit> ");

                String name = inputHelper.inputString(sc, "Student's name> ");
                int age = inputHelper.inputInteger(sc, "Student's age> ");
                String email = inputHelper.inputString(sc, "Student's email> ");
                boolean isActive = inputHelper.inputBoolean(sc, "Active user? [y/n]> ");
            
                service.editStudentData(id, name, age, email, isActive);

                break;
            }

            case 3: {
                showStudents(service.getStudents());

                int id = inputHelper.inputInteger(sc, "Student's id to delete> ");

                Enum.Error error = service.deleteStudent(id);

                if (error != null) {
                    IO.println("Error: " + error);
                }

                break;

            }
            case 4: {
                String name = inputHelper.inputString(sc, "Name of student to search> ");

                ArrayList<String> student = service.searchByName(name);

                if (student == null) {
                    IO.println("Failed to search student");
                    break;
                }

                showStudent(student);

                break;
            }
            case 5: {
                int id = inputHelper.inputInteger(sc, "ID of student to search> ");

                ArrayList<String> student = service.searchByID(id);

                if (student == null) {
                    IO.println("Failed to search student");
                    break;
                }

                showStudent(student);

                break;
            }
            case 6: {
                showStudents(service.getStudents());
                
                break;
            }
            case 7: { 
                showStudents(service.sortByAverageGrade());
                
                break; 
            }
            case 8: { 
                showStudents(service.sortAlphabetically());

                break;
            }
            case 9: {
                break;
            }
            default:
                IO.println("Invalid Option!");
                break;
        }
    }
    
    public static void teachersMenu(Scanner sc, InputHelper inputHelper, TeacherService service) {
        IO.print("\n\n");
        IO.print("                      T E A C H E R S   M E N U");
        IO.print("\n\n");

        IO.println("                        <Choose an option>");
        IO.println("1. Create Teeacher");
        IO.println("2. Edit Teacher Data");
        IO.println("3. Delete Teacher");
        IO.println("4. Search By Name");
        IO.println("5. Search By ID");
        IO.println("6. Show All");
        IO.println("7. Show Assigned Courses");
        IO.println("8. Back");

        int option = inputHelper.inputInteger(sc, "> ");

        switch (option) {
            case 1: {
                String name = inputHelper.inputString(sc, "Teacher's name> ");
                String department = inputHelper.inputString(sc, "Teacher's department> ");
                String email = inputHelper.inputString(sc, "Teacher's email> ");

                service.newTeacher(name, department, email);

                break;
            }
            case 2: {
                showTeachers(service.getTeachers());

                int id = inputHelper.inputInteger(sc, "Teacher's id to edit> ");

                String name = inputHelper.inputString(sc, "Teacher's name> ");
                String department = inputHelper.inputString(sc, "Teacher's department> ");
                String email = inputHelper.inputString(sc, "Teacher's email> ");

                service.editTeacherData(id, name, department, email);

                break;
            }
            case 3: {
                showTeachers(service.getTeachers());

                int id = inputHelper.inputInteger(sc, "ID from teacher to delete> ");

                // Missing error handler
                service.removeTeacher(id);

                break;
            }
            case 4: {
                String name = inputHelper.inputString(sc, "Teacher's name to search> ");

                // Missing error handler
                showTeacher(service.searchTeacherByName(name));

                break;
            }
            case 5: {
                int id = inputHelper.inputInteger(sc, "Teacher's ID to seach> ");

                // Missing error handler
                showTeacher(service.searchTeacherByID(id));

                break;
            }
            case 6: {
                showTeachers(service.getTeachers());
                break;
            }
            case 7: {
                showTeachers(service.getTeachers());

                // int id = inputHelper.inputInteger(sc, "ID of teacher to show assigned courses> ");

                // Missing error handler
                // Missing service method
                // service.showAssignedCourses(id);

                break;
            }
            case 8: {
                break;
            }
            default: {
                // Maybe default could return to same menu?
                IO.println("Invalid Option!");

                // Just to test?
                // teachersMenu(sc, inputHelper, service);

                break;
            }
        }
    }

    public static void coursesMenu(Scanner sc, InputHelper inputHelper, CourseService service) {
        IO.print("\n\n");
        IO.print("                      C O U R S E S   M E N U");
        IO.print("\n\n");

        IO.println("                       <Choose an option>");

        IO.println("1. Create Course");
        IO.println("2. Edit Course");
        IO.println("3. Delete Course");
        IO.println("4. Assign Teacher to Course");
        IO.println("5. Show Courses");
        IO.println("6. Show Students in Course");
        IO.println("7. Show Remaining Quote");
        IO.println("8. Back");

        int option = inputHelper.inputInteger(sc, "> ");

        switch (option) {
            case 1: {
                String name = inputHelper.inputString(sc, "Course's name> ");
                String description = inputHelper.inputString(sc, "Course's description> ");
                int maximumStudents = inputHelper.inputInteger(sc, "Course's maximum quota> ");

                service.newCourse(name, description, maximumStudents, maximumStudents);

                break;
            }
            case 2: {
                showCourses(service.getCourses());

                int id = inputHelper.inputInteger(sc, "ID of course to edit> ");

                String name = inputHelper.inputString(sc, "New course's name> ");
                String description = inputHelper.inputString(sc, "New course's description> ");
                int maximumStudents = inputHelper.inputInteger(sc, "New course's maximum quota> ");
                // Maybe teachers id could be eedited?

                service.editCourse(id, name, description, maximumStudents);

                break;
            }
            case 3: {
                showCourses(service.getCourses());

                int id = inputHelper.inputInteger(sc, "ID of course to delete> ");

                service.removeCourse(id);

                break;
            }
            case 4: {
                // showTeachers();
                // showCourses();

                // Working on it...

                break;
            }
            case 5: {
                showCourses(service.getCourses());
                break;
            }
            case 6: {
                showCourses(service.getCourses());

                // int id = inputHelper.inputInteger(sc, "ID from which course to show students> ");

                // showStudentsInCourse(id);

                break;
            }
            case 7: {
                showCourses(service.getCourses());

                int id = inputHelper.inputInteger(sc, "ID of course to show remaining quota> ");

                // What the f?
                IO.println(service.getRemainingQuota(id));
            }
            case 8: {
                break;
            }
            default:
                IO.println("Invalid Option!");
                break;
        }
    }
    // Missing
    public static void registrationsMenu(Scanner sc, InputHelper inputHelper, RegistrationService service) {
        IO.print("\n\n");
        IO.print("                      R E G I S T R A T I O N S   M E N U");
        IO.print("\n\n");

        IO.println("                            <Choose an option>");

        IO.println("1. Create Registration");
        IO.println("2. Cancel Registration");
        IO.println("3. Set Grade");
        IO.println("4. Show Academic History");

        int option = inputHelper.inputInteger(sc, "> ");

        switch (option) {
            case 1: {
                // show students

                int studentID = inputHelper.inputInteger(sc, "ID of student to register> ");

                // show courses

                int courseID = inputHelper.inputInteger(sc, "ID of course to register");

                service.newRegistration(studentID, courseID);

                break;
            }
            case 2: {
                // show courses

                int courseID = inputHelper.inputInteger(sc, "from which course you want to cancel registration> ");

                // show students in course (id)

                int studentID = inputHelper.inputInteger(sc, "Studen to cancel registration> ");

                service.cancelRegistration(studentID, courseID);

                break;
            }
            case 3: {
                // show courses

                int courseID = inputHelper.inputInteger(sc, "from which course you want to set grade");

                // show students in course (id)

                int studentID = inputHelper.inputInteger(sc, "id of student to grade");

                // double grade = inputHelper.inputDouble(sc, "grade> ");

                // temporal, just to compile
                double grade = 0.0;

                service.grade(studentID, courseID, grade);

                break;
            }
            case 4: {
                // show students

                int studentID = inputHelper.inputInteger(sc, "id of student to show academic history");

                // showAcademicHistory(service, id);

                break;
            }
            default:
                IO.println("Invalid Option!");
                break;
        }
    }
    public static void reportsMenu(Scanner sc) {}
    public static void saveMenu(Scanner sc) {}

    public static void showStudent(ArrayList<String> studentData) {
        String active;
        
        if (studentData.get(5) == "true") {
            active = "ACTIVE";
        } else {
            active = "INACTIVE";
        }
        
        IO.println(
            studentData.get(0) + "  |  " +
            studentData.get(1) + ", " +
            studentData.get(2) + " YO  |  " +
            studentData.get(3) + "  |  " +
            studentData.get(4) + "  |  " +
            active + "  |"
        );

    }
    public static void showTeacher(ArrayList<String> teacherData) {
        IO.println(
            teacherData.get(0) + "  |  " +
            teacherData.get(1) + "  |  " +
            teacherData.get(2) + "  |  " +
            teacherData.get(3) + "  |"
        );
    }    
    public static void showCourse(ArrayList<String> courseData) {
        // A better print design should be set
        IO.println(
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
}

// FOR COMPILING> javac -d target -sourcepath src src/org/institution/app/Main.java
// FOR RUNNING> java -cp target org.institution.app.Main
package org.institution.app;

import java.util.Scanner;
import org.institution.app.repository.StudentRepository;
import org.institution.app.repository.TeacherRepository;
import org.institution.app.repository.CourseRepository;
import org.institution.app.repository.RegistrationRepository;
import org.institution.app.repository.csv.CsvCourseRepository;
import org.institution.app.repository.csv.CsvRegistrationRepository;
import org.institution.app.repository.csv.CsvStudentRepository;
import org.institution.app.repository.csv.CsvTeacherRepository;
import org.institution.app.service.StudentService;
import org.institution.app.service.TeacherService;
import org.institution.app.service.CourseService;
import org.institution.app.service.RegistrationService;
import org.institution.app.util.InputHelper;
import org.institution.app.ui.StudentsMenu;
import org.institution.app.ui.CoursesMenu;
import org.institution.app.ui.TeachersMenu;
import org.institution.app.ui.RegistrationsMenu;
import org.institution.app.ui.ReportsMenu;
import org.institution.app.ui.UiHelper;

public class Main {
    final private static Scanner sc = new Scanner(System.in);

    // repositories initialization
    final private static StudentRepository studentRepository = new CsvStudentRepository();
    final private static TeacherRepository teacherRepository = new CsvTeacherRepository();
    final private static CourseRepository courseRepository = new CsvCourseRepository();
    final private static RegistrationRepository registrationRepository = new CsvRegistrationRepository();

    // services initialization | dependencies injection
    final private static StudentService studentService = new StudentService(studentRepository);
    final private static TeacherService teacherService = new TeacherService(teacherRepository);
    final private static CourseService courseService = new CourseService(courseRepository, teacherService);
    final private static RegistrationService registrationService = new RegistrationService(registrationRepository, courseService, studentService, teacherService);

    final private static InputHelper inputHelper = new InputHelper();

    public static void main(String[] args) {
        
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
                    StudentsMenu.studentsMenu(inputHelper, studentService, registrationService, sc);
                    break;
                case 2:
                    TeachersMenu.teachersMenu(inputHelper, teacherService, courseService, sc);
                    break;
                case 3:
                    CoursesMenu.coursesMenu(inputHelper, courseService, registrationService, teacherService, sc);
                    break;
                case 4:
                    RegistrationsMenu.registrationsMenu(inputHelper, registrationService, studentService, courseService, teacherService, sc);
                    break;
                case 5:
                    ReportsMenu.reportsMenu();
                    break;
                case 6:
                    save();
                    UiHelper.pressToContinue(sc);
                    break;
                case 7:
                    flow = false;
                    save();
                    break;
                default:
                    System.out.println("Invalid Option!");
                    UiHelper.pressToContinue(sc);
                    break;
            }
        } while (flow);

        sc.close();
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
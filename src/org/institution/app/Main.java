package org.institution.app;

import java.util.Scanner;
import org.institution.app.repository.*;
import org.institution.app.repository.csv.*;
import org.institution.app.service.*;
import org.institution.app.serializer.student.StudentSerializer;
import org.institution.app.serializer.teacher.TeacherSerializer;
import org.institution.app.serializer.course.CourseSerializer;
import org.institution.app.serializer.registration.RegistrationSerializer;
import org.institution.app.serializer.student.impl.*;
import org.institution.app.serializer.teacher.impl.*;
import org.institution.app.serializer.course.impl.*;
import org.institution.app.serializer.registration.impl.*;
import org.institution.app.util.InputHelper;
import org.institution.app.ui.*;

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
                    saveMenu();
                    break;
                case 7:
                    flow = false;
                    save(new CsvStudentSerializer(), new CsvTeacherSerializer(), new CsvCourseSerializer(), new CsvRegistrationSerializer());
                    break;
                default:
                    System.out.println("Invalid Option!");
                    UiHelper.pressToContinue(sc);
                    break;
            }
        } while (flow);

        sc.close();
    }

    public static void save(StudentSerializer studentExporter, TeacherSerializer teacherExporter, CourseSerializer courseExporter, RegistrationSerializer registrationExport) {
        if (!studentService.save(studentExporter) |
                !courseService.save(courseExporter) |
                !teacherService.save(teacherExporter) |
                !registrationService.save(registrationExport)) {
            System.out.println("ERROR: Something went wrong while saving data.");
        } else {
            System.out.println("Data saved correctly!");
        }
    }

    public static void saveMenu() {
        while(true) {
            System.out.print("\n\n");
            System.out.print("                      S A V E   M E N U");
            System.out.print("\n\n");
            System.out.println("                    <Choose an option>");

            System.out.println("1. Save (default)");
            System.out.println("2. Export to JSON");
            System.out.println("3. Export to XML");
            System.out.println("4. Export to CSV");
            System.out.println("5. Back");

            int option = inputHelper.inputInteger(sc, "> ", true);

            switch (option) {
                case 1: {
                    save(new CsvStudentSerializer(), new CsvTeacherSerializer(), new CsvCourseSerializer(), new CsvRegistrationSerializer());
                    break;
                } case 2: {
                    System.out.println("Exporting to JSON...");
                    export("json");
                    break;
                } case 3: {
                    System.out.println("Exporting to XML...");
                    export("xml");
                    break;
                } case 4: {
                    System.out.println("Exporting to CSV...");
                    export("csv");
                    break;
                } case 5: {
                    return;
                } default: {
                    save(new CsvStudentSerializer(), new CsvTeacherSerializer(), new CsvCourseSerializer(), new CsvRegistrationSerializer());
                    break;
                }
            }

            UiHelper.pressToContinue(sc);
        }
    }

    public static void export(String format) {
        TeacherSerializer teacherExporter;
        StudentSerializer studentExporter;
        CourseSerializer courseExporter;
        RegistrationSerializer registrationExporter;
        
        switch (format) {
            case "json": {
                teacherExporter = new JsonTeacherSerializer();
                studentExporter = new JsonStudentSerializer();
                courseExporter = new JsonCourseSerializer();
                registrationExporter = new JsonRegistrationSerializer();
                break;
            } case "xml": {
                teacherExporter = new XmlTeacherSerializer();
                studentExporter = new XmlStudentSerializer();
                courseExporter = new XmlCourseSerializer();
                registrationExporter = new XmlRegistrationSerializer();
                break;
            } case "csv": {
                teacherExporter = new CsvTeacherSerializer();
                studentExporter = new CsvStudentSerializer();
                courseExporter = new CsvCourseSerializer();
                registrationExporter = new CsvRegistrationSerializer();
                break;
            } default:{ return; }
        }
        
        
        if (!studentService.exportData(studentExporter)) System.out.println("Something went wrong while trying to export students");
        if (!teacherService.exportData(teacherExporter)) System.out.println("Something went wrong while trying to export teachers");
        if (!courseService.exportData(courseExporter)) System.out.println("Something went wrong while trying to export courses");
        if (!registrationService.exportData(registrationExporter)) System.out.println("Something went wrong while trying to export registrations");
    }

}

// FOR COMPILING> javac -d target -sourcepath src src/org/institution/app/Main.java
// FOR RUNNING> java -cp target org.institution.app.Main
// Missing 'Ver historial académico' from chatgpt md

package org.institution.app.service;

// JAVA LIBS
import java.util.ArrayList;

// PROJ PACKAGES
import org.institution.app.model.Registration;
import org.institution.app.repository.RegistrationRepository;
import org.institution.app.util.Enum;
import org.institution.app.util.*;

public class RegistrationService {
    private RegistrationRepository repository = new RegistrationRepository();
    private Validator validator = new Validator();

    public Enum.Error newRegistration(int studentID, int courseID) {
        // Validator
        // Missing to validate if there's yet a registration with same id's
        if (!validator.validateRegistrationInputData(studentID, courseID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.newRegistration(studentID, courseID);

        return null;
    }

    // maybe stupid
    public void setAverageGrade(int studentID, ArrayList<Double> grades) {
        
        int sum = 0;
        for (double g : grades) {
            sum += g;
        }

        // HAVE TO USE HELPER
        StudentService service = new StudentService();
        service.setAverageGrade(studentID, sum/grades.size());
    }

    public void grade(int studentID, int courseID, double grade) {
        Registration reg = repository.getRegistration(studentID, courseID);

        reg.setGrade(grade);
        ArrayList<Double> grades = repository.getStudentGrades(studentID);
        setAverageGrade(studentID, grades);
    }

    public void removeStudentRegistrations(int studentID) {
        ArrayList<Registration> registrations = repository.getRegistrations();
        ArrayList<Integer> indexes = new ArrayList<>();

        int index = 0;
        for (Registration r : registrations) {
            if (r.getStudentId() == studentID) {
                indexes.add(index);
            }

            index++;
        }

        repository.removeRegistrationsIndexes(indexes);

        return;
    }

    public ArrayList<ArrayList<String>> getStudentsByCourse(int courseID) {
        ArrayList<Registration> registrations = repository.getRegistrations();
        ArrayList<Registration> courseRegistrations = new ArrayList<>();

        for (Registration r : registrations) {
            if (r.getCourseId() == courseID) {
                courseRegistrations.add(r);
            }
        }

        return Helper.studentsInCourse(courseRegistrations, courseID);

    }    

    public Enum.Error cancelRegistration(int studentID, int courseID) {
        if (!Helper.existStudent(studentID) || !Helper.existCourse(courseID)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.removeRegistration(studentID, courseID);

        return null;
    }

}

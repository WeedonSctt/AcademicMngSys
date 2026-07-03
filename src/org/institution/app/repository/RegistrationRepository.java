package org.institution.app.repository;

import java.util.ArrayList;
import org.institution.app.model.Registration;

public interface RegistrationRepository {

    void newRegistration(int studentID, int courseID);

    Registration getRegistration(int stID, int cID);

    ArrayList<Double> getStudentGrades(int id);

    ArrayList<Registration> getRegistrations();

    boolean save();

    boolean load();

    void deleteRegistration(int studentID, int courseID);

    void deleteRegistrationsIndexes(ArrayList<Integer> indexes);
    
}

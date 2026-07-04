package org.institution.app.repository;

import java.util.ArrayList;
import org.institution.app.model.Registration;

public interface RegistrationRepository {

    void newRegistration(Registration r);

    Registration getRegistration(int stID, int cID);

    ArrayList<Double> getStudentGrades(int id);

    ArrayList<Registration> getRegistrations();

    boolean save();

    boolean load();

    void deleteRegistration(Registration r);

    void deleteRegistrations(ArrayList<Registration> regs);
    
}

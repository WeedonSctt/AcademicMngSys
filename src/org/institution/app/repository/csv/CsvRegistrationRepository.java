package org.institution.app.repository.csv;

import java.io.IOException;
import java.util.ArrayList;
import org.institution.app.model.Registration;
import org.institution.app.repository.RegistrationRepository;
import org.institution.app.util.FileManager;
import org.institution.app.util.Helper;

public class CsvRegistrationRepository implements RegistrationRepository {
    private ArrayList<Registration> registrations = new  ArrayList<>();
    private FileManager reader = new FileManager();
    private FileManager writer = new FileManager();

    @Override
    public void newRegistration(Registration r) {
        registrations.add(r);
    }

    @Override
    public Registration getRegistration(int stID, int cID) {
        for (Registration r : registrations) {
            if (r.getStudentId() == stID && r.getCourseId() == cID) {
                return r;
            }

        }
        return null;
    }

    @Override
    public ArrayList<Double> getStudentGrades(int id) {
        ArrayList<Double> grades = new ArrayList<>();

        for (Registration r : registrations) {
            if (r.getStudentId() == id) {
                grades.add(r.getGrade());
            }
        }
        
        return grades;
    }

    @Override
    public void deleteRegistration(Registration r) {
        registrations.remove(r);
    }

    @Override
    public void deleteRegistrations(ArrayList<Registration> regs) {
        for (int i = regs.size()-1; i > 0; i--) {
            Registration r = regs.get(i);

            registrations.remove(r);
        }
    }

    @Override
    public boolean save() {
        ArrayList<ArrayList<String>> registrationsData = new ArrayList<>();
        
        for (Registration r : registrations) {
            registrationsData.add(Helper.registrationToStringArray(r));
        }

        try {
            writer.writeToFile("registrations.txt", registrationsData);
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean load() {
        ArrayList<ArrayList<String>> registrationsData; 
        
        try {
            registrationsData = reader.readFromFile("registrations.txt");
        } catch (IOException e) {
            return false;
        }

        for (ArrayList<String> array : registrationsData) {
            registrations.add(new Registration(
                Integer.parseInt(array.get(0)),
                Integer.parseInt(array.get(1)),
                Double.parseDouble(array.get(2))
            ));
        }

        return true;
    }

    
    // Getter
    @Override
    public ArrayList<Registration> getRegistrations() { return registrations; }

}

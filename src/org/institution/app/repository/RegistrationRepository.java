package org.institution.app.repository;

import java.io.IOException;
// JAVA LIBS
import java.util.ArrayList;

// PROJ PACKAGES
import org.institution.app.model.Registration;
import org.institution.app.util.FileManager;
import org.institution.app.util.Helper;

public class RegistrationRepository {
    private ArrayList<Registration> registrations = new  ArrayList<>();
    private FileManager reader = new FileManager();
    private FileManager writer = new FileManager();

    public void newRegistration(int studentID, int courseID) {
        registrations.add(new Registration(studentID, courseID));
    }

    public Registration getRegistration(int stID, int cID) {
        for (Registration r : registrations) {
            if (r.getStudentId() == stID && r.getCourseId() == cID) {
                return r;
            }

        }
        return null;
    }

    public ArrayList<Double> getStudentGrades(int id) {
        ArrayList<Double> grades = new ArrayList<>();

        for (Registration r : registrations) {
            if (r.getStudentId() == id) {
                grades.add(r.getGrade());
            }
        }
        
        return grades;
    }

    public void removeRegistration(int studentID, int courseID) {
        
        for (Registration r : registrations) {
            if (r.getStudentId() == studentID && r.getCourseId() == courseID) {
                registrations.remove(r);
            }
        }
    }

    public void removeRegistrationsIndexes(ArrayList<Integer> indexes) {
        for (int i : indexes) {
            registrations.remove(i);
        }

        return;
    }

    public boolean saveRegistrationsToCSV() {
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

    public boolean loadRegistrationsFromCSV() {
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
    public ArrayList<Registration> getRegistrations() { return registrations; }

}

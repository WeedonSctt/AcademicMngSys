package org.institution.app.util;

// PROJ PACKAGES
// import org.institution.app.util.Helper;

public class Validator {
    
    
    public boolean validateStudentInputData(String name, int age, String email) {
        
        if (age < 18) {
            return false;
        }

        if (!emailHasExtension(email)) {
            return false;
        }

        return true;
    }
    
    public boolean validateStudentInputData(String email) {

        if (!emailHasExtension(email)) {
            return false;
        }

        return true;
    }

    public boolean validateCourseInputData(int maximumStudents, int teacherID) {
        if (maximumStudents < 0 && maximumStudents > 50) {
            return false;
        }

        if (!Helper.existTeacher(teacherID)) {
            return false;
        } else if (teacherID == -1) {
            return true;
        }

        return true;
    }

    private boolean emailHasExtension(String e) {
        // MISSING TO CHECK IF IT HAS 'gmail/outlook/whatever'
        
        char[] characters = e.toCharArray();
        
        int index = 0;
        boolean at = false;
        for (char c : characters) {
            if (c == '@') {
                at = true;
                break;
            }

            index++;
        }

        if (at) {
            for (int i = index; i < characters.length; i++) {
                if (characters[i] == '.') {
                    return true;
                }
            }

            return false;
        }

        return false;
    }

    public boolean validateRegistrationInputData(int studentID, int courseID) {
        if (!Helper.existStudent(studentID)) {
            return false;
        } else if (!Helper.existCourse(courseID)) {
            return false;
        }

        return true;
    }

}
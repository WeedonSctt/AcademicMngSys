package org.institution.app.service;

import java.util.ArrayList;

import org.institution.app.model.Teacher;
import org.institution.app.repository.TeacherRepository;
import org.institution.app.util.Validator;
import org.institution.app.util.Enum;
import org.institution.app.util.Helper;

public class TeacherService {
    Validator validator = new Validator();
    TeacherRepository repository = new TeacherRepository();
    
    public Enum.Error newTeacher(String name, String department, String email) {
        if (!validator.validateStudentInputData(email)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.newTeacher(repository.getLastID() + 1, name, department, email);

        return null;
    }

    public Enum.Error editTeacherData(int id, String name, String department, String email) {
        if (!validator.validateStudentInputData(email)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        Teacher t = repository.getTeacherByID(id);

        t.setName(name);
        t.setDepartment(department);
        t.setEmail(email);
        
        return null;
    }

    public boolean removeTeacher(int id) {
        return (repository.removeTeacher(id));
    }

    public ArrayList<String> searchTeacherByID(int id) {
        Teacher t = repository.getTeacherByID(id);

        ArrayList<String> teacherData = Helper.teacherToStringArray(t);

        return teacherData;
    }

    public ArrayList<String> searchTeacherByName(String name) {
        Teacher t = repository.getTeacherByName(name);

        ArrayList<String> teacherData = Helper.teacherToStringArray(t);

        return teacherData;
    }

    public ArrayList<ArrayList<String>> getTeachers() {
        ArrayList<ArrayList<String>> teachersData = new ArrayList<>();
        ArrayList<Teacher> teachers = repository.getTeachers();

        for (Teacher teacher : teachers) {
            teachersData.add(Helper.teacherToStringArray(teacher));
        }

        return teachersData;

    }

}

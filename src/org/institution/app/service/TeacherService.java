package org.institution.app.service;

import java.util.ArrayList;
import org.institution.app.model.Teacher;
import org.institution.app.repository.TeacherRepository;
import org.institution.app.util.Enum;
import org.institution.app.util.*;

public class TeacherService {
    private final TeacherRepository repository;

    public TeacherService(TeacherRepository repo) {
        this.repository = repo;
    }
    
    public Enum.Error newTeacher(String name, String department, String email) {
        if (!Validator.teacherInputData(email)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        for (Teacher t : repository.getTeachers()) {
            if (t.getEmail().equals(email)) {
                return Enum.Error.ALREADY_CREATED;
            }
        }

        repository.newTeacher(repository.getLastID() + 1, name, department, email);

        return null;
    }

    public Enum.Error editTeacherData(int id, String name, String department, String email) {
        if (!Validator.teacherInputData(email)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }


        Teacher t = repository.getTeacherByID(id);

        if (email != null) {
            t.setEmail(email);
        }

        if (name != null) {
            t.setName(name);
        }

        if (department != null) {
            t.setDepartment(department);
        }

        return null;
    }

    public Enum.Error removeTeacher(int id) {
        if (!existTeacher(id)) {
            return Enum.Error.WRONG_INPUT_DATA;
        }

        repository.removeTeacher(id);

        return null;
    }

    public ArrayList<String> searchTeacherByID(int id) {
        Teacher t = repository.getTeacherByID(id);

        return Helper.teacherToStringArray(t);
    }

    public ArrayList<String> searchTeacherByName(String name) {
        Teacher t = repository.getTeacherByName(name);

        if (t != null) {
            return Helper.teacherToStringArray(t);
        }

        return null;
    }

    public ArrayList<ArrayList<String>> getTeachers() {
        ArrayList<ArrayList<String>> teachersData = new ArrayList<>();
        ArrayList<Teacher> teachers = repository.getTeachers();

        for (Teacher teacher : teachers) {
            teachersData.add(Helper.teacherToStringArray(teacher));
        }

        return teachersData;

    }

    public Teacher getTeacherByID(int id) {
        ArrayList<Teacher> teachers = repository.getTeachers();

        for (Teacher t : teachers) {
            if (t.getID() == id) {
                return t;
            }
        }

        return null;
    }

    public boolean save() {
        return repository.saveTeachersToCSV();
    }

    public boolean loadRepo() {
        return repository.loadTeachersFromCSV();
    }

    public boolean existTeacher(int id) {
        for (Teacher t : repository.getTeachers()) {
            if (t.getID() == id) {
                return true;
            }
        }

        return false;
    }

}

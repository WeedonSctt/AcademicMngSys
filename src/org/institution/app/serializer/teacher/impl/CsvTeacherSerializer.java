package org.institution.app.serializer.teacher.impl;

import org.institution.app.serializer.teacher.TeacherSerializer;
import org.institution.app.model.Teacher;
import java.util.List;

public class CsvTeacherSerializer implements TeacherSerializer {
    @Override
    public String export(List<Teacher> teachers) {
        StringBuilder csv = new StringBuilder();

        for (Teacher t : teachers) {
            objectToCsv(csv, t);
            csv.append("\n");
        }

        return csv.toString();
    }

    public String getExtension() { return "csv"; }

    private static void objectToCsv(StringBuilder csv, Teacher t) {
        csv.append(t.getID());
        csv.append(",");
        csv.append(t.getName());
        csv.append(",");
        csv.append(t.getDepartment());
        csv.append(",");
        csv.append(t.getEmail());
    }
}

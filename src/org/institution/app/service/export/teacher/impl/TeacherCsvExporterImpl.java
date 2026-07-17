package org.institution.app.service.export.teacher.impl;

import org.institution.app.service.export.teacher.TeacherExportService;
import org.institution.app.model.Teacher;
import java.util.List;

public class TeacherCsvExporterImpl implements TeacherExportService {
    @Override
    public String export(List<Teacher> teachers) {
        StringBuilder csv = new StringBuilder();

        for (Teacher t : teachers) {
            objectToCsv(csv, t);
            csv.append("\n");
        }

        return csv.toString();
    }

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

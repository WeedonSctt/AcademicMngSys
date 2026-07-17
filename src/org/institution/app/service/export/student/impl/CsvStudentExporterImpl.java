package org.institution.app.service.export.student.impl;

import org.institution.app.service.export.student.StudentExportService;
import org.institution.app.model.Student;
import java.util.List;

public class CsvStudentExporterImpl implements StudentExportService {
    @Override
    public String export(List<Student> students) {
        StringBuilder csv = new StringBuilder();

        for (Student s : students) {
            objectToCsv(csv, s);
            csv.append("\n");
        }

        return csv.toString();
    }

    private static void objectToCsv(StringBuilder csv, Student s) {
        csv.append(s.getId());
        csv.append(",");
        csv.append(s.getName());
        csv.append(",");
        csv.append(s.getAge());
        csv.append(",");
        csv.append(s.getEmail());
        csv.append(",");
        csv.append(s.getAverageGrade());
        csv.append(",");
        csv.append(s.isActive());
    }
}

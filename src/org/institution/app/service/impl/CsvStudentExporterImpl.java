package org.institution.app.service.impl;

import org.institution.app.service.StudentExportService;
import org.institution.app.model.Student;
import java.util.List;

public class CsvStudentExporterImpl implements StudentExportService {
    @Override
    public String export(List<Student> students) {
        StringBuilder csv = new StringBuilder();

        for (Student s : students) {
            csv.append(s.getId());
            csv.append(",");
            csv.append(s.getName());
            csv.append(",");
            csv.append(s.getAge());
            csv.append(",");
            csv.append(s.getEmail());
            csv.append(",");
            csv.append(String.valueOf(s.getAverageGrade()));
            csv.append(",");
            csv.append(String.valueOf(s.isActive()));
            csv.append("\n");
        }

        return csv.toString();
    }
}

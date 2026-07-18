package org.institution.app.serializer.student.impl;

import org.institution.app.serializer.student.StudentSerializer;
import org.institution.app.model.Student;
import java.util.List;

public class CsvStudentSerializer implements StudentSerializer {
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

    @Override
    public String getExtension() { return "csv"; }

}

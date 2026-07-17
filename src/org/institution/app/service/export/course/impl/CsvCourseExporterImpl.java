package org.institution.app.service.export.course.impl;

import org.institution.app.service.export.course.CourseExportService;
import org.institution.app.model.Course;
import java.util.List;

public class CsvCourseExporterImpl implements CourseExportService {
    @Override
    public String export(List<Course> courses) {
        StringBuilder csv = new StringBuilder();

        for (Course c : courses) {
            objectToCsv(csv, c);
            csv.append("\n");
        }

        return csv.toString();
    }

    private static void objectToCsv(StringBuilder csv, Course c) {
        csv.append(c.getId());
        csv.append(",");
        csv.append(c.getName());
        csv.append(",");
        csv.append(c.getDescription());
        csv.append(",");
        csv.append(c.getMaximumStudents());
        csv.append(",");
        csv.append(c.getTeacherId());
    }
}

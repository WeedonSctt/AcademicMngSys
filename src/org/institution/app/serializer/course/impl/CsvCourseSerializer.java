package org.institution.app.serializer.course.impl;

import org.institution.app.serializer.course.CourseSerializer;
import org.institution.app.model.Course;
import java.util.List;

public class CsvCourseSerializer implements CourseSerializer {
    @Override
    public String export(List<Course> courses) {
        StringBuilder csv = new StringBuilder();

        for (Course c : courses) {
            objectToCsv(csv, c);
            csv.append("\n");
        }

        return csv.toString();
    }

    @Override
    public String getExtension() { return "csv"; }

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

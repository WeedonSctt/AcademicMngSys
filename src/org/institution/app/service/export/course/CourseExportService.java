package org.institution.app.service.export.course;

import org.institution.app.model.Course;
import java.util.List;

public interface CourseExportService {
    String export(List<Course> courses);
}

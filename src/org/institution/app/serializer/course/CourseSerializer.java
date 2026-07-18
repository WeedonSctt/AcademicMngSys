package org.institution.app.serializer.course;

import org.institution.app.model.Course;
import java.util.List;

public interface CourseSerializer {
    String export(List<Course> courses);

    String getExtension();
}

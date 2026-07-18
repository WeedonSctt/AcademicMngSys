package org.institution.app.serializer.teacher;

import org.institution.app.model.Teacher;
import java.util.List;

public interface TeacherSerializer {
    String export(List<Teacher> teachers);

    String getExtension();
}

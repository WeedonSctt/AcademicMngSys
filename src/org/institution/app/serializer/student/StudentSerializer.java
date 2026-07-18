package org.institution.app.serializer.student;

import org.institution.app.model.Student;
import java.util.List;

public interface StudentSerializer {
    String export(List<Student> students);

    String getExtension();
}

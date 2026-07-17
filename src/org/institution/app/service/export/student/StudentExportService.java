package org.institution.app.service.export.student;

import org.institution.app.model.Student;
import java.util.List;

public interface StudentExportService {
    String export(List<Student> students);
}

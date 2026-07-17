package org.institution.app.service.export.teacher;

import org.institution.app.model.Teacher;
import java.util.List;

public interface TeacherExportService {
    String export(List<Teacher> teachers);
}

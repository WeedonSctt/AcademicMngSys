package org.institution.app.service;

import org.institution.app.model.Student;
import java.util.List;

public interface StudentExportService {
    String export(List<Student> students);
}

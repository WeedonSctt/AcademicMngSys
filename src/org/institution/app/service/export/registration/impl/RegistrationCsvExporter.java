package org.institution.app.service.export.registration.impl;

import org.institution.app.service.export.registration.RegistrationExportService;
import org.institution.app.model.Registration;
import java.util.List;

public class RegistrationCsvExporter implements RegistrationExportService {
    @Override
    public String export(List<Registration> registrations) {
        StringBuilder csv = new StringBuilder();

        for (Registration r : registrations) {
            objectToCsv(csv, r);
            csv.append("\n");
        }

        return csv.toString();
    }

    private static void objectToCsv(StringBuilder csv, Registration r) {
        csv.append(r.getStudentId());
        csv.append(",");
        csv.append(r.getCourseId());
        csv.append(",");
        csv.append(r.getGrade());
    }
}

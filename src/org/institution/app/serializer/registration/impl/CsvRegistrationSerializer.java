package org.institution.app.serializer.registration.impl;

import org.institution.app.serializer.registration.RegistrationSerializer;
import org.institution.app.model.Registration;
import java.util.List;

public class CsvRegistrationSerializer implements RegistrationSerializer {
    @Override
    public String export(List<Registration> registrations) {
        StringBuilder csv = new StringBuilder();

        for (Registration r : registrations) {
            objectToCsv(csv, r);
            csv.append("\n");
        }

        return csv.toString();
    }

    @Override
    public String getExtension() { return "csv"; }

    private static void objectToCsv(StringBuilder csv, Registration r) {
        csv.append(r.getStudentId());
        csv.append(",");
        csv.append(r.getCourseId());
        csv.append(",");
        csv.append(r.getGrade());
    }
}

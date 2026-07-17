package org.institution.app.service.export.registration;

import org.institution.app.model.Registration;
import java.util.List;

public interface RegistrationExportService {
    String export(List<Registration> registrations);
}

package org.institution.app.serializer.registration;

import org.institution.app.model.Registration;
import java.util.List;

public interface RegistrationSerializer {
    String export(List<Registration> registrations);

    String getExtension();
}

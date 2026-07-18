package org.institution.app.serializer.registration.impl;

import org.institution.app.serializer.registration.RegistrationSerializer;
import org.institution.app.model.Registration;
import java.util.List;

public class JsonRegistrationSerializer implements RegistrationSerializer {
    
    @Override
    public String export(List<Registration> registrations) {
        int indentation = 0;
        StringBuilder json = new StringBuilder();

        json.append("[\n");
        indentation++;
        indent(json, indentation);

        for (int i = 0; i < registrations.size(); i++) {
            objectToJson(json, registrations.get(i), indentation);
            
            if (i + 1 != registrations.size()) {
                json.append(",\n");
                indent(json, indentation);
            }
        }
        
        indentation--;
        json.append("\n"); 
        indent(json, indentation);
        json.append("]");

        return json.toString();
    }

    @Override
    public String getExtension() { return "json"; }

    private static void objectToJson(StringBuilder json, Registration r, int indentation) {
        json.append("{\n"); indentation++;

        appendField(json, "studentId", r.getStudentId(), indentation, true);
        appendField(json, "courseId", r.getCourseId(), indentation, false);
        appendField(json, "grade", r.getGrade(), indentation, false);

        indentation--;
        indent(json, indentation);
        json.append("}");
    }

    private static void indent(StringBuilder json, int times) {        
        for (int i = 0; i < times; i++) {
            json.append("\t");
        }
    }

    private static void appendField(StringBuilder json, String key, int value, int indentation, boolean isFirst) {
        beginField(json, isFirst, indentation);
        appendKey(json, key);
        json.append(value);
    }

    private static void appendField(StringBuilder json, String key, double value, int indentation, boolean isFirst) {
        beginField(json, isFirst, indentation);
        appendKey(json, key);
        json.append(value);
    }

    private static void beginField(StringBuilder json, boolean isFirst, int indentation) {
        if (!isFirst) json.append(",\n");
        indent(json, indentation);
    }

    private static void appendKey(StringBuilder json, String key) {
        json.append('"')
            .append(key)
            .append("\": ");
    }
}

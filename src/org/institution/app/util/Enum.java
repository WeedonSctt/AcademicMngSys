package org.institution.app.util;

public class Enum {
    public enum Error {
        // GLOBAL
	    ALREADY_CREATED,
        STUDENT_OR_COURSE_NOT_FOUND,
        EMAIL_ALREADY_IN_USE,

        // COURSE SERVICE
        INVALID_STUDENT_QUOTA,
        REPEATED_COURSE_NAME,
        COURSE_NOT_FOUND,

        // STUDENT SERVICE
        ACTIVE_STUDENT,
	    INACTIVE_STUDENT,
        INVALID_AGE_OR_EMAIL,
        STUDENT_NOT_FOUND,

        // TEACHER SERVICE
        TEACHER_NOT_FOUND,
        INVALID_EMAIL,

        // REGISTRATION SERVICE
        QUOTA_AT_LIMIT,
        NOT_ASSIGNED_TEACHER,
        INVALID_GRADE,
        REGISTRATION_NOT_FOUND,

        // OTHER SERVICES
        COULD_NOT_WRITE_TO_FILE,
        FILE_NOT_FOUND,



        INVALID_INPUT_DATA,
	    WRONG_INPUT_DATA,
	    PLACEHOLDER_VALUE,
        
    }

}

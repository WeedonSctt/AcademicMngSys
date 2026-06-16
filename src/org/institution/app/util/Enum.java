package org.institution.app.util;

public class Enum {
    public enum Sys {
        IN, OUT
    }

    public enum Error {
        INVALID_INPUT_DATA, 
	WRONG_INPUT_DATA,
        ACTIVE_STUDENT,
        TEACHER_NOT_FOUND,
        FILE_NOT_FOUND,
        COULD_NOT_WRITE_TO_FILE,
	ALREADY_CREATED,
	INACTIVE_STUDENT,
	PLACEHOLDER_VALUE,
        
    }

}

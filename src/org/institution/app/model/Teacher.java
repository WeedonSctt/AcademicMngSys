package org.institution.app.model;

public class Teacher {
    private final int id;
    private String name;
    private String department;
    private String email;

    // ALSO FOR LOADING DATA
    public Teacher(int id, String name, String department, String email) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
    }

    // Getters
    public int getID() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setEmail(String email) { this.email = email; }

}

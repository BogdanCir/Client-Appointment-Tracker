package domain;

import java.io.Serializable;

public class Patient extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String lastName, firstName;
    private final int age;

    public Patient(int id, String lastName, String firstName, int age){
        super(id);
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return id + " " + lastName + " " + firstName + " " + age;
    }
}

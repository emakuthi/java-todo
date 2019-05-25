package models;

import java.util.Objects;

public class Departments {
    private String name;
    private int id;


    public Departments(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Departments)) return false;
        Departments departments = (Departments) o;
        return id == departments.id &&
                Objects.equals(name, departments.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
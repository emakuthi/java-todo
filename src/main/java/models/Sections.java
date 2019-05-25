package models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Sections {

    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private int id;
    private int departmentId;

    public Sections(String description, int departmentId){
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.departmentId = departmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sections)) return false;
        Sections sections = (Sections) o;
        return completed == sections.completed &&
                id == sections.id &&
                Objects.equals(description, sections.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, completed, id);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public boolean getCompleted(){
        return this.completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

}
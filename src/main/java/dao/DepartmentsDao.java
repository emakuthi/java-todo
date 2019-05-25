package dao;

import models.Departments;
import models.Sections;
import java.util.List;

public interface DepartmentsDao {

    List<Sections> getAllTasksByCategory(int categoryId);

    //LIST
    List<Departments> getAll();

    //CREATE
    void add (Departments departments);

    //READ
    Departments findById(int id);

    //UPDATE
    void update(int id, String name);

    //DELETE
    void deleteById(int id);
    void clearAllCategories();
}
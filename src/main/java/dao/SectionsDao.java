package dao;

import models.Sections;
import java.util.List;

public interface SectionsDao {

    // LIST
    List<Sections> getAll();

    // CREATE
    void add(Sections sections);

    // READ
    Sections findById(int id);

    //UPDATE
    void update(int id, String content, int categoryId);

    //DELETE
    void deleteById(int id);
    void clearAllTasks();
}
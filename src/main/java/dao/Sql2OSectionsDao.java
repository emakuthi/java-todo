package dao;

import models.Sections;
import org.sql2o.*;
import java.util.List;

public class Sql2OSectionsDao implements SectionsDao { //implementing our interface

    private final Sql2o sql2o;

    public Sql2OSectionsDao(Sql2o sql2o){
        this.sql2o = sql2o; //making the sql2o object available everywhere so we can call methods in it
    }

    @Override
    public void add(Sections sections) {
        String sql = "INSERT INTO tasks (description, categoryId) VALUES (:description, :categoryId)"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
            int id = (int) con.createQuery(sql, true) //make a new variable
                    .bind(sections)
                    .executeUpdate() //run it all
                    .getKey(); //int id is now the row number (row “key”) of db
            sections.setId(id); //update object to set id now from database
        } catch (Sql2oException ex) {
            System.out.println(ex); //oops we have an error!
        }
    }

    @Override
    public List<Sections> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM tasks") //raw sql
                    .executeAndFetch(Sections.class); //fetch a list
        }
    }

    @Override
    public Sections findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM tasks WHERE id = :id")
                    .addParameter("id", id) //key/value pair, key must match above
                    .executeAndFetchFirst(Sections.class); //fetch an individual item
        }
    }

    @Override
    public void update(int id, String newDescription, int newCategoryId){
        String sql = "UPDATE tasks SET (description, categoryId) = (:description, :categoryId) WHERE id=:id";   //raw sql
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("description", newDescription)
                    .addParameter("categoryId", newCategoryId)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from tasks WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllSections() {
        String sql = "DELETE from tasks";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
}
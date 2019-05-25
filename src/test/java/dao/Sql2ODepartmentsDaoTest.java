package dao;

import models.Departments;
import models.Sections;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class Sql2ODepartmentsDaoTest {
    private Sql2ODepartmentsDao categoryDao;
    private Sql2OSectionsDao taskDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        categoryDao = new Sql2ODepartmentsDao(sql2o);
        taskDao = new Sql2OSectionsDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingCategorySetsId() throws Exception {
        Departments departments = setupNewCategory();
        int originalCategoryId = departments.getId();
        categoryDao.add(departments);
        assertNotEquals(originalCategoryId, departments.getId());
    }

    @Test
    public void existingCategoriesCanBeFoundById() throws Exception {
        Departments departments = setupNewCategory();
        categoryDao.add(departments);
        Departments foundDepartments = categoryDao.findById(departments.getId());
        assertEquals(departments, foundDepartments);
    }

    @Test
    public void addedCategoriesAreReturnedFromGetAll() throws Exception {
        Departments departments = setupNewCategory();
        categoryDao.add(departments);
        assertEquals(1, categoryDao.getAll().size());
    }

    @Test
    public void noCategoriesReturnsEmptyList() throws Exception {
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void updateChangesCategoryContent() throws Exception {
        String initialDescription = "Yardwork";
        Departments departments = new Departments(initialDescription);
        categoryDao.add(departments);
        categoryDao.update(departments.getId(),"Cleaning");
        Departments updatedDepartments = categoryDao.findById(departments.getId());
        assertNotEquals(initialDescription, updatedDepartments.getName());
    }

    @Test
    public void deleteByIdDeletesCorrectCategory() throws Exception {
        Departments departments = setupNewCategory();
        categoryDao.add(departments);
        categoryDao.deleteById(departments.getId());
        assertEquals(0, categoryDao.getAll().size());
    }

    @Test
    public void clearAllClearsAllCategories() throws Exception {
        Departments departments = setupNewCategory();
        Departments otherDepartments = new Departments("Cleaning");
        categoryDao.add(departments);
        categoryDao.add(otherDepartments);
        int daoSize = categoryDao.getAll().size();
        categoryDao.clearAllCategories();
        assertTrue(daoSize > 0 && daoSize > categoryDao.getAll().size());
    }

    @Test
    public void getAllTasksByCategoryReturnsTasksCorrectly() throws Exception {
        Departments departments = setupNewCategory();
        categoryDao.add(departments);
        int categoryId = departments.getId();
        Sections newSections = new Sections("mow the lawn", categoryId);
        Sections otherSections = new Sections("pull weeds", categoryId);
        Sections thirdSections = new Sections("trim hedge", categoryId);
        taskDao.add(newSections);
        taskDao.add(otherSections); //we are not adding task 3 so we can test things precisely.
        assertEquals(2, categoryDao.getAllTasksByCategory(categoryId).size());
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(newSections));
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(otherSections));
        assertFalse(categoryDao.getAllTasksByCategory(categoryId).contains(thirdSections)); //things are accurate!
    }

    // helper method
    public Departments setupNewCategory(){
        return new Departments("Yardwork");
    }
}
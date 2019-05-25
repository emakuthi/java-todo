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
    private Sql2ODepartmentsDao departmentDao;
    private Sql2OSectionsDao sectionDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        departmentDao = new Sql2ODepartmentsDao(sql2o);
        sectionDao = new Sql2OSectionsDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingCategorySetsId() throws Exception {
        Departments departments = setupNewCategory();
        int originalDepartmentId = departments.getId();
        departmentDao.add(departments);
        assertNotEquals(originalDepartmentId, departments.getId());
    }

    @Test
    public void existingDepartmentsCanBeFoundById() throws Exception {
        Departments departments = setupNewCategory();
        departmentDao.add(departments);
        Departments foundDepartments = departmentDao.findById(departments.getId());
        assertEquals(departments, foundDepartments);
    }

    @Test
    public void addedDepartmentsAreReturnedFromGetAll() throws Exception {
        Departments departments = setupNewCategory();
        departmentDao.add(departments);
        assertEquals(1, departmentDao.getAll().size());
    }

    @Test
    public void noDepartmentsReturnsEmptyList() throws Exception {
        assertEquals(0, departmentDao.getAll().size());
    }

    @Test
    public void updateChangesDepartmentContent() throws Exception {
        String initialDescription = "Yardwork";
        Departments departments = new Departments(initialDescription);
        departmentDao.add(departments);
        departmentDao.update(departments.getId(),"Cleaning");
        Departments updatedDepartments = departmentDao.findById(departments.getId());
        assertNotEquals(initialDescription, updatedDepartments.getName());
    }

    @Test
    public void deleteByIdDeletesCorrectDepartment() throws Exception {
        Departments departments = setupNewCategory();
        departmentDao.add(departments);
        departmentDao.deleteById(departments.getId());
        assertEquals(0, departmentDao.getAll().size());
    }

    @Test
    public void clearAllClearsAllCategories() throws Exception {
        Departments departments = setupNewCategory();
        Departments otherDepartments = new Departments("Cleaning");
        departmentDao.add(departments);
        departmentDao.add(otherDepartments);
        int daoSize = departmentDao.getAll().size();
        departmentDao.clearAllDepartments();
        assertTrue(daoSize > 0 && daoSize > departmentDao.getAll().size());
    }

    @Test
    public void getAllTasksByCategoryReturnsTasksCorrectly() throws Exception {
        Departments departments = setupNewCategory();
        departmentDao.add(departments);
        int categoryId = departments.getId();
        Sections newSections = new Sections("mow the lawn", categoryId);
        Sections otherSections = new Sections("pull weeds", categoryId);
        Sections thirdSections = new Sections("trim hedge", categoryId);
        sectionDao.add(newSections);
        sectionDao.add(otherSections); //we are not adding task 3 so we can test things precisely.
        assertEquals(2, departmentDao.getAllSectionsByDepartment(categoryId).size());
        assertTrue(departmentDao.getAllSectionsByDepartment(categoryId).contains(newSections));
        assertTrue(departmentDao.getAllSectionsByDepartment(categoryId).contains(otherSections));
        assertFalse(departmentDao.getAllSectionsByDepartment(categoryId).contains(thirdSections)); //things are accurate!
    }

    // helper method
    public Departments setupNewCategory(){
        return new Departments("Yardwork");
    }
}
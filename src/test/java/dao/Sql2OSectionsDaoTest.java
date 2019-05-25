package dao;

import models.Sections;
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Sql2OSectionsDaoTest {
    private Sql2OSectionsDao taskDao; //ignore me for now. We'll create this soon.
    private Connection conn; //must be sql2o class conn

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        taskDao = new Sql2OSectionsDao(sql2o); //ignore me for now
        conn = sql2o.open(); //keep connection open through entire test so it does not get erased
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingTaskSetsId() throws Exception {
        Sections sections = setupNewTask();
        int originalTaskId = sections.getId();
        taskDao.add(sections);
        assertNotEquals(originalTaskId, sections.getId()); //how does this work?
    }

    @Test
    public void existingTasksCanBeFoundById() throws Exception {
        Sections sections = setupNewTask();
        taskDao.add(sections); //add to dao (takes care of saving)
        Sections foundSections = taskDao.findById(sections.getId()); //retrieve
        assertEquals(sections, foundSections); //should be the same
    }

    @Test
    public void addedTasksAreReturnedFromgetAll() throws Exception {
        Sections sections = setupNewTask();
        taskDao.add(sections);
        assertEquals(1, taskDao.getAll().size());
    }

    @Test
    public void noTasksReturnsEmptyList() throws Exception {
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void updateChangesTaskContent() throws Exception {
        String initialDescription = "mow the lawn";
        Sections sections = setupNewTask();
        taskDao.add(sections);

        taskDao.update(sections.getId(),"brush the cat", 1);
        Sections updatedSections = taskDao.findById(sections.getId()); //why do I need to refind this?
        assertNotEquals(initialDescription, updatedSections.getDescription());
    }

    @Test
    public void deleteByIdDeletesCorrectTask() throws Exception {
        Sections sections = setupNewTask();
        taskDao.add(sections);
        taskDao.deleteById(sections.getId());
        assertEquals(0, taskDao.getAll().size());
    }

    @Test
    public void clearAllClearsAll() throws Exception {
        Sections sections = setupNewTask();
        Sections otherSections = new Sections("brush the cat", 2);
        taskDao.add(sections);
        taskDao.add(otherSections);
        int daoSize = taskDao.getAll().size();
        taskDao.clearAllTasks();
        assertTrue(daoSize > 0 && daoSize > taskDao.getAll().size()); //this is a little overcomplicated, but illustrates well how we might use `assertTrue` in a different way.
    }

    @Test
    public void categoryIdIsReturnedCorrectly() throws Exception {
        Sections sections = setupNewTask();
        int originalCatId = sections.getCategoryId();
        taskDao.add(sections);
        assertEquals(originalCatId, taskDao.findById(sections.getId()).getCategoryId());
    }

    //define the following once and then call it as above in your tests.
    public Sections setupNewTask(){
        return new Sections("Mow the lawn", 1);
    }
}

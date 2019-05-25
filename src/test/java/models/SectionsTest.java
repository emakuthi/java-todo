package models;

import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class SectionsTest {
    @Test
    public void NewTaskObjectGetsCorrectlyCreated_true() throws Exception {
        Sections sections = setupNewTask();
        assertEquals(true, sections instanceof Sections);
    }

    @Test
    public void TaskInstantiatesWithDescription_true() throws Exception {
        Sections sections = setupNewTask();
        assertEquals("Mow the lawn", sections.getDescription());
    }

    @Test
    public void isCompletedPropertyIsFalseAfterInstantiation() throws Exception {
        Sections sections = setupNewTask();
        assertEquals(false, sections.getCompleted()); //should never start as completed
    }

    @Test
    public void getCreatedAtInstantiatesWithCurrentTimeToday() throws Exception {
        Sections sections = setupNewTask();
        assertEquals(LocalDateTime.now().getDayOfWeek(), sections.getCreatedAt().getDayOfWeek());
    }

    //helper methods
    public Sections setupNewTask(){
        return new Sections("Mow the lawn", 1);
    }
}
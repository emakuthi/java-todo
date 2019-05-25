import java.util.HashMap;
import java.util.List;
import java.util.Map;


import dao.Sql2ODepartmentsDao;
import dao.Sql2OSectionsDao;
import models.Departments;
import models.Sections;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) { //type “psvm + tab” to autocreate this
        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/todolist.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Sql2OSectionsDao sectionDao = new Sql2OSectionsDao(sql2o);
        Sql2ODepartmentsDao departmentDao = new Sql2ODepartmentsDao(sql2o);

        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 4567;
        }
        port(port);

        //get: show all sections in all departments and show all departments
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Departments> allDepartments = departmentDao.getAll();
            model.put("departments", allDepartments);
            List<Sections> sections = sectionDao.getAll();
            model.put("sections", sections);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a form to create a new departments
        get("/departments/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Departments> departments = departmentDao.getAll(); //refresh list of links for navbar
            model.put("departments", departments);
            return new ModelAndView(model, "department-form.hbs"); //new layout
        }, new HandlebarsTemplateEngine());

        //post: process a form to create a new department
        post("/departments", (req, res) -> { //new
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            Departments newDepartments = new Departments(name);
            departmentDao.add(newDepartments);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());


        //get: delete all departments and all sections
        get("/departments/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            departmentDao.clearAllDepartments();
            sectionDao.clearAllSections();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: delete all sections
        get("/sections/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            sectionDao.clearAllSections();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get a specific department (and the sections it contains)
        get("/departments/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfDepartmentToFind = Integer.parseInt(req.params("id")); //new
            Departments foundDepartments = departmentDao.findById(idOfDepartmentToFind);
            model.put("department", foundDepartments);
            List<Sections> allSectionsByDepartment = departmentDao.getAllSectionsByDepartment(idOfDepartmentToFind);
            model.put("sections", allSectionsByDepartment);
            model.put("departments", departmentDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "department-detail.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a department
        get("/departments/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("editDepartment", true);
            Departments departments = departmentDao.findById(Integer.parseInt(req.params("id")));
            model.put("departments", departments);
            model.put("departments", departmentDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "department-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update a department
        post("/departments/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfDepartmentToEdit = Integer.parseInt(req.params("id"));
            String newName = req.queryParams("newDepartmentName");
            departmentDao.update(idOfDepartmentToEdit, newName);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: delete an individual section
        get("/departments/:department_id/sections/:section_id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfSectionToDelete = Integer.parseInt(req.params("section_id"));
            sectionDao.deleteById(idOfSectionToDelete);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: show new section form
        get("/sections/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Departments> departments = departmentDao.getAll();
            model.put("departments", departments);
            return new ModelAndView(model, "section-form.hbs");
        }, new HandlebarsTemplateEngine());

        //task: process new section form
        post("/sections", (req, res) -> { //URL to make new task on POST route
            Map<String, Object> model = new HashMap<>();
            List<Departments> allDepartments = departmentDao.getAll();
            model.put("departments", allDepartments);
            String description = req.queryParams("description");
            int departmentId = Integer.parseInt(req.queryParams("categoryId"));
            Sections newSections = new Sections(description, departmentId);        //See what we did with the hard coded departmentId?
            sectionDao.add(newSections);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: show an individual section that is nested in a department
        get("/departments/:department_id/sections/:section_id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfSectionToFind = Integer.parseInt(req.params("section_id")); //pull id - must match route segment
            Sections foundSections = sectionDao.findById(idOfSectionToFind); //use it to find task
            int idOfDepartmentToFind = Integer.parseInt(req.params("department_id"));
            Departments foundDepartments = departmentDao.findById(idOfDepartmentToFind);
            model.put("department", foundDepartments);
            model.put("section", foundSections); //add it to model for template to display
            model.put("departments", departmentDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "section-detail.hbs"); //individual section page.
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a section
        get("/sections/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Departments> allDepartments = departmentDao.getAll();
            model.put("departments", allDepartments);
            Sections sections = sectionDao.findById(Integer.parseInt(req.params("id")));
            model.put("sections", sections);
            model.put("editSection", true);
            return new ModelAndView(model, "sections-form.hbs");
        }, new HandlebarsTemplateEngine());

        //task: process a form to update a section
        post("/sections/:id", (req, res) -> { //URL to update task on POST route
            Map<String, Object> model = new HashMap<>();
            int sectionToEditId = Integer.parseInt(req.params("id"));
            String newContent = req.queryParams("description");
            int newDepartmentId = Integer.parseInt(req.queryParams("categoryId"));
            sectionDao.update(sectionToEditId, newContent, newDepartmentId);  // remember the hardcoded categoryId we placed? See what we've done to/with it?
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

    }
}

package org.kenta.kosugi.employee.api;

import org.kenta.kosugi.employee.model.Employee;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Employee Resource class.
 * Provided REST API for accessing Employee resources.
 *
 * @author Kenta Kosugi
 */
@RequestScoped
@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    @Inject
    private EntityManager entityManager;

    @Inject
    private Validator validator;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * Find the employee by using employee id.
     *
     * @param id The id of employee that you want to search.
     * @return If find a employee by using employee id, return Employee object.<br>
     * If not find, return null object.
     */
    @GET
    @Path("/find/id/{id}")
    public Employee find(@PathParam("id") String id) {
        return this.entityManager.find(Employee.class, id);
    }

    /**
     * Find the employee by using first name or last name.
     *
     * @param name Name of employee that you want to search.
     * @return If find any employees, return List of Employee.<br>
     * If not find, return null object.
     */
    @GET
    @Path("/find/name/{name}")
    public List<Employee> findEmployeeByName(@PathParam("name") String name) {
        return this.entityManager.createNamedQuery("Employee.findByName", Employee.class)
                .setParameter("name", name)
                .getResultList();
    }

    /**
     * Register the employee object to EMPLOYEE table.
     *
     * @param id        Employee id
     * @param firstName First name of employee that you want to register.
     * @param lastName  Last name of employee that you want to register.
     * @param hiredDate The hired date of employee that you want to register.
     * @param email     The email address of employee that you want to register.
     * @return If register request success, return the 200 OK response and employee object.
     * If not success, return 500 error response.
     */
    @POST
    @Path("/register/{id}")
    @Transactional
    public Response register(@PathParam("id") String id, @QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName, @QueryParam("hiredDate") String hiredDate, @QueryParam("email") String email) {
        // Get Calendar instance.
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(hiredDate));
        } catch (ParseException e) {
            return Response.status(500).encoding(e.getMessage()).build();
        }

        // New Employee object.
        Employee employee = new Employee(id, firstName, null, lastName, calendar, email);

        // Bean Validation.
        Set<ConstraintViolation<Employee>> constraints = this.validator.validate(employee);

        StringBuilder errors = new StringBuilder();
        if (constraints.size() != 0) {
            for (ConstraintViolation<Employee> constraintViolation : constraints) {
                errors.append(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage() + "<br />");
            }

            // Return error code.
            return Response.status(500).encoding(errors.toString()).build();
        }

        // Persist
        this.entityManager.persist(employee);

        // Return 200 OK response.
        return Response.ok(employee).build();
    }

    /**
     * Delete employee from EMPLOYEE table.<br>
     * Logical delete instead of physical delete.
     *
     * @param id          The id of employee that you want to delete.
     * @param retiredDate The retired date of employee.
     * @return When request success, return 200 response.<br>
     * When request fail, 400 error response or 500 error response.
     */
    @DELETE
    @Path("/delete/{id}")
    @Transactional
    public Response delete(@PathParam("id") String id, @QueryParam("retiredDate") String retiredDate) {

        // Find employee object by using employee id
        Employee employee = this.entityManager.find(Employee.class, id);

        // When not found.
        if (employee == null) {
            return Response.status(404).encoding("Employee(id = " + id + " does not found.").build();
        }

        Calendar calendar = Calendar.getInstance();
        try {
            // Parsing String object to Calendar object.
            calendar.setTime(this.simpleDateFormat.parse(retiredDate));
        } catch (ParseException e) {
            // When parse error happens, return 500 error code.
            return Response.status(500).encoding(e.getMessage()).build();
        }

        // Set retired date.
        employee.retiredDate = calendar;

        // Flush
        this.entityManager.flush();

        // Return 200 OK response.
        return Response.status(200).entity(employee).build();
    }

}

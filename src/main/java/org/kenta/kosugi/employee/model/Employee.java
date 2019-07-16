package org.kenta.kosugi.employee.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Employee class.
 *
 * @author Kenta Kosugi
 */
@Entity
@Table(name = "EMPLOYEE", indexes = {
        @Index(name = "IDX_FIRST_NAME", columnList = "FIRST_NAME"),
        @Index(name = "IDX_LAST_NAME", columnList = "LAST_NAME")
}
)
@NamedQueries({
        @NamedQuery(name = "Employee.findAll", query = "select a from Employee a where a.retiredDate is null"),
        @NamedQuery(name = "Employee.findByName", query = "select a from Employee a where a.retiredDate like :name or a.lastName like :name")
})
public class Employee implements Serializable {

    private static final long serialVersionUID = -1625260879241651180L;

    @NotNull
    @Size(min = 6, max = 6)
    @Pattern(regexp = "^[0-9][0-9]{5}")
    @Id
    @Column(name = "ID", length = 8)
    public String id;

    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "FIRST_NAME", length = 64, nullable = false)
    public String firstName;

    @Size(max = 64)
    @Column(name = "MIDDLE_NAME", length = 64)
    public String middleName;

    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "LAST_NAME", length = 64, nullable = false)
    public String lastName;

    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    @Column(name = "HIRED_DATE")
    public Calendar hiredDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "RETIRED_DATE")
    public Calendar retiredDate;

    @NotNull
    @Size(min = 3, max = 64)
    @Email
    @Column(name = "EMAIL", length = 64, nullable = false)
    public String email;

    /**
     * Default constructor.
     */
    public Employee() {
    }

    /**
     * Constructor for Employee class.
     *
     * @param id         Id of employee. ^[0-9][0-9]{5}
     * @param firstName  First name associated with this employee. <br>
     *                   Not Allow setting null.
     * @param middleName Middle name associated with this employee. <br>
     *                   Allow setting null.
     * @param lastName   Last name associated with this employee. <br>
     *                   Not Allow setting null.
     * @param hiredDate  The date that this employee hired by NetApp. <br>
     *                   Not Allow setting null.
     * @param email      Email address associated with this employee.
     */
    public Employee(@NotNull @Size(min = 6, max = 6) @Pattern(regexp = "^[0-9][0-9]{5}") String id, @NotNull @Size(min = 1, max = 64) String firstName, @Size(max = 64) String middleName, @NotNull @Size(min = 1, max = 64) String lastName, @NotNull @Past Calendar hiredDate, @NotNull @Size(min = 3, max = 64) @Email String email) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.hiredDate = hiredDate;
        this.email = email;
    }

}

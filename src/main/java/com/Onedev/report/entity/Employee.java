

package com.Onedev.report.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job jobId;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department departmentId;

    @Column(name = "salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(name = "created_at", updatable = false, insertable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "update_at", insertable = false)
    @LastModifiedBy
    private ZonedDateTime updatedAt;
}




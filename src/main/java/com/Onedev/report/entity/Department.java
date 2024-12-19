package com.Onedev.report.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(name = "created_at", updatable = false, insertable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "update_at", insertable = false)
    @LastModifiedBy
    private ZonedDateTime updatedAt;
}
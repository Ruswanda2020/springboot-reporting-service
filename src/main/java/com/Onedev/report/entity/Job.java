package com.Onedev.report.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "job_title", nullable = false, length = 100)
    private String jobTitle;

    @Column(name = "min_salary", precision = 10, scale = 2)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 10, scale = 2)
    private BigDecimal maxSalary;

    @Column(name = "created_at", updatable = false, insertable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "update_at", insertable = false)
    @LastModifiedBy
    private ZonedDateTime updatedAt;
}



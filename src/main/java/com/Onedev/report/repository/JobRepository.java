package com.Onedev.report.repository;

import com.Onedev.report.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository  extends JpaRepository<Job, Long> {
}

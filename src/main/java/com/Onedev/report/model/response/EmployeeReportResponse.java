package com.Onedev.report.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeReportResponse {
    private String first_name;
    private String last_name;
    private BigDecimal salary;
    private LocalDate hire_date;
    private String department_name;
    private String job_title;
}

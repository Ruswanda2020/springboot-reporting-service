package com.Onedev.report.controller;

import com.Onedev.report.Service.EmployeeService;
import com.Onedev.report.model.response.File;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employees")
    public ResponseEntity<File> generateReportEmployees(@RequestParam("month") String month) throws JRException {
        return ResponseEntity.ok(employeeService.generateEmployeeReportPdf(month));
    }

}

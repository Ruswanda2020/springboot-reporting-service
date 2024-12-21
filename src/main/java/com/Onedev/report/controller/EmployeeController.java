package com.Onedev.report.controller;

import com.Onedev.report.Service.EmployeeService;
import com.Onedev.report.model.response.FileResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employees/report-pdf")
    public ResponseEntity<FileResponse> generateReportEmployeesPdf(@RequestParam("month") String month) throws JRException {
        return ResponseEntity.ok(employeeService.generateEmployeeReportPdf(month));
    }

    @PostMapping("/employees/report-excel")
    public ResponseEntity<FileResponse> generateReportEmployeesExcel(@RequestParam("month") String month) throws IOException {
        return ResponseEntity.ok(employeeService.generateEmployeeReportExcel(month));
    }

}

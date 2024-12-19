package com.Onedev.report.Service.impl;

import com.Onedev.report.Service.EmployeeService;
import com.Onedev.report.entity.Employee;
import com.Onedev.report.model.response.EmployeeReportResponse;
import com.Onedev.report.model.response.File;
import com.Onedev.report.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Override
    public File generateEmployeeReportPdf(String month) throws JRException {
        File file = new File();
        file.setFileName("employeesReport" + month);
        file.setFileExt("pdf");
        List<EmployeeReportResponse> res = employeeRepository.findAll()
                .stream()
                .map(this::mappingToResponse)
                .toList();
        List<EmployeeReportResponse> employeeReportResponse = new ArrayList<>(res);


        JRBeanCollectionDataSource employees = new JRBeanCollectionDataSource(employeeReportResponse);

        // Parameters for the report (if needed)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("monthReport", month);
        parameters.put("employees", employees);

        InputStream filePath = getClass().getClassLoader().getResourceAsStream("templates/employeesReport.jrxml");

        JasperReport report = JasperCompileManager.compileReport(filePath);
        JasperPrint print = JasperFillManager.fillReport(report, parameters, employees);
        removeBlankPage(print.getPages());
        byte[] bytes = JasperExportManager.exportReportToPdf(print);
        file.setData(bytes);
        return file;
    }

    @Override
    public File generateEmployeeReportExcel(String month) {
        File file = new File();
        file.setFileName("employeesReport" + month);
        file.setFileExt("xls");
        final String excelName = "employee Report" + month;

        //create workbook
        Workbook employeeReportWorkbook = new HSSFWorkbook();
        //create sheet
        Sheet employeeReportSheet = employeeReportWorkbook.createSheet(excelName);

        return null;
    }

    private void removeBlankPage(List<JRPrintPage> pages) {
        pages.removeIf(page -> page.getElements().isEmpty());
    }

    private EmployeeReportResponse mappingToResponse(Employee employee){
        EmployeeReportResponse response = new EmployeeReportResponse();
        response.setDepartment_name(employee.getDepartmentId().getDepartmentName());
        response.setSalary(employee.getSalary());
        response.setFirst_name(employee.getFirstName());
        response.setLast_name(employee.getLastName());
        response.setHire_date(employee.getHireDate());
        response.setJob_title(employee.getJobId().getJobTitle());
        return response;
    }
}

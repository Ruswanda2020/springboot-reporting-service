package com.Onedev.report.Service.impl;

import com.Onedev.report.Service.EmployeeService;
import com.Onedev.report.entity.Employee;
import com.Onedev.report.model.response.EmployeeReportResponse;
import com.Onedev.report.model.response.FileResponse;
import com.Onedev.report.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public FileResponse generateEmployeeReportPdf(String month) throws JRException {
        FileResponse file = new FileResponse();
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

    @Override
    public FileResponse generateEmployeeReportExcel(String month) throws IOException {
        FileResponse file = new FileResponse();
        file.setFileName("Employees Report " + month);
        file.setFileExt("xls");
        final String excelName = "Employee Report " + month;

        // Create workbook
        Workbook employeeReportWorkbook = new HSSFWorkbook();
        Sheet employeeReportSheet = employeeReportWorkbook.createSheet(excelName);

        CreationHelper creationHelper = employeeReportWorkbook.getCreationHelper();

        // Title
        Row titleRow = employeeReportSheet.createRow(2);
        Cell titleCell = titleRow.createCell(6);
        titleCell.setCellValue(excelName);

        CellStyle titleStyle = employeeReportWorkbook.createCellStyle();
        Font titleFont = employeeReportWorkbook.createFont();
        titleFont.setFontName("Arial");
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.RIGHT);
        titleCell.setCellStyle(titleStyle);

        employeeReportSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        // Add Logo
        ClientAnchor clientAnchor = creationHelper.createClientAnchor();
        clientAnchor.setRow1(1);
        clientAnchor.setRow2(4);
        clientAnchor.setCol1(0);
        clientAnchor.setCol2(2);

        Drawing<?> drawing = employeeReportSheet.createDrawingPatriarch();
        InputStream companyLogo = getClass().getClassLoader().getResourceAsStream("logo/X-Logo.png");
        assert companyLogo != null;
        int pictureIdx = employeeReportWorkbook.addPicture(IOUtils.toByteArray(companyLogo), Workbook.PICTURE_TYPE_PNG);
        Picture picture = drawing.createPicture(clientAnchor, pictureIdx);
        picture.resize(0.8);

        // Column widths
        employeeReportSheet.setColumnWidth(0, 6 * 256);
        employeeReportSheet.setColumnWidth(1, 15 * 256);
        employeeReportSheet.setColumnWidth(2, 15 * 256);
        employeeReportSheet.setColumnWidth(3, 10 * 256);
        employeeReportSheet.setColumnWidth(4, 15 * 256);
        employeeReportSheet.setColumnWidth(5, 20 * 256);
        employeeReportSheet.setColumnWidth(6, 20 * 256);

        // Header
        Row headerRow = employeeReportSheet.createRow(4);
        String[] headers = {"No", "First Name", "Last Name", "Salary", "Hire Date", "Department", "Job Title"};

        CellStyle headerStyle = employeeReportWorkbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        Font headerFont = employeeReportWorkbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);
        }

        // Data rows
        List<Employee> employees = employeeRepository.findAll();
        int rowNum = 5;

        CellStyle recordStyle = employeeReportWorkbook.createCellStyle();
        recordStyle.setBorderBottom(BorderStyle.THIN);
        recordStyle.setBorderTop(BorderStyle.THIN);
        recordStyle.setBorderLeft(BorderStyle.THIN);
        recordStyle.setBorderRight(BorderStyle.THIN);

        DataFormat format = employeeReportWorkbook.createDataFormat();
        recordStyle.setDataFormat(format.getFormat("#,##0"));

        AtomicInteger index = new AtomicInteger(1);

        for (Employee employee : employees) {
            Row dataRow = employeeReportSheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(index.getAndIncrement()); // No
            dataRow.createCell(1).setCellValue(employee.getFirstName()); // First Name
            dataRow.createCell(2).setCellValue(employee.getLastName()); // Last Name
            Cell salaryCell = dataRow.createCell(3);
            salaryCell.setCellValue(employee.getSalary().doubleValue()); // Salary
            salaryCell.setCellStyle(recordStyle);
            dataRow.createCell(4).setCellValue(employee.getHireDate().toString()); // Hire Date
            dataRow.createCell(5).setCellValue(employee.getDepartmentId().getDepartmentName()); // Department
            dataRow.createCell(6).setCellValue(employee.getJobId().getJobTitle()); // Job Title

            // Apply recordStyle to all cells
            for (int i = 0; i < 7; i++) {
                dataRow.getCell(i).setCellStyle(recordStyle);
            }
        }

        // Summary
        Row summaryRow = employeeReportSheet.createRow(rowNum + 2);
        Cell summaryCell = summaryRow.createCell(0);
        summaryCell.setCellValue("Total Salary");

        Cell sumCell = summaryRow.createCell(3);
        String formula = String.format("SUM(D6:D%d)", rowNum - 1);
        sumCell.setCellFormula(formula);

        CellStyle summaryStyle = employeeReportWorkbook.createCellStyle();
        summaryStyle.setDataFormat(format.getFormat("#,##0"));
        sumCell.setCellStyle(summaryStyle);

        employeeReportSheet.addMergedRegion(new CellRangeAddress(rowNum + 2, rowNum + 2, 0, 2));

        // Write workbook to output stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            employeeReportWorkbook.write(bos);
        } finally {
            bos.close();
        }

        byte[] bytes = bos.toByteArray();
        file.setData(bytes);
        return file;
    }




}

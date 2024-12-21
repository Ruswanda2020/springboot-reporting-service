package com.Onedev.report.Service;

import com.Onedev.report.model.response.FileResponse;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface EmployeeService {
    FileResponse generateEmployeeReportPdf(String month) throws JRException;
    FileResponse generateEmployeeReportExcel(String month) throws IOException;
}

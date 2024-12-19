package com.Onedev.report.Service;

import com.Onedev.report.model.response.File;
import net.sf.jasperreports.engine.JRException;

public interface EmployeeService {
    File generateEmployeeReportPdf(String month) throws JRException;
    File generateEmployeeReportExcel(String month);
}

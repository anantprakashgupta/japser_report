package jasper_export;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class GenerateMultipleReportsFromJasper {
 
  public static void main(String[] args) {

  //Jrxml file source
  String sourceFileName = "/home/prakash/Desktop/my_test.jrxml";
  //destination file location
  String outXlsName = "/home/prakash/Desktop/apgtest.xls";
  //If we want to pass any parameters to jasper report, then we can use this map
  HashMap xlsParams = new HashMap();
  Connection con = null;
  try {
    Class.forName("com.mysql.jdbc.Driver");
    // here employee is database name, root is username and password
    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jasper?characterEncoding=utf8", "root", "root");
    JasperReport report = JasperCompileManager.compileReport(sourceFileName );
    JasperPrint xlsPrint = JasperFillManager.fillReport(report, xlsParams, con);
    JRXlsExporter xlsExporter = new JRXlsExporter();
    xlsExporter.setExporterInput(new SimpleExporterInput(xlsPrint));
    xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outXlsName));
    SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
    xlsReportConfiguration.setOnePagePerSheet(false);
    xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
    xlsReportConfiguration.setDetectCellType(false);
    xlsReportConfiguration.setWhitePageBackground(false);
    xlsExporter.setConfiguration(xlsReportConfiguration);
    xlsExporter.exportReport();
  } catch (Exception e) {
      System.out.println(e);
    }
    finally{
      try{
 if(con != null){
    con.close();
 }
 
      }
    catch(Exception ex){
 }
    }
  }

}
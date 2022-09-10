package jasper_export;
	import java.io.File;
	import java.io.InputStream;
	import java.sql.Connection;
	import java.sql.SQLException;
	import java.util.Map;
	import javax.naming.Context;
	import javax.naming.InitialContext;
	import javax.servlet.ServletContext;
	import javax.sql.DataSource;
	import net.sf.jasperreports.engine.JRException;
	import net.sf.jasperreports.engine.JRExporterParameter;
	import net.sf.jasperreports.engine.JRParameter;
	import net.sf.jasperreports.engine.JasperFillManager;
	import net.sf.jasperreports.engine.JasperPrint;
	import net.sf.jasperreports.engine.JasperReport;
	import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
	import net.sf.jasperreports.engine.export.JRPdfExporter;
	import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
	import net.sf.jasperreports.engine.util.JRLoader;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	//import com.blogspot.na5cent.util.JSFSpringUtils;
	 
	/**
	 *jar
	 * @author redcrow
	 */
	public class jasper {
	 
	    private static final Logger LOG = LoggerFactory.getLogger(jasper.class);
	    private static Connection conn = null;
	 
	    public static void exportPDFToFile(JasperReport jasper, Object[] models, Map reportParam, File outputFile) throws JRException {
	        JRFileVirtualizer virtualizer = null;
	        try {
	            //write report to temp file, protect out of memory
	            virtualizer = new JRFileVirtualizer(2, System.getProperty("java.io.tmpdir"));
	            reportParam.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
	             
	            //pass models into jasper bean   
	            JRBeanArrayDataSource beans = new JRBeanArrayDataSource(models);
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, reportParam, beans);
	 
	            //create exporter and set basic parameter
	            JRPdfExporter exporter = new JRPdfExporter();
	            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, outputFile);
	            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	 
	            exporter.exportReport(); //export
	        } finally {
	            if (virtualizer != null) {
	                virtualizer.cleanup(); //clean up temp file
	            }
	        }
	    }
	 
	    public static void exportPDFToFile(InputStream jasperInputStream, Object[] models, Map reportParam, File outputFile) throws JRException {
	        JasperReport jasper = (JasperReport) JRLoader.loadObject(jasperInputStream);
	        exportPDFToFile(jasper, models, reportParam, outputFile);
	    }
	 
	    public static void exportPDFToFileWithConnection(JasperReport jasper, Map reportParam, File outputFile, Connection connection) throws JRException {
	        JRFileVirtualizer virtualizer = null;
	        try {
	            //write report to temp file, protect out of memory
	            virtualizer = new JRFileVirtualizer(2, System.getProperty("java.io.tmpdir"));
	            reportParam.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
	 
	            //user connection (inject database connection into jasper report)
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, reportParam, connection);
	 
	            //create exporter and set basic parameter
	            JRPdfExporter exporter = new JRPdfExporter();
	            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, outputFile);
	            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	 
	            exporter.exportReport(); //export
	        } finally {
	            if (virtualizer != null) {
	                virtualizer.cleanup(); //clean up temp file
	            }
	 
	            if (connection != null) {
	                try {
	                    connection.close(); //close database connection
	                } catch (SQLException ex) {
	                    LOG.warn(null, ex);
	                }
	            }
	        }
	    }
	 
	    public static void exportPDFToFileConnection(InputStream jasperInputStream, Map reportParam, File outputFile, Connection connection) throws JRException {
	        JasperReport jasper = (JasperReport) JRLoader.loadObject(jasperInputStream);
	        exportPDFToFileWithConnection(jasper, reportParam, outputFile, connection);
	    }
	 
	    public static Connection getConnection(ServletContext servletContext) {
	        try {
	 
	            if (conn == null || conn.isClosed()) {
	                //Context ctx = new InitialContext();
	                //DataSource ds = (DataSource) ctx.lookup("JNDI_NAME");
	                //get datbase connection by spring util, you can learn at http://na5cent.blogspot.com/2012/12/jsf-get-service-by-webapplicationcontex.html
//	                DataSource ds = JSFSpringUtils.getBean(servletContext, DataSource.class); 
//	                conn = ds.getConnection();
	            }
	            return conn;
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	 
	 
	    }
	 
	    public static void closeConnection() throws SQLException {
	        if (conn != null && !conn.isClosed()) {
	            conn.close();
	        }
	    }
	    public static void main(String[] args) {
	    
	    
		}
	    
	    
	}





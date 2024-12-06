package Library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

public class Report extends JFrame {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    public Report(String fileName) {
        this(fileName, null);
    }

    public Report(String fileName, HashMap<String, Object> parameter) {
        super("View Report");
        
        // Set window properties before loading report
        setSize(1366, 1024);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        Connection con = null;
        try {
            // Establish database connection
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Generate report
            JasperPrint jp = JasperFillManager.fillReport(fileName, parameter, con);
            
            // Check if report has pages
            if (jp.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No data found for the report", 
                    "Empty Report", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the window
                return;
            }
            
            // Add report viewer to frame
            JRViewer jrv = new JRViewer(jp);
            getContentPane().add(jrv);
            
            // Make window visible after everything is set up
            setVisible(true);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Database Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        } catch (JRException e) {
            JOptionPane.showMessageDialog(this,
                "Report Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        } finally {
            // Close database connection
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
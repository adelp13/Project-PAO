package utilityAndServices;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import learning.Course;


public class AuditMeniu{
    private static AuditMeniu auditMeniu;
    private static final String auditPath = "D:\\facultate\\II\\PAO\\Project PAO\\Project-PAO\\src\\auditMenu.csv";

    private AuditMeniu() {
    }
    public static AuditMeniu getAuditMeniu() {
        if (auditMeniu == null)
            auditMeniu = new AuditMeniu();
        return auditMeniu;
    }

    public void insertAction(String menuCommand) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(auditPath, true))) {
            String timeStamp = UtilityClass.getCurrentDate();
            writer.println(menuCommand + ", " + timeStamp);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Action could not be registered in the audit: " + e.getMessage());
        }
    }
}

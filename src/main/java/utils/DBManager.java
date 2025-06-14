// src/main/java/utils/DBManager.java

package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3307/keyword_framework";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    /**
     * Existing method—fetches all steps for a given testcase.
     */
    public static List<String[]> getTestSteps(String testcaseId) {
        List<String[]> steps = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT a.name as action, l.locator_value, ts.testdata " +
                    "FROM testcase_steps ts " +
                    "JOIN action a ON ts.action_id = a.id " +
                    "JOIN locator l ON ts.locator_id = l.id " +
                    "WHERE ts.testcase_id = ? ORDER BY ts.step_number";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, testcaseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                steps.add(new String[]{
                        rs.getString("action"),
                        rs.getString("locator_value"),
                        rs.getString("testdata")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return steps;
    }

    /**
     * New method—fetches all enabled testcase IDs.
     * Adjust the SQL WHERE clause if you need to filter by "enabled = 'Y'" or by suite, etc.
     */
    public static List<String> getAllTestcaseIds() {
        List<String> testcaseIds = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // If you want to only pick enabled testcases, add WHERE enabled='Y'
            String query = "SELECT id FROM testcase";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                testcaseIds.add(rs.getString("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testcaseIds;
    }
}

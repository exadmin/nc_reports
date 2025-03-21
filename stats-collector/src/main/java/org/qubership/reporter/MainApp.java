package org.qubership.reporter;

import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.renderers.db.HSQLDBRenderer;
import org.qubership.reporter.renderers.html.HtmlRenderer;
import org.qubership.reporter.renderers.json.JsonRenderer;
import org.qubership.reporter.utils.JDBCUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class MainApp {
    public static void main(String[] args) throws Exception{
        // Initializing DB
        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        try (Connection jdbcConn = DriverManager.getConnection("jdbc:hsqldb:file:./reports-are-here/data/db/hsqldb", "SA", "")) {
            jdbcConn.setSchema("PUBLIC");

            // Run analyze and report creation
            RepositoriesAnalyzer analyzer = new RepositoriesAnalyzer();
            ReportModel report = analyzer.analyzeAllIn(args[0]);

            // Render results into html format
            String htmlReportFileName = args[0] + File.separator + "combined-report.html";
            HtmlRenderer htmlRenderer = new HtmlRenderer();
            htmlRenderer.createHtmlFile(report, htmlReportFileName);

            // Save current results as json data file
            String jsonDataFileName = args[0] + File.separator + "persisted-data.json";
            JsonRenderer jsonRenderer = new JsonRenderer();
            jsonRenderer.saveToFile(report, jsonDataFileName);

            // Save current results into HSQL DB
            HSQLDBRenderer hsqldbRenderer = new HSQLDBRenderer();
            hsqldbRenderer.saveToDB(jdbcConn, report);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // report.saveReposToFileForDebugAims("z:\\all_repos.csv");
    }
}

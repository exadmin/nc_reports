package org.qubership.reporter;

import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.renderers.db.HSQLDBRenderer;
import org.qubership.reporter.renderers.html.HtmlRenderer;
import org.qubership.reporter.renderers.json.JsonRenderer;
import org.qubership.reporter.utils.StrUtils;
import org.qubership.reporter.utils.TheLogger;
import org.qubership.reporter.utils.TokenHolder;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class MainApp {
    public static void main(String[] args) throws Exception{
        // Initializing DB
        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        if (args.length != 3) {
            TheLogger.error("Unexpected number of parameters. java -jar xxx.jar $PATH_TO_DIR_WITH_REPOS$ $PATH_TO_HSQLDB_FILE$ $GIT_HIB_PERSONAL_TOKEN$");
        }

        String dbFile = args[1];
        TokenHolder.setDbFilePath(dbFile);
        TokenHolder.setPersonalToken(args[2]);

        TheLogger.debug("Repositories directory = " + args[0]);
        TheLogger.debug("Database file = " + dbFile);
        TheLogger.debug("GitHub personal token is " + (!StrUtils.isEmpty(TokenHolder.getPersonalToken()) ? "set" : "not set"));

        try (Connection jdbcConn = DriverManager.getConnection("jdbc:hsqldb:file:" + dbFile + ";ifexists=false", "SA", "")) {
            jdbcConn.setSchema("PUBLIC");
            jdbcConn.setAutoCommit(true);

            HSQLDBRenderer hsqldbRenderer = new HSQLDBRenderer();
            hsqldbRenderer.ensureDB(jdbcConn);

            // Run analyze and report creation
            RepositoriesAnalyzer analyzer = new RepositoriesAnalyzer();
            ReportModel report = analyzer.analyzeAllIn(args[0], jdbcConn);

            // Render results into html format
            String htmlReportFileName = args[0] + File.separator + "combined-report.html";
            HtmlRenderer htmlRenderer = new HtmlRenderer();
            htmlRenderer.createHtmlFile(report, htmlReportFileName);
            TheLogger.debug("New report file is created: " + htmlReportFileName);

            // Save current results as json data file
            String jsonDataFileName = args[0] + File.separator + "persisted-data.json";
            JsonRenderer jsonRenderer = new JsonRenderer();
            jsonRenderer.saveToFile(report, jsonDataFileName);

            // Save current results into HSQL DB
            hsqldbRenderer.saveToDB(jdbcConn, report, null);
            jdbcConn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // here we have closed the connection and need to wait - until hsqldb flushed the data
        // Thread.sleep(1000);
    }
}

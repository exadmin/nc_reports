package org.qubership.reporter;

import org.qubership.reporter.renderers.html.HtmlRenderer;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.renderers.json.JsonRenderer;

import java.io.File;

public class MainApp {
    public static void main(String[] args) throws Exception{
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

        // report.saveReposToFileForDebugAims("z:\\all_repos.csv");
    }
}

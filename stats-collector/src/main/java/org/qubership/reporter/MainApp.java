package org.qubership.reporter;

import org.qubership.reporter.html.HtmlRenderer;
import org.qubership.reporter.markdown.MarkdownRenderer;
import org.qubership.reporter.model.ReportModel;

import java.io.File;

public class MainApp {
    public static void main(String[] args) throws Exception{
        ReposAnalyzerApp analyzer = new ReposAnalyzerApp();
        ReportModel report = analyzer.analyzeAllIn(args[0]);

        /*MarkdownRenderer renderer = new MarkdownRenderer();
        String outputFileName = args[0] + File.separator + "combined-report.md";
        renderer.createMarkdawnFile(report, outputFileName);*/

        HtmlRenderer htmlRenderer = new HtmlRenderer();
        String outputFileName = args[0] + File.separator + "combined-report.html";
        htmlRenderer.createHtmlFile(report, outputFileName);

    }
}

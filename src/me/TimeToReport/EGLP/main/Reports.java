package me.TimeToReport.EGLP.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

public class Reports {

	private static List<Report> reports = new ArrayList<>();
	
	public static void addReport(OfflinePlayer reporter, OfflinePlayer reported, ReportReason reason) {
		reports.add(new Report(reporter, reported, reason));
		Config.reports.set("reports", reports);
		Config.reports.saveConfigSafely();
		GUIs.REPORT_LIST_GUI.refreshAllInstances();
	}
	
	public static void removeReport(OfflinePlayer reporter, OfflinePlayer reported) {
		reports.removeIf(r -> r.getReporter().equals(reporter.getUniqueId()) && r.getReported().equals(reported.getUniqueId()));
		Config.reports.set("reports", reports);
		Config.reports.saveConfigSafely();
		GUIs.REPORT_LIST_GUI.refreshAllInstances();
	}
	
	public static void loadReports() {
		reports = Config.reports.getMappableList("reports", Report.class, new ArrayList<>(), false);
	}
	
	public static List<Report> getReports() {
		return reports;
	}
	
}

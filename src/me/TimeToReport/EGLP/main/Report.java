package me.TimeToReport.EGLP.main;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Report {

	private UUID reporter, reported;
	private ReportReason reason;
	private String status;
	
	public Report(UUID reporter, UUID reported, ReportReason reason) {
		this.reporter = reporter;
		this.reported = reported;
		this.reason = reason;
		this.status = Config.statuses.get(0);
	}
	
	public Report(OfflinePlayer reporter, OfflinePlayer reported, ReportReason reason) {
		this(reporter.getUniqueId(), reported.getUniqueId(), reason);
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public UUID getReporter() {
		return reporter;
	}
	
	public UUID getReported() {
		return reported;
	}
	
	public OfflinePlayer getReportingPlayer() {
		return Bukkit.getOfflinePlayer(reporter);
	}
	
	public OfflinePlayer getReportedPlayer() {
		return Bukkit.getOfflinePlayer(reported);
	}
	
	public String getStatus() {
		return status;
	}
	
	public ReportReason getReason() {
		return reason;
	}
	
}

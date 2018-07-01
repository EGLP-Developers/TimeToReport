package me.TimeToReport.EGLP.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.config.LocaleProvider;
import me.mrletsplay.mrcore.config.LocaleProvider.CustomLocaleProvider;
import me.mrletsplay.mrcore.config.LocaleProvider.Locale;

public class Messages {
	
	private static LocaleProvider localeProvider;
	private static Locale locale;
	
	public static void init() {
		localeProvider = new LocaleProvider(new File(Main.pl.getDataFolder(), "lang"));
		localeProvider.setCustomLocaleProvider(new CustomLocaleProvider(new File(Main.pl.getDataFolder(), "lang")));
	
		CustomConfig en = new CustomConfig((File) null);
		
		en.addDefault("console-error", "{prefix}§cOnly players can use this command");
		en.addDefault("successfully-reported", "{prefix}§aSuccessfully reported §b{reported} §afor §b{reason}");
		en.addDefault("no-reports", "{prefix}§cNo reports that are exists");
		en.addDefault("kick-msg", "§7Kicked by an admin because you have been reported for {reason}");
		en.addDefault("report-received", "§a§lNew report received");
		en.addDefault("already-reported", "{prefix}§cYou already have reported this player");
		en.addDefault("receive-reports", "{prefix}§aNow you received new reports");
		en.addDefault("not-receive-reports", "{prefix}§cNow you not received new reports");
		en.addDefault("cannot-selfreport", "{prefix}§cYou cannot report yourself");
		en.addDefault("perm-error", "{prefix}§cSorry but you dont have the permission to execute this command");
		en.addDefault("player-not-found", "{prefix}§cPlayer cannot be found");
		
		en.addDefault("gui.reports.title", "§7§lReports");
		en.addDefault("gui.reports.item.dispenser.title", "§7§lInformation");
		en.addDefault("gui.reports.item.dispenser.lore", "§7Listed reports: §b{reports}");
		en.addDefault("gui.reports.item.skull.title", "§6{reported}§b's report");
		en.addDefault("gui.reports.item.skull.lore.reported-by", "§7Reported by §b{reporter}");
		en.addDefault("gui.reports.item.skull.lore.reason", "§7Reason: §b{reason}");
		en.addDefault("gui.reports.item.skull.lore.status", "§7Status: §b{status}");
		
		en.addDefault("gui.admin.title", "§7§lAdmin GUI");
		en.addDefault("gui.admin.item.dispenser.title", "§7§lReport information");
		en.addDefault("gui.admin.item.dispenser.lore.reporter", "§7Reporter: §b{reporter}");
		en.addDefault("gui.admin.item.dispenser.lore.reported", "§7Reported: §b{reported}");
		en.addDefault("gui.admin.item.dispenser.lore.reason", "§7Reason: §b{reason}");
		en.addDefault("gui.admin.item.dispenser.lore.status", "§7Status: {status}");
		en.addDefault("gui.admin.item.reporter.title", "§b§l{reporter}");
		en.addDefault("gui.admin.item.reported.title", "§b§l{reported}");
		en.addDefault("gui.admin.item.kill.title", "§c§lKill");
		en.addDefault("gui.admin.item.kick.title", "§c§lKick");
		en.addDefault("gui.admin.item.teleport.title", "§b§lTeleport");
		en.addDefault("gui.admin.item.status.title", "§7§lStatus: {status}");
		en.addDefault("gui.admin.item.status.lore", "§7Click to change the status");
		en.addDefault("gui.admin.item.close-report.title", "§c§lClose report");
		
		en.addDefault("gui.report-reasons.title", "§7§lSelect a report reason");
		en.addDefault("gui.report-reasons.item.skull", "§7§l{reported}");
		en.addDefault("gui.report-reasons.item.reasons", "§b§l{reason}");
		
		en.addDefault("gui.player-online", "§aPlayer is online");
		en.addDefault("gui.player-offline", "§cPlayer is offline");
		
		en.addDefault("gui.next-page", "§7Next page");
		en.addDefault("gui.previous-page", "§7Previous page");
		en.addDefault("gui.back", "§7§lBack");
		
		en.applyDefaults(false, true);
		
		CustomConfig de = new CustomConfig((File) null);
		
		de.loadDefaultFromClassPathSafely("de.yml", false, Main.class);
		
		localeProvider.registerLocale("de", de);
		localeProvider.registerLocale("en", en);
		
		localeProvider.setDefaultLocale("en");
		locale = localeProvider.getLocale(Config.cfg.getString("locale", "en", true));
	}
	
	public static String getString(String path, String... params) {
		List<String> pars = new ArrayList<>(Arrays.asList(params));
		pars.addAll(Arrays.asList("prefix", Main.getPrefix()));
		return locale.getMessage(path, pars.toArray(new String[pars.size()]));
	}
	
	public static String getGUIString(String path, String... params) {
		return locale.getMessage("gui." + path, params);
	}

}

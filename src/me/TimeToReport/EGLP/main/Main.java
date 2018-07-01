package me.TimeToReport.EGLP.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	public static Main pl;
	
	@Override
	public void onEnable() {
		pl = this;
		MrCoreBukkitImpl.loadMrCore(this);
		Config.init();
		Messages.init();
		getCommand("report").setExecutor(new ReportCommand());
		getCommand("reports").setExecutor(new ReportCommand());
		getCommand("togglereports").setExecutor(new ReportCommand());
		Bukkit.getPluginManager().registerEvents(this, this);
		Reports.loadReports();
		getLogger().info("TimeToReport was enabled successfully");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("TimeToReport was disabled");
	}
	
	public static File getBaseFolder() {
		return pl.getDataFolder();
	}
	
	public static String getPrefix() {
		return Config.cfg.getString("prefix");
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.pl, () -> {
			GUIs.ADMIN_GUI.refreshAllInstances();
		}, 1);
		
		if(e.getPlayer().isOp()) {
			UpdateChecker.checkForUpdate(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.pl, () -> {
			GUIs.ADMIN_GUI.refreshAllInstances();
		}, 1);
	}
	
}

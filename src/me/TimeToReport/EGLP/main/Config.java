package me.TimeToReport.EGLP.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.BukkitCustomConfig;
import me.mrletsplay.mrcore.bukkitimpl.ItemUtils;
import me.mrletsplay.mrcore.config.CompactCustomConfig;
import me.mrletsplay.mrcore.config.ConfigExpansions.ExpandableCustomConfig.ObjectMapper;
import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.CustomConfig.ConfigSaveProperty;

public class Config {
	
	public static BukkitCustomConfig cfg, reports;
	public static CompactCustomConfig data;
	public static List<ReportReason> reportReasons;
	public static List<String> statuses;
	
	public static void init() {
		reportReasons = new ArrayList<>();
		
		cfg = ConfigLoader.loadBukkitConfig(new File(Main.getBaseFolder(), "config.yml"), ConfigSaveProperty.KEEP_CONFIG_SORTING);
		try {
			BukkitCustomConfig defaults = new BukkitCustomConfig((File) null).loadConfig(Main.pl.getResource("config.yml"));
			cfg.loadDefault(defaults, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cfg.registerMapper(new ObjectMapper<ReportReason>(ReportReason.class) {

			@Override
			public Map<String, Object> mapObject(ReportReason object) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", object.getName());
				map.put("item", object.getIcon());
				return map;
			}

			@Override
			public ReportReason constructObject(Map<String, Object> map) {
				String name = (String) map.get("name");
				ItemStack item = castGeneric(map.get("item"), ItemStack.class);
				return new ReportReason(name, item);
			}
			
		});
		
		reportReasons = cfg.getMappableList("report-reasons", ReportReason.class);
		
		statuses = cfg.getStringList("report-statuses");
		
		reports = ConfigLoader.loadBukkitConfig(new File(Main.getBaseFolder(), "reports.yml"));
		
		reports.registerMapper(new ObjectMapper<Report>(Report.class) {

			@Override
			public Map<String, Object> mapObject(Report object) {
				Map<String, Object> map = new HashMap<>();
				map.put("reporter", object.getReporter().toString());
				map.put("reported", object.getReported().toString());
				map.put("reason", object.getReason().getName());
				return map;
			}

			@Override
			public Report constructObject(Map<String, Object> map) {
				if(!requireKeys(map, "reporter", "reported", "reason")) return null;
				UUID reporter = UUID.fromString((String) map.get("reporter"));
				UUID reported = UUID.fromString((String) map.get("reported"));
				ReportReason reason = reportReasons.stream().filter(r -> r.getName().equals(map.get("reason")))
						.findFirst()
						.orElse(new ReportReason(map.get("reason") + " §c(Deleted)", ItemUtils.createItem(Material.STONE, 1, 0, null)));
				return new Report(reporter, reported, reason);
			}
		});
		
		cfg.saveConfigSafely();
		
		data = new CompactCustomConfig(new File(Main.getBaseFolder(), "data.yml"));
		data.loadConfigSafely();
	}
	
	public static void loadDefaultReasons() {
		
	}

}

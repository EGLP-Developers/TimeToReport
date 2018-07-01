package me.TimeToReport.EGLP.main;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUI;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIBuildEvent;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIBuilder;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIBuilderMultiPage;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIElement;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIHolderPropertyMap;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIMultiPage;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.ItemSupplier;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.StaticGUIElement;
import me.mrletsplay.mrcore.bukkitimpl.ItemUtils;

public class GUIs {
	
	public static final GUIMultiPage<ReportReason> REPORT_REASON_GUI;
	public static final GUIMultiPage<Report> REPORT_LIST_GUI;
	public static final GUI ADMIN_GUI;
	
	static {
		REPORT_REASON_GUI = buildReportReasonGUI();
		ADMIN_GUI = buildAdminGUI();
		REPORT_LIST_GUI = buildReportsGUI();
	}
	
	public static Inventory getReportReasonGUI(OfflinePlayer reporter, OfflinePlayer reported) {
		GUIHolderPropertyMap properties = new GUIHolderPropertyMap();
		properties.put(Main.pl, "player-reported", reported.getUniqueId());
		properties.put(Main.pl, "player-reporter", reporter.getUniqueId());
		return REPORT_REASON_GUI.getForPlayer(null, properties);
	}
	
	public static Inventory getAdminGUI(Report report) {
		GUIHolderPropertyMap properties = new GUIHolderPropertyMap();
		properties.put(Main.pl, "report", report);
		return ADMIN_GUI.getForPlayer(null, properties);
	}
	
	public static Inventory getReportsGUI() {
		return REPORT_LIST_GUI.getForPlayer(null);
	}
	
	private static GUIMultiPage<Report> buildReportsGUI(){
		GUIBuilderMultiPage<Report> builder = new GUIBuilderMultiPage<>(Messages.getGUIString("reports.title"), 6);
		String base = "reports.item.";
		
		GUIElement gP = new StaticGUIElement(ItemUtils.createItem(Material.STAINED_GLASS_PANE, 1, 15, "§0"));
		for(int i = 0; i < 6*9; i++) {
			builder.addElement(i, gP);
		}
		
		for(int y = 1; y < 5; y++) {
			for(int x = 3; x < 8; x++) {
				int slot = y * 9 + x;
				builder.addElement(slot, (GUIElement) null);
				builder.addPageSlots(slot);
			}
		}
		
		builder.addElement(10, new GUIElement() {
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				return ItemUtils.createItem(Material.DISPENSER, 1, 0, Messages.getGUIString(base + "dispenser.title"), 
						  Messages.getGUIString(base + "dispenser.lore", "reports", ""+Reports.getReports().size()));
			}
		});
		
		builder.addPreviousPageItem(48, ItemUtils.createItem(ItemUtils.arrowLeft(DyeColor.WHITE), previousPage()));
		builder.addNextPageItem(52, ItemUtils.createItem(ItemUtils.arrowRight(DyeColor.WHITE), nextPage()));
		
		builder.setSupplier(new ItemSupplier<Report>() {
			
			@Override
			public GUIElement toGUIElement(GUIBuildEvent event, Report item) {
				return new GUIElement() {
					
					@SuppressWarnings("deprecation")
					@Override
					public ItemStack getItem(GUIBuildEvent event) {
						OfflinePlayer reporter = Bukkit.getOfflinePlayer(item.getReporter());
						OfflinePlayer reported = Bukkit.getOfflinePlayer(item.getReported());
						
						ItemStack skull = ItemUtils.createItem(Material.SKULL_ITEM, 1, 3, Messages.getGUIString(base + "skull.title", "repored", reported.getName()), 
								Messages.getGUIString(base + "skull.lore.reported-by", "reporter", reporter.getName()),
								Messages.getGUIString(base + "skull.lore.reason", "reason", item.getReason().getName()),
								Messages.getGUIString(base + "skull.lore.status", "reason", item.getStatus()));
						SkullMeta sM = (SkullMeta) skull.getItemMeta();
						sM.setOwner(reported.getName());
						skull.setItemMeta(sM);
						
						return skull;
					}
				}.setAction(e -> {
					e.getPlayer().openInventory(getAdminGUI(item));
				});
			}
			
			@Override
			public List<Report> getItems(GUIBuildEvent event) {
				return Reports.getReports();
			}
		});
		
		return builder.build();
	}
	
	private static GUI buildAdminGUI() {
		GUIBuilder builder = new GUIBuilder(Messages.getGUIString("admin.title") , 6);
		String base = "admin.item.";
		
		GUIElement gP = new StaticGUIElement(ItemUtils.createItem(Material.STAINED_GLASS_PANE, 1, 15, "§0"));
		for(int i = 0; i < 6*9; i++) {
			builder.addElement(i, gP);
		}
		
		builder.addElement(10, new GUIElement() {
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				Report report = (Report) event.getHolder().getProperty(Main.pl, "report");
				
				return ItemUtils.createItem(Material.DISPENSER, 1, 0, Messages.getGUIString(base + "dispenser.title"), 
						  Messages.getGUIString(base + "dispenser.lore.reporter", "reporter", report.getReportingPlayer().getName()),
						  Messages.getGUIString(base + "dispenser.lore.reported", "reported", report.getReportedPlayer().getName()),
						  Messages.getGUIString(base + "dispenser.lore.reason", "reason", report.getReason().getName()),
						  Messages.getGUIString(base + "dispenser.lore.status", "status", report.getStatus()));
			}
		});
		
		builder.addElement(12, new GUIElement() {
			
			@SuppressWarnings("deprecation")
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				Report report = (Report) event.getHolder().getProperty(Main.pl, "report");
				OfflinePlayer reporter = report.getReportingPlayer();
				ItemStack skull = ItemUtils.createItem(Material.SKULL_ITEM, 1, 3, Messages.getGUIString(base + "reporter.title", "reporter", reporter.getName()), (reporter.isOnline()?playerIsOnline():playerIsOffline()));
				SkullMeta sM = (SkullMeta) skull.getItemMeta();
				sM.setOwner(reporter.getName());
				skull.setItemMeta(sM);
				return skull;
			}
		}.setAction(event -> {
			Report report = (Report) event.getGUIHolder().getProperty(Main.pl, "report");
			event.getPlayer().openInventory(getAdminGUI(report));
		}));
		
		builder.addElement(13, new GUIElement() {
			
			@SuppressWarnings("deprecation")
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				Report report = (Report) event.getHolder().getProperty(Main.pl, "report");
				OfflinePlayer reported = report.getReportedPlayer();
				ItemStack skull = ItemUtils.createItem(Material.SKULL_ITEM, 1, 3, Messages.getGUIString(base + "reported.title", "reported", reported.getName()), (reported.isOnline()?playerIsOnline():playerIsOffline()));
				SkullMeta sM = (SkullMeta) skull.getItemMeta();
				sM.setOwner(reported.getName());
				skull.setItemMeta(sM);
				return skull;
			}
		});
		
		builder.addElement(21, new GUIElement() {
			
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				Report report = (Report) event.getHolder().getProperty(Main.pl, "report");
				return ItemUtils.createItem(Material.DIAMOND_SWORD, 1, 0, Messages.getGUIString(base + "kill.title"), (report.getReportedPlayer().isOnline()?"":playerIsOffline()));
			}
		}.setAction(e -> {
			Report report = (Report) e.getGUIHolder().getProperty(Main.pl, "report");
			OfflinePlayer reported = report.getReportedPlayer();
			if(reported.isOnline()) {
				reported.getPlayer().setHealth(0);
			}
		}));
		
		builder.addElement(22, new GUIElement() {
			
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				Report report = (Report) event.getHolder().getProperty(Main.pl, "report");
				return ItemUtils.createItem(Material.BOW, 1, 0, Messages.getGUIString(base + "kick.title"), (report.getReportedPlayer().isOnline()?"":playerIsOffline()));
			}
		}.setAction(e -> {
			Report report = (Report) e.getGUIHolder().getProperty(Main.pl, "report");
			OfflinePlayer reported = report.getReportedPlayer();
			
			if(reported.isOnline()) {
				reported.getPlayer().kickPlayer(Messages.getString("kick-msg", "reason", report.getReason().getName()));
			}
		}));
		
		builder.addElement(23, new GUIElement() {
			
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				Report report = (Report) event.getHolder().getProperty(Main.pl, "report");
				return ItemUtils.createItem(Material.BLAZE_ROD, 1, 0, Messages.getGUIString(base + "teleport.title"), (report.getReportedPlayer().isOnline()?"":playerIsOffline()));
			}
		}.setAction(e -> {
			Report report = (Report) e.getGUIHolder().getProperty(Main.pl, "report");
			OfflinePlayer reported = report.getReportedPlayer();
			if(reported.isOnline()) {
				e.getPlayer().teleport(reported.getPlayer().getLocation());
			}
		}));
		
		builder.addElement(37, new StaticGUIElement(ItemUtils.createItem(Material.ENDER_PEARL, 1, 0, back())).setAction(e -> {
			e.getPlayer().openInventory(getReportsGUI());
		}));
		
		builder.addElement(39, new GUIElement() {
			
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				Report report = (Report) event.getHolder().getProperty(Main.pl, "report");
				return ItemUtils.createItem(Material.BEACON, 1, 0, Messages.getGUIString(base + "status.title", "status", report.getStatus()), Messages.getGUIString(base + "status.lore"));
			}
		}.setAction(e -> {
			Report report = (Report) e.getGUIHolder().getProperty(Main.pl, "report");
			int ind = Config.statuses.indexOf(report.getStatus());
			report.setStatus(Config.statuses.get(ind + 1 >= Config.statuses.size() ? 1 : ind + 1));
			e.getGUI().refreshInstance(e.getPlayer());
		}));
		
		builder.addElement(40, new GUIElement() {
			
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				return ItemUtils.createItem(Material.CAKE, 1, 0, Messages.getGUIString(base + "close-report.title"));
			}
		}.setAction(e -> {
			Report report = (Report) e.getGUIHolder().getProperty(Main.pl, "report");
			OfflinePlayer reported = report.getReportedPlayer();
			OfflinePlayer reporter = report.getReportingPlayer();
			Reports.removeReport(reporter, reported);
			e.getPlayer().closeInventory();
		}));
		
		return builder.build();
	}
	
	private static GUIMultiPage<ReportReason> buildReportReasonGUI() {
		GUIBuilderMultiPage<ReportReason> builder = new GUIBuilderMultiPage<>(Messages.getGUIString("report-reasons.title"), 6);
		String base = "report-reasons.item.";
		
		GUIElement gP = new StaticGUIElement(ItemUtils.createItem(Material.STAINED_GLASS_PANE, 1, 15, "§0"));
		for(int i = 0; i < 6*9; i++) {
			builder.addElement(i, gP);
		}
		
		for(int y = 1; y < 5; y++) {
			for(int x = 3; x < 8; x++) {
				int slot = y * 9 + x;
				builder.addElement(slot, (GUIElement) null);
				builder.addPageSlots(slot);
			}
		}
		
		GUIElement dis = new GUIElement() {
			
			@SuppressWarnings("deprecation")
			@Override
			public ItemStack getItem(GUIBuildEvent event) {
				UUID player = (UUID) event.getHolder().getProperty(Main.pl, "player-reported");
				OfflinePlayer pl = Bukkit.getOfflinePlayer(player);
				ItemStack skull = ItemUtils.createItem(Material.SKULL_ITEM, 1, 3, Messages.getGUIString(base + "skull.title", "reported", pl.getName()));
				SkullMeta sM = (SkullMeta) skull.getItemMeta();
				sM.setOwner(pl.getName());
				skull.setItemMeta(sM);
				return skull;
			}
		};
		builder.addElement(10, dis);
		
		builder.setSupplier(new ItemSupplier<ReportReason>() {
			
			@Override
			public GUIElement toGUIElement(GUIBuildEvent event, ReportReason reason) {
				return new StaticGUIElement(ItemUtils.createItem(reason.getIcon(), Messages.getGUIString(base + "reasons", "reason", reason.getName()))).setAction(e -> {
					UUID uReporter = (UUID) e.getGUIHolder().getProperty(Main.pl, "player-reporter");
					OfflinePlayer reporter = Bukkit.getOfflinePlayer(uReporter);
					UUID uReported = (UUID) e.getGUIHolder().getProperty(Main.pl, "player-reported");
					OfflinePlayer reported = Bukkit.getOfflinePlayer(uReported);
					
					Reports.addReport(reporter, reported, reason);
					e.getPlayer().closeInventory();;
					
					for(Player permPlayer : Bukkit.getOnlinePlayers()) {
						if(!permPlayer.hasPermission("ttr.admin")) continue;
						if(Config.data.getStringList("togglereports").contains(permPlayer.getName())) continue;
						permPlayer.sendMessage(Messages.getString("report-received"));
					}
					
					e.getPlayer().sendMessage(Messages.getString("successfully-reported", "reported", reported.getName(), "reason", reason.getName()));
				});
			}
			
			@Override
			public List<ReportReason> getItems(GUIBuildEvent event) {
				return Config.reportReasons;
			}
		});
		
		return builder.build();
	}
	
	private static String nextPage() {
		return Messages.getGUIString("gui.next-page");
	}
	
	private static String previousPage() {
		return Messages.getGUIString("gui.previous-page");
	}
	
	private static String back() {
		return Messages.getGUIString("gui.back");
	}
	
	private static String playerIsOnline() {
		return Messages.getGUIString("gui.player-online");
	}
	
	private static String playerIsOffline() {
		return Messages.getGUIString("gui.player-offline");
	}

}

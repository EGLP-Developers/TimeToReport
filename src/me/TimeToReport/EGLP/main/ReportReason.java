package me.TimeToReport.EGLP.main;

import org.bukkit.inventory.ItemStack;

public class ReportReason {

	private String name;
	private ItemStack icon;
	
	public ReportReason(String name, ItemStack icon) {
		this.name = name;
		this.icon = icon;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
}

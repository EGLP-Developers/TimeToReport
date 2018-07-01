package me.TimeToReport.EGLP.main;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))	{
			sender.sendMessage(Messages.getString("console-error"));
			return true;
		}
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("report")) {
			if(args.length != 1) return sendCommandHelp(p);
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
			if(!target.hasPlayedBefore()) {
				p.sendMessage(Messages.getString("player-not-found"));
				return true;
			}
			if(target.getUniqueId().equals(p.getUniqueId())) {
				p.sendMessage(Messages.getString("cannot-selfreport"));
				return true;
			}
			if(Reports.getReports().stream().anyMatch(r -> r.getReported().equals(target.getUniqueId()) && r.getReporter().equals(p.getUniqueId()))) {
				p.sendMessage(Messages.getString("already-reported"));
				return true;
			}
			p.openInventory(GUIs.getReportReasonGUI(p, target));
		}
		
		if(cmd.getName().equalsIgnoreCase("reports")) {
			if(!p.hasPermission("ttr.reports") && !p.hasPermission("ttr.admin")) {
				p.sendMessage(Messages.getString("perm-error"));
				return true;
			}
			if(args.length != 0) return sendCommandHelp(p);
			if(Reports.getReports().isEmpty()) {
				p.sendMessage(Messages.getString("no-reports"));
				return true;
			}
			p.openInventory(GUIs.getReportsGUI());
		}
		
		if(cmd.getName().equalsIgnoreCase("togglereports")) {
			if(!p.hasPermission("ttr.togglereports") && !p.hasPermission("ttr.admin")) {
				p.sendMessage(Messages.getString("perm-error"));
				return true;
			}
			if(args.length != 0) return sendCommandHelp(p);
			List<String> tr = Config.data.getStringList("togglereports");
			if(tr.contains(p.getName())) {
				tr.remove(p.getName());
				p.sendMessage(Messages.getString("receive-reports"));
			}else {
				tr.add(p.getName());
				p.sendMessage(Messages.getString("not-receive-reports"));
			}
			Config.data.set("togglereports", tr);
			Config.data.saveConfigSafely();
		}
		return false;
	}
	
	private boolean sendCommandHelp(CommandSender sender) {
		sender.sendMessage("§7Command help:");
		sender.sendMessage("§7/report <Player>");
		if(sender.hasPermission("ttr.reports") || sender.hasPermission("ttr.admin")) {
			sender.sendMessage("§7/reports");
		}
		if(sender.hasPermission("ttr.togglereports") || sender.hasPermission("ttr.admin")) {
			sender.sendMessage("§7/togglereports");
		}
		return true;
	}

}

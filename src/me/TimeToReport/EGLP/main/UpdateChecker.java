package me.TimeToReport.EGLP.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.entity.Player;


public class UpdateChecker {

	public static void checkForUpdate(Player p){
		try {
			URL updUrl = new URL("http://134.255.225.192/api/plugin-data/TimeToReport/version.txt");
			BufferedReader r = new BufferedReader(new InputStreamReader(updUrl.openStream()));
			String ver = r.readLine();
			if(!ver.equalsIgnoreCase(Main.pl.getDescription().getVersion())){
				p.sendMessage("§aA new update for §cTimeToReport§a is available.");
				
				String zeile="";
			    String a = "https://";
			    String b = "http://";
			     
			   try{
				   while(!zeile.startsWith(a) || !zeile.startsWith(b)){
					   zeile = r.readLine();
				   }
				   p.sendMessage("\n§aUpdate Link: " + zeile);
			   }catch(NullPointerException e){
				   p.sendMessage(Main.getPrefix() + "§cUpdate Link not available.");
			   }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

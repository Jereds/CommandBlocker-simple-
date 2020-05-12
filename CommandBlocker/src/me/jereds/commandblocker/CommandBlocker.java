package me.jereds.commandblocker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class CommandBlocker extends JavaPlugin implements Listener, CommandExecutor {

	private List<String> disabledCommands = new ArrayList<>();
	@Override
	public void onEnable() {
		saveDefaultConfig();
		disabledCommands = getConfig().getStringList("blocked commands");
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("commandblockerreload").setExecutor(this);
	}
	
	@EventHandler
	public void onServerCommand(ServerCommandEvent event) {
		if(isDisabled(event.getCommand())) {
			event.setCancelled(true);
			event.getSender().sendMessage(ChatColor.DARK_RED + "Sorry! This command is disabled.");
		}
	}
	
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if(isDisabled(event.getMessage())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry! This command is disabled.");
		}
	}
	// return command to lowercase matches any of the commands (to lowercase) in the config
	private boolean isDisabled(String command) { 
		return disabledCommands.stream().anyMatch(cmd -> command.toLowerCase().startsWith(cmd.toLowerCase()));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {
		if(sender.hasPermission("commandblocker.reload")) {
			reloadConfig();
			disabledCommands = getConfig().getStringList("blocked commands");
			return true;
		}
		return false;
	}
}

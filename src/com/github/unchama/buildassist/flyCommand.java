/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.github.unchama.buildassist;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class flyCommand implements TabExecutor {
	BuildAssist plugin;

	public flyCommand(BuildAssist _plugin) {
		this.plugin = _plugin;
	}

	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	public boolean isInt(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.GREEN
					+ "FLY機能を利用したい場合は、末尾に「利用したい時間(分単位)」の数値を、");
			sender.sendMessage(ChatColor.GREEN
					+ "FLY機能を中断したい場合は、末尾に「stop」を記入してください。");
			return true;
		}
		if (args.length == 1) {
			Player player = (Player) sender;

			UUID uuid = player.getUniqueId();

			PlayerData playerdata = (PlayerData) BuildAssist.playermap
					.get(uuid);

			boolean flyflag = playerdata.flyflag;
			int flytime = playerdata.flytime;
			if (args[0].equalsIgnoreCase("stop")) {
				flyflag = false;
				flytime = 0;
				playerdata.flyflag = flyflag;
				playerdata.flytime = 0;
				player.setFlying(false);
			} else if (isInt(args[0])) {
				if (Integer.parseInt(args[0]) <= 0) {
					sender.sendMessage(ChatColor.GREEN
							+ "時間指定の数値は「1」以上の整数で行ってください。");
					return true;
				}
				flytime += Integer.parseInt(args[0]);
				flyflag = true;
				playerdata.flyflag = flyflag;
				playerdata.flytime = flytime;
				sender.sendMessage(ChatColor.YELLOW + "【FLYコマンド認証】効果の残り時間はあと"
						+ flytime + "分です。");
				player.setFlying(true);
			} else {
				sender.sendMessage(ChatColor.GREEN
						+ "FLY機能を利用したい場合は、末尾に「利用したい時間(分単位)」の数値を、");
				sender.sendMessage(ChatColor.GREEN
						+ "FLY機能を中断したい場合は、末尾に「stop」を記入してください。");
			}
			return true;
		}
		return false;
	}
}

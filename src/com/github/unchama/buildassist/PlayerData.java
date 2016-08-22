/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.github.unchama.buildassist;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerData {
	public String name;
	public UUID uuid;
	public int level;
	public boolean flyflag;
	public int flytime;

	public PlayerData(Player player) {
		this.name = Util.getName(player);
		this.uuid = player.getUniqueId();
	}

	public void levelupdata(Player player, int mines) {
		calcPlayerLevel(player, mines);
		setDisplayName(player);
	}

	private void calcPlayerLevel(Player player, int mines) {
		int i = this.level + 1;
		while ((((Integer) BuildAssist.levellist.get(i)).intValue() <= mines)
				&& (i <= 101)) {
			i++;
		}
		this.level = (i - 1);
	}

	public void setDisplayName(Player p) {
		String displayname = Util.getName(p);
		if (p.isOp()) {
			displayname = ChatColor.RED + "<管理人>" + this.name;
		}
		if (this.level == 101) {
			displayname = ChatColor.GOLD + "[ GOD ]" + displayname
					+ ChatColor.WHITE;
		} else {
			displayname = "[ Lv" + this.level + " ]" + displayname
					+ ChatColor.WHITE;
		}
		p.setDisplayName(displayname);
		p.setPlayerListName(displayname);
	}

	public boolean isOffline() {
		return BuildAssist.plugin.getServer().getPlayer(this.name) == null;
	}
}

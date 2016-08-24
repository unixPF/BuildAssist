/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.github.unchama.buildassist;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	HashMap<UUID, PlayerData> playermap = BuildAssist.playermap;

	public PlayerJoinListener() {
	}

	@EventHandler
	public void onplayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		UUID uuid = player.getUniqueId();

		PlayerData playerdata = null;
		if (!this.playermap.containsKey(uuid)) {
			playerdata = new PlayerData(player);

			this.playermap.put(player.getUniqueId(), playerdata);
		} else {
			playerdata = (PlayerData) this.playermap.get(uuid);
		}
		int builds = BuildBlock.calcBuildBlock(player);
		playerdata.levelupdata(player, builds);
	}
}

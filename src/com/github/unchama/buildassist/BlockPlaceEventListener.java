package com.github.unchama.buildassist;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class BlockPlaceEventListener implements Listener {
	BuildAssist plugin = BuildAssist.plugin;
	HashMap<UUID,PlayerData> playermap = BuildAssist.playermap;

	//ブロックを設置した時のイベント
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		
		//playerを取得
		Player player = event.getPlayer();
		//カウント対象ワールドかチェック
		if( com.github.unchama.buildassist.Util.isBlockCount(player) == false){
			return;
		}

		Block b = event.getBlock();
		//ブロックカウント対象の場合にカウントする
//		if( BuildAssist.materiallist.contains(b.getType()) == true ) {
			//プレイヤーのuuidを取得
			UUID uuid = player.getUniqueId();
			//プレイヤーデータ取得
			PlayerData playerdata = playermap.get(uuid);
			//プレイヤーデータが無い場合は処理終了
			if(playerdata == null){
				return;
			}

			playerdata.build_num_1min++;
//			player.sendMessage(""+b.getType() + ":"+b.getData());
//		}

	}

}
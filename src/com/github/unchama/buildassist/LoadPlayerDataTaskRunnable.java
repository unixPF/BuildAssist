package com.github.unchama.buildassist;


import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.unchama.seichiassist.SeichiAssist;


public class LoadPlayerDataTaskRunnable extends BukkitRunnable{

	private BuildAssist plugin = BuildAssist.plugin;
	private HashMap<UUID,PlayerData> playermap = BuildAssist.playermap;


	String name;
	Player p;
	final UUID uuid;
	int i;

	public LoadPlayerDataTaskRunnable(Player _p) {
		p = _p;
		name = Util.getName(p);
		uuid = p.getUniqueId();
		i = 0;
	}

	@Override
	public void run() {
		// 接続時にスケジュールで実行する処理
		
		//対象プレイヤーがオフラインなら処理終了
		if(BuildAssist.plugin.getServer().getPlayer(uuid) == null){
			cancel();
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + p.getName() + "はオフラインの為取得処理を中断");
			return;
		}
		//読み込み失敗が規定回数超えたら終わる
 		if(i >= 7){
			cancel();
 			plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + p.getName() + "の建築系データが読み込めませんでした");
 			p.sendMessage(ChatColor.RED + "建築系データが読み込めませんでした。再接続しても改善されない場合はお問い合わせフォームからお知らせ下さい。");
 			return;
 		}
 		//DBから読み込み終わるまで待つ
		com.github.unchama.seichiassist.data.PlayerData playerdata_s = SeichiAssist.playermap.get(uuid);
		if(playerdata_s == null){
 			plugin.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + p.getName() + "の建築系データ取得待機…(" + (i+1) + "回目)");
 			i++;
 			return;
		}
		cancel();
		PlayerData playerdata = null;
		if (!playermap.containsKey(uuid)) {
			//リストにplayerdataが無い場合は新規作成
			playerdata = new PlayerData(p);
			//PlayerDataをリストに登録
			playermap.put(uuid, playerdata);
// 			plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "new");
		} else {
			//リストにある場合はそれを読み込む
			playerdata = (PlayerData) playermap.get(uuid);
// 			plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "読み込み");
		}
		//建築系データ読み込み
		playerdata.buildload(p);
		
		plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "建築系データ読み込み完了");
		return;
	}

}

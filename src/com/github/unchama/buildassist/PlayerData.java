/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.github.unchama.buildassist;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.unchama.seichiassist.SeichiAssist;

public class PlayerData {
	public String name;
	public UUID uuid;
	public int level;
	//ランキング算出用トータル破壊ブロック
	public int totalbuildnum;

	public boolean flyflag;
	public int flytime;
	public boolean Endlessfly ;
	public boolean ZoneSetSkillFlag ;
	public boolean zsSkillDirtFlag;
	public int AREAint ;

	//ブロックを並べるスキル設定フラグ
	public int line_up_flg;
	public int line_up_step_flg;
	public int line_up_des_flg;
	public int line_up_minestack_flg;
	private BuildAssist plugin = BuildAssist.plugin;



	//プレイヤーデータクラスのコンストラクタ
		public PlayerData(Player player){
			//初期値を設定
			name = Util.getName(player);
			uuid = player.getUniqueId();
			totalbuildnum = 0;
			level = 1;
			flyflag = false;
			flytime = 0;
			Endlessfly = false;
			ZoneSetSkillFlag = false;
			zsSkillDirtFlag = true;
			AREAint = 2;

			line_up_flg = 0;
			line_up_step_flg = 0;
			line_up_des_flg = 0;
			line_up_minestack_flg = 0;

		}
		//レベルを更新
		public void levelupdata(Player player) {
			calcPlayerLevel(player);
//			setDisplayName(player);
		}

		//プレイヤーレベルを計算し、更新する。
		private void calcPlayerLevel(Player player){
			//現在のランクの次を取得
			int i = level;
			//ランクが上がらなくなるまで処理
			while(BuildAssist.levellist.get(i).intValue() <= totalbuildnum && (i+2) <= BuildAssist.levellist.size()){
				if(!BuildAssist.DEBUG){
					//レベルアップ時のメッセージ
					player.sendMessage(ChatColor.GOLD+"ﾑﾑｯﾚﾍﾞﾙｱｯﾌﾟ∩( ・ω・)∩【建築Lv(" + i +")→建築Lv(" + (i+1) + ")】");
				}
				i++;
				if( (i+1) == BuildAssist.levellist.size()){
					player.sendMessage(ChatColor.GOLD+"最大LVに到達したよ(`･ω･´)");
				}
			}
			level = i;
		}

//		//表示される名前に建築レベルを追加
//		public void setDisplayName(Player p) {
//			String displayname = Util.getName(p);
//			if(p.isOp()){
//				//管理人の場合
//				displayname = ChatColor.RED + "<管理人>" + name;
//			}
//				displayname =  "[ B-Lv" + level + " ]" + displayname + ChatColor.WHITE;
//
//			p.setDisplayName(displayname);
//			p.setPlayerListName(displayname);
//		}

		//オフラインかどうか
		public boolean isOffline() {
			return BuildAssist.plugin.getServer().getPlayer(uuid) == null;
		}

		//建築系データを読み込む　ture:読み込み成功　false:読み込み失敗
		public boolean buildload(Player player){
			com.github.unchama.seichiassist.data.PlayerData playerdata_s = SeichiAssist.playermap.get(uuid);
			if(playerdata_s == null){
				return false;
			}
			int server_num = SeichiAssist.config.getServerNum();

			totalbuildnum = playerdata_s.build_count_get();
			//ブロック設置カウントが統合されてない場合は統合する
			if(server_num >= 1 && server_num <= 3){
				byte f = playerdata_s.build_count_flg_get();
				if( (f & (0x01 << server_num))  == 0 ){
					if(f == 0) {
						// 初回は加算じゃなくベースとして代入にする
						totalbuildnum = BuildBlock.calcBuildBlock(player);
					} else {
						totalbuildnum += BuildBlock.calcBuildBlock(player);
					}
					f = (byte) (f | (0x01 << server_num));
					playerdata_s.build_count_flg_set(f);
					playerdata_s.build_count_set(totalbuildnum);
					player.sendMessage(ChatColor.GREEN+"サーバー" + server_num + "の建築データを統合しました");
					if(f == 0x0E){
						player.sendMessage(ChatColor.GREEN+"全サーバーの建築データを統合しました");
					}
				}
			}
			level = playerdata_s.build_lv_get();
			player.sendMessage(ChatColor.GREEN + "建築系データ読み込み");
			levelupdata(player);
			return true;
		}

		//建築系データを保存
		public void buildsave(Player player){
			com.github.unchama.seichiassist.data.PlayerData playerdata_s = SeichiAssist.playermap.get(uuid);
			if (playerdata_s == null){
				player.sendMessage(ChatColor.RED+"建築系データ保存失敗しました");
				return;
			}
			playerdata_s.build_count_set(totalbuildnum);
			playerdata_s.build_lv_set(level);
//			plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "建築系データ保存");
		}



	}

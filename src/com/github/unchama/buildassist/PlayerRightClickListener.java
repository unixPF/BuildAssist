package com.github.unchama.buildassist;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

public class PlayerRightClickListener implements Listener  {
	HashMap<UUID, PlayerData> playermap = BuildAssist.playermap;

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMenuUIEvent(PlayerInteractEvent event){
		//プレイヤーを取得
		Player player = event.getPlayer();
		//UUID取得
		UUID uuid = player.getUniqueId();
		//ワールドデータを取得
		World playerworld = player.getWorld();
		//プレイヤーが起こしたアクションを取得
		Action action = event.getAction();
		//アクションを起こした手を取得
		EquipmentSlot equipmentslot = event.getHand();
		//プレイヤーデータ
		PlayerData playerdata = BuildAssist.playermap.get(uuid);


		if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)){
			//左クリックの処理
			if(player.getInventory().getItemInMainHand().getType().equals(Material.STICK)){
				//メインハンドに棒を持っているときの処理

				//オフハンドのアクション実行時処理を終了
				if(equipmentslot.equals(EquipmentSlot.OFF_HAND)){
					return;
				}
				//開く音を再生
				player.playSound(player.getLocation(), Sound.BLOCK_FENCE_GATE_OPEN, 1, (float) 0.1);
				player.openInventory(MenuInventoryData.getMenuData(player));
			}else if(player.isSneaking()){

				//プレイヤーインベントリを取得
				PlayerInventory inventory = player.getInventory();
				//メインハンドとオフハンドを取得
				ItemStack mainhanditem = inventory.getItemInMainHand();
				ItemStack offhanditem = inventory.getItemInOffHand();

				//メインハンドにブロックがあるか
				boolean mainhandtoolflag = BuildAssist.materiallist.contains(mainhanditem.getType());
				//オフハンドにブロックがあるか
				boolean offhandtoolflag = BuildAssist.materiallist.contains(offhanditem.getType());

				//オフハンドのハンブロック検知処理
				boolean offhandslabflag = BuildAssist.material_slab.contains(offhanditem.getType());


				String SlabType = null ;


				//半ブロック種類別データ一次保管
				if(offhandslabflag){
					if(offhanditem.getType() == Material.WOOD_STEP){
						SlabType = "wood" ;
					}else if(offhanditem.getType() == Material.STEP){
						SlabType = "stone";
					}
				}

				//場合分け
				if(offhandtoolflag){
					//スキルフラグON以外のときは終了
					if(!playerdata.ZoneSetSkillFlag == true ){
						return;
					}
					//オフハンドの時

					//Location playerloc = player.getLocation();
					//Block block = player.getWorld().getBlockAt(playerloc.getBlockX(), playerloc.getBlockY() -1 , playerloc.getBlockZ());

					//プレイヤーの足の座標を取得
					int playerlocx = player.getLocation().getBlockX() ;
					int playerlocy = player.getLocation().getBlockY() ;
					int playerlocz = player.getLocation().getBlockZ() ;

					/*Coordinate start,end;
					Block placelocblock;

					start = new Coordinate(-3,-4,-3);
					end = new Coordinate(3,4,3);


					for(int x = start.x ; x < end.x ; x++){
						for(int z = start.z ; z < end.z ; z++){
							for(int y = start.y ; y < end.y; y++){
								placelocblock = block.getRelative(x, y, z);

							}
						}
					}
					*/

					//スキルの範囲設定用
					int AREAint = playerdata.AREAint ;
					int SEARCHint = AREAint + 1 ;
					int AREAintB = (AREAint * 2)+ 1 ;
					int SEARCHintB = (SEARCHint * 2)+ 1;


					//同ブロック探索(7*6*7)の開始座標を計算
					int searchX = playerlocx - SEARCHint ;
					int searchY = playerlocy - 4 ;
					int searchZ = playerlocz - SEARCHint ;

					MaterialData s = null ;
					MaterialData ws = null ;



					//同上(Y座標記録)
					int Y1 = 256 ;
					int Y2 = 256 ;

					//スキル発動条件を満たすか
					boolean SetReady = false ;

					//オフハンドアイテムと、範囲内のブロックに一致する物があるかどうか判別
					//同じ物がない場合・同じ物が3か所以上のY軸で存在する場合→SetReady = false
					for(;searchY < playerlocy + 2 ;){
						BlockState block = player.getWorld().getBlockAt(searchX,searchY,searchZ).getState();

						if(offhanditem.getType() == player.getWorld().getBlockAt(searchX,searchY,searchZ).getType()){

							if(offhandslabflag){
								if(SlabType == "stone"){
									s = block.getData();

								}else if(SlabType == "wood"){
									ws = block.getData();
								}
							}

							if(Y1 == searchY || Y1 == 256){
								Y1 = searchY ;
								SetReady = true ;
							}else if(Y2 == searchY || Y2 == 256){
								Y2 = searchY ;
							}else {
								SetReady = false ;
								player.sendMessage(ChatColor.RED + "範囲内に「オフハンドと同じブロック」が多すぎます。(Y軸2つ分以内にして下さい)");
								break;
							}
						}
						searchX ++ ;

						if(searchX > playerlocx + SEARCHint){
						searchX = searchX - SEARCHintB ;
						searchZ ++ ;
							if(searchZ > playerlocz + SEARCHint){
								searchZ = searchZ - SEARCHintB ;
								searchY ++ ;
							}

						}
					}

					if(Y1 == 256){
						player.sendMessage(ChatColor.RED + "範囲内に「オフハンドと同じブロック」を設置してください。(基準になります)");
						SetReady = false ;
					}

					//上の処理で「スキル条件を満たしていない」と判断された場合、処理終了
					if(SetReady == false){
						player.sendMessage(ChatColor.RED + "発動条件が満たされませんでした。");
					}

					if(SetReady == true){
				//実際に範囲内にブロックを設置する処理
						//設置範囲の基準となる座標
						int setblockX = playerlocx - AREAint ;
						int setblockY = Y1 ;
						int setblockZ = playerlocz - AREAint ;
						int setunder = 1 ;

						int searchedInv = 9 ;

						ItemStack ItemInInv = null ;
						int ItemInInvAmount = 0 ;

						Location WGloc = new Location(playerworld,0,0,0)  ;


						for(;setblockZ < playerlocz + SEARCHint ;){
							if(player.getWorld().getBlockAt(setblockX,setblockY,setblockZ).getType() == Material.AIR ){
								setunder = 1;
								if(player.getWorld().getBlockAt(setblockX,(setblockY - setunder),setblockZ).getType() == Material.AIR){
									for(;player.getWorld().getBlockAt(setblockX,(setblockY - setunder),setblockZ).getType() == Material.AIR;){
										//他人の保護がかかっている場合は処理を終了
										WGloc.add(setblockX ,(setblockY - setunder) , setblockZ);
										if(!Util.getWorldGuard().canBuild(player, WGloc)){
											setunder ++ ;
										}else {
											//設置対象座標の下も空気だった場合、土を設置する
											player.getWorld().getBlockAt(setblockX,(setblockY - setunder),setblockZ).setType(Material.DIRT);
											setunder ++ ;
										}

										if(setunder > 3){
											break;
										}
									}
								}

								//他人の保護がかかっている場合は処理を終了
								WGloc.add(setblockX ,setblockY , setblockZ);
								if(!Util.getWorldGuard().canBuild(player, WGloc)){
								}else {

								//インベントリの左上から一つずつ確認する。
								//※一度「該当アイテムなし」と判断したスロットは次回以降スキップする様に組んであるゾ
									for(; searchedInv < 36 ;){
										//該当スロットのアイテムデータ取得
										ItemInInv = player.getInventory().getItem(searchedInv) ;
										if(ItemInInv == null ){
										}else {
											ItemInInvAmount = ItemInInv.getAmount() ;
										}
										//スロットのアイテムが空白だった場合の処理(エラー回避のため)
										if(ItemInInv == null ){
											//確認したスロットが空気だった場合に次スロットへ移動
											if(searchedInv == 35){
												searchedInv = 0 ;
											}else if(searchedInv == 8 ){
												searchedInv = 36 ;
												player.sendMessage(ChatColor.RED + "アイテムが不足しています！" ) ;
											}else {
												searchedInv ++ ;
											}
											//スロットアイテムがオフハンドと一致した場合
										}else if(ItemInInv.getType() == offhanditem.getType() ){
											//数量以外のデータ(各種メタ)が一致するかどうか検知(仮)
											ItemStack ItemInInvCheck = ItemInInv ;
											ItemStack offhandCheck = offhanditem ;
											ItemInInvCheck.setAmount(1);
											offhandCheck.setAmount(1);

											if(!(ItemInInvCheck.equals(offhandCheck))){
												if(searchedInv == 35){
													searchedInv = 0 ;
												}else if(searchedInv == 8 ){
													searchedInv = 36 ;
													player.sendMessage(ChatColor.RED + "アイテムが不足しています!" ) ;
												}else {
													searchedInv ++ ;
												}
											}else {
												//取得したインベントリデータから数量を1ひき、インベントリに反映する
												if(ItemInInvAmount == 1) {
													ItemInInv.setType(Material.AIR);
													ItemInInv.setAmount(1);
												}else{
													ItemInInv.setAmount(ItemInInvAmount - 1) ;
												}
												player.getInventory().setItem(searchedInv, ItemInInv);
												//ブロックを設置する
												player.getWorld().getBlockAt(setblockX,setblockY,setblockZ).setType(offhanditem.getType());

												break;

												/*
												if(offhandslabflag){
													player.sendMessage(ChatColor.RED + "SLABですってよ奥さん" ) ;
													if(SlabType == "stone"){
														player.getWorld().getBlockAt(setblockX,setblockY,setblockZ).getState().setData(s);
														player.getWorld().getBlockAt(setblockX,setblockY,setblockZ).getState().update();
													}else if(SlabType == "wood"){
														player.getWorld().getBlockAt(setblockX,setblockY,setblockZ).getState().setData(ws);
														player.getWorld().getBlockAt(setblockX,setblockY,setblockZ).getState().update();
													}

												}
												*/
											}
										}else {
											//確認したスロットが違うアイテムだった場合に、次のスロットへと対象を移す
											if(searchedInv == 35){
												searchedInv = 0 ;
											}else if(searchedInv == 8 ){
												searchedInv = 36 ;
												player.sendMessage(ChatColor.RED + "アイテムが不足しています!" ) ;
											}else {
												searchedInv ++ ;
											}
										}
									}
								}
							}
							if(searchedInv == 36){
								break;
							}

							setblockX ++ ;

							if(setblockX > playerlocx + AREAint){
								setblockX = setblockX - AREAintB ;
								setblockZ ++ ;

							}
						}
					}
					player.sendMessage(ChatColor.RED + "敷き詰めスキル：処理終了" ) ;
					return;


				}else if(mainhandtoolflag){
					//メインハンドの時
					return;
				}else{
					//どちらにももっていない時処理を終了
					return;
				}

			}
		}
	}
}
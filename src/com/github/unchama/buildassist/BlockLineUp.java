package com.github.unchama.buildassist;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
//import org.bukkit.metadata.FixedMetadataValue;
//import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;

public class BlockLineUp implements Listener{
	
//    private JavaPlugin plugin;
    
//	public void BlockLineUp(JavaPlugin plugin) {
//		this.plugin = plugin;
//		plugin.getServer().getPluginManager().registerEvents(this, plugin);
//	}
	
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e){
		e.getPlayer().sendMessage(e.getAction().toString()); //クリックした物体のID表示
		
		
		//プレイヤーを取得
		Player player = e.getPlayer();
		//UUID取得
		UUID uuid = player.getUniqueId();
		//ワールドデータを取得
		World playerworld = player.getWorld();
		//プレイヤーが起こしたアクションを取得
		Action action = e.getAction();
		//アクションを起こした手を取得
		EquipmentSlot equipmentslot = e.getHand();
		//プレイヤーデータ
		PlayerData playerdata = BuildAssist.playermap.get(uuid);
		
		Location pl = player.getLocation();
		
		//左クリックの処理
		if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)){
			//プレイヤーインベントリを取得
			PlayerInventory inventory = player.getInventory();
			//メインハンドとオフハンドを取得
			ItemStack mainhanditem = inventory.getItemInMainHand();
			ItemStack offhanditem = inventory.getItemInOffHand();
			player.sendMessage(mainhanditem.getType().toString());
			player.sendMessage(mainhanditem.getData().toString());
			player.sendMessage(""+mainhanditem.getMaxStackSize());
			player.sendMessage(""+mainhanditem.getAmount());	//持ってる数
			Material m = mainhanditem.getType();
			byte d = mainhanditem.getData().getData();
			
//			pl.getBlock().setType(arg0);
			
			//メインハンドにブロックがあるとき
			if( BuildAssist.materiallist.contains(mainhanditem.getType()) == true ) {
				if(offhanditem.getType() != Material.STICK){//オフハンドに木の棒を持ってるときのみ発動する
					return;
				}
				//仰角は下向きがプラスで上向きがマイナス
				//方角は南を0度として時計回りに360度、何故か偶にマイナスの値になる
				float pitch = pl.getPitch();
				float yaw = (pl.getYaw() + 360) % 360;
				player.sendMessage("方角：" + Float.toString(yaw) + "　仰角：" + Float.toString(pitch));
				int step_x = 0;
				int step_y = 0;
				int step_z = 0;
				//プレイヤーの足の座標を取得
				int px = pl.getBlockX();
//				int py = pl.getBlockY()+1;
				int py = (int)(pl.getY() + 1.6);
				int pz = pl.getBlockZ();
				
				if (pitch > 45 ){//下
					step_y = -1;
//					py--;
					py = pl.getBlockY();
				}else if (pitch < -45 ){//上
					step_y = 1;
				}else{
					if (yaw > 315 || yaw < 45 ){//南
						step_z = 1;
					}else if(yaw < 135 ){//西
						step_x = -1;
					}else if(yaw < 225 ){//北
						step_z = -1;
					}else{//東
						step_x = 1;
					}				
				}
				int v = 0;
				for( v = 0 ; v<mainhanditem.getAmount();v++){
					px += step_x;
					py += step_y;
					pz += step_z;
					Block b = pl.getWorld().getBlockAt(px , py , pz );
					if (b.getType() != Material.AIR){	//空気以外にぶつかったら設置終わり
						break;
					}
					pl.getWorld().getBlockAt(px , py , pz ).setType(m);
//					pl.getWorld().getBlockAt(px , py , pz ).setType(Material.WOOL);//ブロックを指定座標に設置
					pl.getWorld().getBlockAt(px , py , pz ).setData(d);		//ブロックのデータを設定
//					pl.getWorld().getBlockAt(px , py , pz ).setData((byte)(v % 16));		//ブロックのデータを設定
//					pl.getWorld().getBlockAt(px , py , pz ).setMetadata("", new FixedMetadataValue(plugin,(v % 16)));		//ブロックのメタデータを設定
//					pl.getWorld().getBlockAt(px+1 , py+1 , pz+1 ).setTypeIdAndData( Material.WOOL.getId(), (byte)(v % 16) , true);		//ブロックのメタデータを設定

				}
				if (mainhanditem.getAmount() - v == 0 ){
//					mainhanditem.setType(Material.AIR);
//					mainhanditem.setAmount(-1);
					inventory.setItemInMainHand(new ItemStack(Material.AIR,-1));
				}else{
					mainhanditem.setAmount(mainhanditem.getAmount() - v );
				}
				player.playSound(player.getLocation(), Sound.BLOCK_STONE_PLACE, 1, 1);
				player.sendMessage(Integer.toString(player.getLocation().getBlockY()) +":" + Double.toString(pl.getY()));
				player.sendMessage("v:" + v );
				
			}
		}
	}
}

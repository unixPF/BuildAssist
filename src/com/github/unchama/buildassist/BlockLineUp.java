package com.github.unchama.buildassist;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BlockLineUp implements Listener{
	
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
		
		if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)){
			//左クリックの処理
			
			//仰角は下向きがプラスで上向きがマイナス
			//方角は南を0度として時計回りに360度、何故か偶にマイナスの値になる
			float pitch = pl.getPitch();
			float yaw = (pl.getYaw() + 360) % 360;
			player.sendMessage("方角：" + Float.toString(yaw) + "　仰角：" + Float.toString(pitch));
			int step_x = 0;
			int step_y = 0;
			int step_z = 0;
			//プレイヤーの足の座標を取得
			int px = player.getLocation().getBlockX();
//			int py = player.getLocation().getBlockY()+1;
			int py = (int)(player.getLocation().getY() + 1.6);

			int pz = player.getLocation().getBlockZ();
			
			if (pitch > 45 ){//下
				step_y = -1;
//				py--;
				py = player.getLocation().getBlockY();
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
			for(int v = 0 ; v<30;v++){
				px += step_x;
				py += step_y;
				pz += step_z;
				pl.getWorld().getBlockAt(px , py , pz ).setType(Material.SMOOTH_BRICK);
			}
			//プレイヤーインベントリを取得
			PlayerInventory inventory = player.getInventory();
			//メインハンドとオフハンドを取得
			ItemStack mainhanditem = inventory.getItemInMainHand();
			ItemStack offhanditem = inventory.getItemInOffHand();
//			player.sendMessage(mainhanditem.getType().toString());
			player.sendMessage(Integer.toString(player.getLocation().getBlockY()) +":" + Double.toString(pl.getY()));
			player.getLocation().getBlockY();
			
		}
		
		
	}

}

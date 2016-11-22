/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.github.unchama.buildassist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BuildAssist extends JavaPlugin {
	public static BuildAssist plugin;
	public static Boolean DEBUG = false;

	public static final String PLAYERDATA_TABLENAME = "playerdata";

	//起動するタスクリスト
	private List<BukkitTask> tasklist = new ArrayList<BukkitTask>();

	//総建築量ランキング表示用データリスト
	public static final List<Integer> ranklist = new ArrayList<Integer>();

	//Playerdataに依存するデータリスト
	public static final HashMap<UUID,PlayerData> playermap = new HashMap<UUID,PlayerData>();
	private HashMap<String, TabExecutor> commandlist;
	public static Config config;

	//lvの閾値
	public static final List<Integer> levellist = new ArrayList<Integer>(Arrays.asList(
			0,15,50,100,175,//5
			300,450,650,900,1200,//10
			1600,2100,2700,3400,4200,//15
			5100,6100,7500,9000,10500,//20
			12000,14000,16000,18000,20000,//25
			25000,30000,35000,40000,50000,100000//30
			));

	//設置ブロックの対象リスト
	public static final List<Material> materiallist = new ArrayList<Material>(Arrays.asList(
			Material.ACACIA_STAIRS,Material.ACACIA_FENCE,Material.ACACIA_FENCE_GATE,
			Material.BIRCH_WOOD_STAIRS,Material.BIRCH_FENCE,Material.BIRCH_FENCE_GATE,
			Material.BONE_BLOCK,Material.BOOKSHELF,
			Material.BRICK,Material.BRICK_STAIRS,
			Material.CACTUS,Material.CHEST,
			Material.CLAY_BRICK,
			Material.DARK_OAK_STAIRS,Material.DARK_OAK_FENCE,Material.DARK_OAK_FENCE_GATE,
			Material.END_BRICKS,
			Material.FURNACE,Material.GLOWSTONE,Material.HARD_CLAY,
			Material.JACK_O_LANTERN,Material.JUKEBOX,Material.JUNGLE_FENCE,Material.JUNGLE_FENCE_GATE,
			Material.JUNGLE_WOOD_STAIRS,Material.LADDER,Material.LEAVES,Material.LEAVES_2,
			Material.LOG,Material.LOG_2,Material.NETHER_BRICK,Material.NETHER_BRICK_STAIRS,
			Material.NETHER_WART_BLOCK,Material.RED_NETHER_BRICK,
			Material.OBSIDIAN,Material.PACKED_ICE,Material.PRISMARINE,
			Material.PUMPKIN,Material.PURPUR_BLOCK,Material.PURPUR_SLAB,
			Material.PURPUR_STAIRS,Material.PURPUR_PILLAR,
			Material.QUARTZ_BLOCK,Material.QUARTZ_STAIRS,Material.QUARTZ,
			Material.SANDSTONE,Material.SANDSTONE_STAIRS,Material.SEA_LANTERN,
			Material.SLIME_BLOCK,Material.SMOOTH_BRICK,Material.SMOOTH_STAIRS,
			Material.SNOW_BLOCK,Material.SPRUCE_FENCE,Material.SPRUCE_FENCE_GATE,
			Material.SPRUCE_WOOD_STAIRS,Material.FENCE,Material.FENCE_GATE,
			Material.STAINED_CLAY,Material.STAINED_GLASS,Material.STAINED_GLASS_PANE,
			Material.STEP,Material.STONE,Material.STONE_SLAB2,Material.THIN_GLASS,
			Material.TORCH,Material.WOOD,
			Material.WOOD_STAIRS,Material.WOOD_STEP,
			Material.WOOL,Material.CARPET,Material.WORKBENCH


			));

	//ハーフブロックまとめ
	public static final List<Material> material_slab = new ArrayList<Material>(Arrays.asList(
			Material.STONE_SLAB2,Material.PURPUR_SLAB,Material.WOOD_STEP,Material.STEP

			));


	@Override
	public void onEnable() {
		plugin = this;

		//コンフィグ系の設定は全てConfig.javaに移動
		config = new Config(this);
		config.loadConfig();


		//コマンドの登録
		commandlist = new HashMap<String, TabExecutor>();
		commandlist.put("fly", new flyCommand(plugin));

		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new EntityListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerRightClickListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerInventoryListener(), this);
		getServer().getPluginManager().registerEvents(new BlockLineUp(), this);		//クリックイベント登録

		for (Player p : getServer().getOnlinePlayers()) {
			UUID uuid = p.getUniqueId();

			PlayerData playerdata = new PlayerData(p);

			int builds = BuildBlock.calcBuildBlock(p);
			playerdata.levelupdata(p, builds);

			playermap.put(uuid, playerdata);
		}
		getLogger().info("BuildAssist is Enabled!");

		tasklist.add(new MinuteTaskRunnable().runTaskTimer(this, 0, 1200));
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return commandlist.get(cmd.getName()).onCommand(sender, cmd, label, args);
	}

	@Override
	public void onDisable() {
		for (BukkitTask task : this.tasklist) {
			task.cancel();
		}
	}
}

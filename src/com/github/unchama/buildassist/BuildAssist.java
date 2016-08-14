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
	public static final boolean DEBUG = true;
	private List<BukkitTask> tasklist = new ArrayList();
	public static final HashMap<UUID, PlayerData> playermap = new HashMap();
	private HashMap<String, TabExecutor> commandlist;
	public static final List<Integer> levellist = new ArrayList(
			Arrays.asList(new Integer[] { Integer.valueOf(0),
					Integer.valueOf(15), Integer.valueOf(49),
					Integer.valueOf(106), Integer.valueOf(198),
					Integer.valueOf(333), Integer.valueOf(705),
					Integer.valueOf(1265), Integer.valueOf(2105),
					Integer.valueOf(3347), Integer.valueOf(4589),
					Integer.valueOf(5831), Integer.valueOf(7073),
					Integer.valueOf(8315), Integer.valueOf(9557),
					Integer.valueOf(11047), Integer.valueOf(12835),
					Integer.valueOf(14980), Integer.valueOf(17554),
					Integer.valueOf(20642), Integer.valueOf(24347),
					Integer.valueOf(28793), Integer.valueOf(34128),
					Integer.valueOf(40530), Integer.valueOf(48212),
					Integer.valueOf(57430), Integer.valueOf(68491),
					Integer.valueOf(81764), Integer.valueOf(97691),
					Integer.valueOf(116803), Integer.valueOf(135915),
					Integer.valueOf(155027), Integer.valueOf(174139),
					Integer.valueOf(193251), Integer.valueOf(212363),
					Integer.valueOf(235297), Integer.valueOf(262817),
					Integer.valueOf(295841), Integer.valueOf(335469),
					Integer.valueOf(383022), Integer.valueOf(434379),
					Integer.valueOf(489844), Integer.valueOf(549746),
					Integer.valueOf(614440), Integer.valueOf(684309),
					Integer.valueOf(759767), Integer.valueOf(841261),
					Integer.valueOf(929274), Integer.valueOf(1024328),
					Integer.valueOf(1126986),

					Integer.valueOf(1237856), Integer.valueOf(1362856),
					Integer.valueOf(1487856), Integer.valueOf(1612856),
					Integer.valueOf(1737856), Integer.valueOf(1862856),
					Integer.valueOf(1987856), Integer.valueOf(2112856),
					Integer.valueOf(2237856), Integer.valueOf(2362856),
					Integer.valueOf(2537856), Integer.valueOf(2712856),
					Integer.valueOf(2887856), Integer.valueOf(3062856),
					Integer.valueOf(3237856), Integer.valueOf(3412856),
					Integer.valueOf(3587856), Integer.valueOf(3762856),
					Integer.valueOf(3937856), Integer.valueOf(4112856),
					Integer.valueOf(4362856), Integer.valueOf(4612856),
					Integer.valueOf(4862856), Integer.valueOf(5112856),
					Integer.valueOf(5362856), Integer.valueOf(5612856),
					Integer.valueOf(5862856), Integer.valueOf(6112856),
					Integer.valueOf(6362856), Integer.valueOf(6612856),
					Integer.valueOf(6942856), Integer.valueOf(7272856),
					Integer.valueOf(7602856), Integer.valueOf(7932856),
					Integer.valueOf(8262856), Integer.valueOf(8592856),
					Integer.valueOf(8922856), Integer.valueOf(9252856),
					Integer.valueOf(9582856), Integer.valueOf(9912856),
					Integer.valueOf(10332856), Integer.valueOf(10752856),
					Integer.valueOf(11172856), Integer.valueOf(11592856),
					Integer.valueOf(12012856), Integer.valueOf(12432856),
					Integer.valueOf(12852856), Integer.valueOf(13272856),
					Integer.valueOf(13692856), Integer.valueOf(14112856) }));
	public static final List<Material> materiallist = new ArrayList(
			Arrays.asList(new Material[] { Material.STONE, Material.NETHERRACK,
					Material.NETHER_BRICK, Material.DIRT, Material.GRAVEL,
					Material.LOG, Material.LOG_2, Material.GRASS,
					Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE,
					Material.DIAMOND_ORE, Material.LAPIS_ORE,
					Material.EMERALD_ORE, Material.REDSTONE_ORE, Material.SAND,
					Material.SANDSTONE, Material.QUARTZ_ORE,
					Material.END_BRICKS, Material.ENDER_STONE, Material.ICE,
					Material.PACKED_ICE, Material.OBSIDIAN, Material.MAGMA,
					Material.SOUL_SAND }));

	public BuildAssist() {
	}

	public void onEnable() {
		plugin = this;

		this.commandlist = new HashMap();
		this.commandlist.put("fly", new flyCommand(plugin));

		getServer().getPluginManager().registerEvents(new PlayerJoinListener(),
				this);
		getServer().getPluginManager().registerEvents(new EntityListener(),
				this);
		for (Player p : getServer().getOnlinePlayers()) {
			UUID uuid = p.getUniqueId();

			PlayerData playerdata = new PlayerData(p);

			int mines = BuildBlock.calcBuildBlock(p);
			playerdata.levelupdata(p, mines);

			playermap.put(uuid, playerdata);
		}
		getLogger().info("BuildAssist is Enabled!");

		this.tasklist
				.add(new MinuteTaskRunnable().runTaskTimer(this, 0L, 300L));
	}

	public void onDisable() {
		for (BukkitTask task : this.tasklist) {
			task.cancel();
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		return ((TabExecutor) this.commandlist.get(cmd.getName())).onCommand(
				sender, cmd, label, args);
	}
}

/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.github.unchama.buildassist;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class BuildBlock {
	public int after;
	public int before;
	public int increase;

	BuildBlock() {
		this.after = 0;
		this.before = 0;
		this.increase = 0;
	}

	public void setIncrease() {
		this.increase = (this.after - this.before);
	}

	public static int calcBuildBlock(Player player) {
		int sum = 0;
		for (Material m : BuildAssist.materiallist) {
			sum += player.getStatistic(Statistic.USE_ITEM, m);
		}
		return sum;
	}
}

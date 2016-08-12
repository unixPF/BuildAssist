package com.github.unchama.buildassist;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;


public class BuildBlock {
	public int after;
	public int before;
	public int increase;

	BuildBlock(){
		after = 0;
		before = 0;
		increase = 0;
	}
	public void setIncrease(){
		increase = after - before;
	}
	//統計の総ブロック破壊数を出力する。
	public static int calcBuildBlock(Player player){
		int sum = 0;
		for(Material m : BuildAssist.materiallist){
			sum += player.getStatistic(Statistic.USE_ITEM, m);
		}
		return  sum;
	}
}

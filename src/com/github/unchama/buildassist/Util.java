/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.github.unchama.buildassist;

import org.bukkit.entity.Player;

public class Util {
	public Util() {
	}

	public static String getName(Player p) {
		return p.getName().toLowerCase();
	}

	public static String getName(String name) {
		return name.toLowerCase();
	}
	/*
//現在の建築量順位を表示する
public static int calcPlayerRank(Player p){
	//ランク用関数
	int i = 0;
	int t = BuildBlock.calcBuildBlock(p);
	//ランクが上がらなくなるまで処理
	while(BuildAssist.ranklist.get(i).intValue() > t){
		i++;
	}
	return i+1;
}
*/
}
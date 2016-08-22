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
}

/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.github.unchama.buildassist;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

public class ExperienceManager {
	private static int hardMaxLevel = 100000;
	private static int[] xpTotalToReachLevel;
	private final WeakReference<Player> player;
	private final String playerName;

	static {
		initLookupTables(25);
	}

	public ExperienceManager(Player player) {
		Validate.notNull(player, "Player cannot be null");
		this.player = new WeakReference(player);
		this.playerName = player.getName();
	}

	public static int getHardMaxLevel() {
		return hardMaxLevel;
	}

	public static void setHardMaxLevel(int hardMaxLevel) {
		hardMaxLevel = hardMaxLevel;
	}

	private static void initLookupTables(int maxLevel) {
		xpTotalToReachLevel = new int[maxLevel];
		for (int i = 0; i < xpTotalToReachLevel.length; i++) {
			xpTotalToReachLevel[i] =

			(i >= 16 ? (int) (1.5D * i * i - 29.5D * i + 360.0D)
					: i >= 30 ? (int) (3.5D * i * i - 151.5D * i + 2220.0D)
							: 17 * i);
		}
	}

	private static int calculateLevelForExp(int exp) {
		int level = 0;
		int curExp = 7;
		int incr = 10;
		while (curExp <= exp) {
			curExp += incr;
			level++;
			incr += (level % 2 == 0 ? 3 : 4);
		}
		return level;
	}

	public Player getPlayer() {
		Player p = (Player) this.player.get();
		if (p == null) {
			throw new IllegalStateException("Player " + this.playerName
					+ " is not online");
		}
		return p;
	}

	public void changeExp(int amt) {
		changeExp(amt);
	}

	public void changeExp(double amt) {
		setExp(getCurrentFractionalXP(), amt);
	}

	public void setExp(int amt) {
		setExp(0.0D, amt);
	}

	public void setExp(double amt) {
		setExp(0.0D, amt);
	}

	private void setExp(double base, double amt) {
		int xp = (int) Math.max(base + amt, 0.0D);

		Player player = getPlayer();
		int curLvl = player.getLevel();
		int newLvl = getLevelForExp(xp);
		if (curLvl != newLvl) {
			player.setLevel(newLvl);
		}
		if (xp > base) {
			player.setTotalExperience(player.getTotalExperience() + xp
					- (int) base);
		}
		double pct = (base - getXpForLevel(newLvl) + amt)
				/ getXpNeededToLevelUp(newLvl);
		player.setExp((float) pct);
	}

	public int getCurrentExp() {
		Player player = getPlayer();

		int lvl = player.getLevel();
		int cur = getXpForLevel(lvl)
				+ Math.round(getXpNeededToLevelUp(lvl) * player.getExp());
		return cur;
	}

	private double getCurrentFractionalXP() {
		Player player = getPlayer();

		int lvl = player.getLevel();
		double cur = getXpForLevel(lvl) + getXpNeededToLevelUp(lvl)
				* player.getExp();
		return cur;
	}

	public boolean hasExp(int amt) {
		return getCurrentExp() >= amt;
	}

	public boolean hasExp(double amt) {
		return getCurrentFractionalXP() >= amt;
	}

	public int getLevelForExp(int exp) {
		if (exp <= 0) {
			return 0;
		}
		if (exp > xpTotalToReachLevel[(xpTotalToReachLevel.length - 1)]) {
			int newMax = calculateLevelForExp(exp) * 2;
			Validate.isTrue(newMax <= hardMaxLevel, "Level for exp " + exp
					+ " > hard max level " + hardMaxLevel);
			initLookupTables(newMax);
		}
		int pos = Arrays.binarySearch(xpTotalToReachLevel, exp);
		return pos < 0 ? -pos - 2 : pos;
	}

	public int getXpNeededToLevelUp(int level) {
		Validate.isTrue(level >= 0, "Level may not be negative.");
		return level >= 16 ? 17 + (level - 15) * 3
				: level > 30 ? 62 + (level - 30) * 7 : 17;
	}

	public int getXpForLevel(int level) {
		Validate.isTrue((level >= 0) && (level <= hardMaxLevel),
				"Invalid level " + level + "(must be in range 0.."
						+ hardMaxLevel + ")");
		if (level >= xpTotalToReachLevel.length) {
			initLookupTables(level * 2);
		}
		return xpTotalToReachLevel[level];
	}
}

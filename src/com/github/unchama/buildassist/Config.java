package com.github.unchama.buildassist;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.unchama.seichiassist.util.Util;

public class Config{
	private static FileConfiguration config;
	private BuildAssist plugin;

	//コンストラクタ
	Config(BuildAssist _plugin){
		plugin = _plugin;
		saveDefaultConfig();
	}

	//コンフィグのロード
	public void loadConfig(){
		config = getConfig();
	}

	//コンフィグのリロード
	public void reloadConfig(){
		plugin.reloadConfig();
		config = getConfig();
	}

	//コンフィグのセーブ
	public void saveConfig(){
		plugin.saveConfig();
	}

	//plugin.ymlがない時にDefaultのファイルを生成
	public void saveDefaultConfig(){
		plugin.saveDefaultConfig();
	}

	//plugin.ymlファイルからの読み込み
	public FileConfiguration getConfig(){
		return plugin.getConfig();
	}


	public int getFlyExp() {
		return Util.toInt(config.getString("flyexp"));
	}
/*
	public String getURL(){
		String url = "jdbc:mysql://";
		url += config.getString("host");
		if(!config.getString("port").isEmpty()){
			url += ":" + config.getString("port");
		}
		return url;
	}

	public String getLvMessage(int i) {
		return config.getString("lv" + i + "message","");
	}
*/


	public int getblocklineuplevel() {
		return Util.toInt(config.getString("blocklineup.level"));
	}
	public double getblocklineupmana_mag() {
		return Util.toDouble(config.getString("blocklineup.mana_mag"));
	}

	public int getZoneSetSkillLevel(){
		return Util.toInt(config.getString("ZoneSetSkill.level"));
	}
}
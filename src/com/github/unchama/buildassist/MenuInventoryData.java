package com.github.unchama.buildassist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class MenuInventoryData {


	public static Inventory getMenuData(Player p){
		//プレイヤーを取得
		Player player = p.getPlayer();
		//UUID取得
		UUID uuid = player.getUniqueId();
		//プレイヤーデータ
		PlayerData playerdata = BuildAssist.playermap.get(uuid);
		//プレイヤーデータが無い場合は処理終了
		if(playerdata == null){
			return null;
		}

		Inventory inventory = Bukkit.getServer().createInventory(null,4*9,ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "木の棒メニューB");
		ItemStack itemstack;
		ItemMeta itemmeta;
		SkullMeta skullmeta;
		List<String> lore = new ArrayList<String>();

		//flyflag/flytimeのメニュー表示用変換
		String Flyallows ;
		if(playerdata.flyflag){
			Flyallows = "ON" ;
		}else{
			Flyallows = "OFF" ;
		}
		String FlyTime ;
		if(playerdata.Endlessfly){
			FlyTime = "∞" ;
		}else {
			FlyTime = String.valueOf(playerdata.flytime);
		}

		String ZSSkill ;
		if(playerdata.ZoneSetSkillFlag){
			ZSSkill = "ON" ;
		}else {
			ZSSkill = "OFF" ;
		}



//		int prank = Util.calcPlayerRank(player);
		itemstack = new ItemStack(Material.SKULL_ITEM,1);
		skullmeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		itemstack.setDurability((short) 3);
		skullmeta.addEnchant(Enchantment.DIG_SPEED, 100, false);
		skullmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + playerdata.name + "の統計データ");
		lore.clear();
		lore.addAll(Arrays.asList(ChatColor.RESET + "" +  ChatColor.AQUA + "建築レベル:" + playerdata.level
//				, ChatColor.RESET + "" +  ChatColor.AQUA + "次のレベルまで:" + (BuildAssist.levellist.get(playerdata.level + 1).intValue() - playerdata.totalbuildnum)
				, ChatColor.RESET + "" +  ChatColor.AQUA + "総建築量:" + playerdata.totalbuildnum
//				, ChatColor.RESET + "" +  ChatColor.GOLD + "ランキング：" + prank + "位" + ChatColor.RESET + "" +  ChatColor.GRAY + "(" + BuildAssist.ranklist.size() +"人中)"

				));
		/*
		if(prank > 1){
			lore.add(ChatColor.RESET + "" +  ChatColor.AQUA + (prank-1) + "位との差：" + (BuildAssist.ranklist.get(prank-2).intValue() - playerdata.totalbuildnum));
		}
		*/
		lore.add(ChatColor.RESET + "" +  ChatColor.DARK_GRAY + "※1分毎に更新");

		skullmeta.setLore(lore);
		skullmeta.setOwner(playerdata.name);
		itemstack.setItemMeta(skullmeta);
		inventory.setItem(0,itemstack);


		//FLY 状況表示
		itemstack = new ItemStack(Material.COOKED_CHICKEN,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.COOKED_CHICKEN);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "FLY機能 情報表示");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.AQUA + "" + "FLY 効果："+ Flyallows
				, ChatColor.RESET + "" + ChatColor.AQUA + "FLY 残り時間："+ FlyTime );
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(2,itemstack);

		//1分限定、FLY ON
		itemstack = new ItemStack(Material.FEATHER,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.FEATHER);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "FLY機能、ON" + ChatColor.AQUA + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "(1分)");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.YELLOW + "クリックすると以降1分間に渡り"
				, ChatColor.RESET + "" + ChatColor.YELLOW + "経験値を消費しつつFLYが可能になります。"
				, ChatColor.RESET + "" +  ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "必要経験値量：毎分 "+BuildAssist.config.getFlyExp());
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(3,itemstack);

		//5分限定、FLY ON
		itemstack = new ItemStack(Material.FEATHER,5);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.FEATHER);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "FLY機能、ON" + ChatColor.GREEN + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "(5分)");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.YELLOW + "クリックすると以降5分間に渡り"
				, ChatColor.RESET + "" + ChatColor.YELLOW + "経験値を消費しつつFLYが可能になります。"
				, ChatColor.RESET + "" +  ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "必要経験値量：毎分 "+BuildAssist.config.getFlyExp());
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(4,itemstack);

		//エンドレス、FLY ON
		itemstack = new ItemStack(Material.ELYTRA,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.ELYTRA);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "FLY機能、ON" + ChatColor.RED + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "(無制限)");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.YELLOW + "クリックすると以降OFFにするまで"
				, ChatColor.RESET + "" + ChatColor.YELLOW + "経験値を消費しつつFLYが可能になります。"
				, ChatColor.RESET + "" +  ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "必要経験値量：毎分 "+BuildAssist.config.getFlyExp());
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(5,itemstack);

		//FLY 効果 OFF
		itemstack = new ItemStack(Material.CHAINMAIL_BOOTS,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.CHAINMAIL_BOOTS);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "FLY機能、OFF");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.RED + "クリックすると、残り時間に関わらず"
				, ChatColor.RESET + "" + ChatColor.RED + "FLYを終了します。") ;
		itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(6,itemstack);

		//範囲設置スキル ON/OFFボタン
		itemstack = new ItemStack(Material.STONE,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.STONE);
		itemmeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "「範囲設置スキル」現在：" + ZSSkill );
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.YELLOW + "「スニーク+左クリック」をすると、"
				, ChatColor.RESET + "" + ChatColor.YELLOW + "オフハンドに持っているブロックと同じ物を"
				, ChatColor.RESET + "" + ChatColor.YELLOW  + "インベントリ内から消費し設置します。"
				, ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "＜クリックでON/OFF切り替え＞"
				, ChatColor.RESET + "" + ChatColor.GRAY + "建築LV" + BuildAssist.config.getZoneSetSkillLevel() + "以上で利用可能");
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(18,itemstack);


		//範囲設置スキル 設定画面移動
		itemstack = new ItemStack(Material.SKULL_ITEM,1);
		skullmeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		itemstack.setDurability((short) 3);
		skullmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "「範囲設置スキル」設定画面へ");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "クリックで移動");
		skullmeta.setLore(lore);
		skullmeta.setOwner("MHF_Exclamation");
		itemstack.setItemMeta(skullmeta);
		inventory.setItem(19,itemstack);


		//ブロックを並べるスキル設定
		itemstack = new ItemStack(Material.WOOD,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.WOOD);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "ブロックを並べるスキル（仮） ：" + BuildAssist.line_up_str[playerdata.line_up_flg]);
		lore = Arrays.asList(ChatColor.RESET + "" + ChatColor.GRAY + "オフハンドに木の棒、メインハンドにブロックを持ってる状態で"
				, ChatColor.RESET + "" + ChatColor.GRAY + "左クリックするとメインハンドのブロックを向いてる方向に並べて設置します。"
				, ChatColor.RESET + "" + ChatColor.GRAY + "建築LV" + BuildAssist.config.getblocklineuplevel() + "以上で利用可能"
				, ChatColor.RESET + "" + ChatColor.GRAY + "クリックで切り替え"
				, ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "※スキル発動時にマナを消費します。 最大消費マナ："+(BuildAssist.config.getblocklineupmana_mag()*64)
				);
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(27,itemstack);

		//ブロックを並べるスキルハーフブロック設定
		itemstack = new ItemStack(Material.STEP,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.STEP);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "ブロックを並べるスキル（仮）ハーフブロック設定 ：" + BuildAssist.line_up_step_str[playerdata.line_up_step_flg]);
		lore = Arrays.asList(ChatColor.RESET + "" + ChatColor.GRAY + "ブロックを並べるスキルでハーフブロックを並べる時の位置を決めます。"
				, ChatColor.RESET + "" + ChatColor.GRAY + "クリックで切り替え"
				);
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(28,itemstack);

		//ブロックを並べるスキル一部ブロックを破壊して並べる設定
		itemstack = new ItemStack(Material.TNT,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.TNT);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "ブロックを並べるスキル（仮）破壊設定 ：" + BuildAssist.line_up_des_str[playerdata.line_up_des_flg]);
		lore = Arrays.asList(ChatColor.RESET + "" + ChatColor.GRAY + "ブロックを並べるスキルでブロックを並べるとき一部ブロックを破壊して並べます。"
				, ChatColor.RESET + "" + ChatColor.GRAY + "破壊対象ブロック：草,花,水,雪,松明,きのこ"
				, ChatColor.RESET + "" + ChatColor.GRAY + "クリックで切り替え"
				);
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(29,itemstack);
		return inventory;

	}


	public static Inventory getSetBlockSkillData(Player p){
		//プレイヤーを取得
		Player player = p.getPlayer();
		//UUID取得
		UUID uuid = player.getUniqueId();
		//プレイヤーデータ
		PlayerData playerdata = BuildAssist.playermap.get(uuid);

		Inventory inventory = Bukkit.getServer().createInventory(null,4*9,ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "「範囲設置スキル」設定画面");
		ItemStack itemstack;
		ItemMeta itemmeta;
		SkullMeta skullmeta;
		List<String> lore = new ArrayList<String>();

		String ZSSkill ;
		if(playerdata.ZoneSetSkillFlag){
			ZSSkill = "ON" ;
		}else {
			ZSSkill = "OFF" ;
		}


		String ZSDirt ;
		if(playerdata.zsSkillDirtFlag){
			ZSDirt = "ON" ;
		}else {
			ZSDirt = "OFF" ;
		}

		int ZSSkillA =(playerdata.AREAint) * 2 + 1;



		//初期画面へ移動
		itemstack = new ItemStack(Material.BARRIER,1);
		itemmeta =Bukkit.getItemFactory().getItemMeta(Material.BARRIER);
		itemstack.setDurability((short) 3);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "元のページへ");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "クリックで移動");
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(0,itemstack);


		//土設置のON/OFF
		itemstack = new ItemStack(Material.DIRT,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.STONE);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "設置時に下の空洞を埋める機能");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.AQUA + "" + ChatColor.UNDERLINE + "機能の使用設定：" + ZSDirt
							,ChatColor.RESET + "" +  ChatColor.AQUA + "" + ChatColor.UNDERLINE + "機能の範囲：地下5マスまで");
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(4,itemstack);


		//設定状況の表示
		itemstack = new ItemStack(Material.STONE,1);
		itemmeta = Bukkit.getItemFactory().getItemMeta(Material.STONE);
		itemmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "現在の設定は以下の通りです");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.AQUA + "" + ChatColor.UNDERLINE + "スキルの使用設定：" + ZSSkill
							,ChatColor.RESET + "" +  ChatColor.AQUA + "" + ChatColor.UNDERLINE + "スキルの範囲設定：" + ZSSkillA + "×" + ZSSkillA);
		itemmeta.setLore(lore);
		itemstack.setItemMeta(itemmeta);
		inventory.setItem(13,itemstack);


		//範囲をMAXへ
		itemstack = new ItemStack(Material.SKULL_ITEM,11);
		skullmeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		itemstack.setDurability((short) 3);
		skullmeta.setDisplayName(ChatColor.RED + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "範囲設定を最大値に変更");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.AQUA + "" + "現在の範囲設定：" + ZSSkillA + "×" + ZSSkillA
							,ChatColor.RESET + "" +  ChatColor.AQUA + "" + ChatColor.UNDERLINE + "変更後の範囲設定：11×11");
		skullmeta.setLore(lore);
		skullmeta.setOwner("MHF_ArrowUp");
		itemstack.setItemMeta(skullmeta);
		inventory.setItem(19,itemstack);


		//範囲を一段階増加
		itemstack = new ItemStack(Material.SKULL_ITEM,7);
		skullmeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		itemstack.setDurability((short) 3);
		skullmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "範囲設定を一段階大きくする");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.AQUA + "" + "現在の範囲設定：" + ZSSkillA + "×" + ZSSkillA
							,ChatColor.RESET + "" +  ChatColor.AQUA + "" + ChatColor.UNDERLINE + "変更後の範囲設定："+ (ZSSkillA + 2) +"×"+(ZSSkillA + 2)
							,ChatColor.RESET + "" +  ChatColor.RED + "" + "※範囲設定の最大値は11×11※");
		skullmeta.setLore(lore);
		skullmeta.setOwner("MHF_ArrowUp");
		itemstack.setItemMeta(skullmeta);
		inventory.setItem(20,itemstack);


		//範囲を初期値へ
		itemstack = new ItemStack(Material.SKULL_ITEM,5);
		skullmeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		itemstack.setDurability((short) 3);
		skullmeta.setDisplayName(ChatColor.RED + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "範囲設定を初期値に変更");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.AQUA + "" + "現在の範囲設定：" + ZSSkillA + "×" + ZSSkillA
							,ChatColor.RESET + "" +  ChatColor.AQUA + "" + ChatColor.UNDERLINE + "変更後の範囲設定：5×5");
		skullmeta.setLore(lore);
		skullmeta.setOwner("MHF_TNT");
		itemstack.setItemMeta(skullmeta);
		inventory.setItem(22,itemstack);


		//範囲を一段階減少
		itemstack = new ItemStack(Material.SKULL_ITEM,3);
		skullmeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		itemstack.setDurability((short) 3);
		skullmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "範囲設定を一段階小さくする");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.AQUA + "" + "現在の範囲設定：" + ZSSkillA + "×" + ZSSkillA
							,ChatColor.RESET + "" +  ChatColor.AQUA + "" + ChatColor.UNDERLINE + "変更後の範囲設定："+ (ZSSkillA - 2) +"×"+(ZSSkillA - 2)
							,ChatColor.RESET + "" +  ChatColor.RED + "" + "※範囲設定の最小値は3×3※");
		skullmeta.setLore(lore);
		skullmeta.setOwner("MHF_ArrowDown");
		itemstack.setItemMeta(skullmeta);
		inventory.setItem(24,itemstack);


		//範囲をMINへ
		itemstack = new ItemStack(Material.SKULL_ITEM,1);
		skullmeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		itemstack.setDurability((short) 3);
		skullmeta.setDisplayName(ChatColor.RED + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "範囲設定を最小値に変更");
		lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.AQUA + "" + "現在の範囲設定：" + ZSSkillA + "×" + ZSSkillA
							,ChatColor.RESET + "" +  ChatColor.AQUA + "" + ChatColor.UNDERLINE + "変更後の範囲設定：3×3");
		skullmeta.setLore(lore);
		skullmeta.setOwner("MHF_ArrowDown");
		itemstack.setItemMeta(skullmeta);
		inventory.setItem(25,itemstack);


		return inventory;
	}
}
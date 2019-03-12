package net.bausucht.hunjy.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.DisplaySlot;

import net.bausucht.floobuild.database.settings.PlayerSettings;
import net.bausucht.hunjy.Settings;
import net.bausucht.hunjy.utils.CustomInventory;
import net.bausucht.hunjy.utils.ItemBuilder;
import net.bausucht.hunjy.utils.ScalableInventory;
import net.bausucht.hunjy.utils.Skull;
import net.bausucht.premiumchat.Premiumchat;
import net.minesucht.coinsapi.CoinsAPI;
import net.minesucht.friendsystem.FriendSystem;
import net.minesucht.friendsystem.user.FriendUser;
import net.minesucht.protocol.packet.friend.FriendRequest;
import net.minesucht.protocol.packet.friend.entry.FriendListEntry;
import net.minesucht.protocol.packet.friend.entry.FriendRequestEntry;
import net.minesucht.protocol.packet.ignore.IgnoreList;
import net.minesucht.protocol.packet.ignore.entry.IgnoreListEntry;

public class InventoryManager {

	public HashMap<Player, String> playerTimeChash;
	public HashMap<Player, Integer> playerPage;

	public HashMap<Player, ScalableInventory> scalableInventorys;
	public HashMap<Player, CustomInventory> customInventorys;
	public FileManager fileManager;

	public FriendSystem friendSystem = Settings.getInstance().getFriendSystem();
	
	public InventoryManager() {
		fileManager = Settings.getInstance().getFileManager();
		playerTimeChash = new HashMap<>();
		playerPage = new HashMap<>();
		scalableInventorys = new HashMap<>();
		customInventorys = new HashMap<>();

	}

	public void openMainInventory(Player player) {
		CustomInventory inventory = new CustomInventory("§8Einstellungen", 27);
		customInventorys.put(player, inventory);
		

		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		ArrayList<String> lore = new ArrayList<>();
		SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		meta.setOwner(player.getName());
		meta.setDisplayName("§dFreunde verwalten");
		lore.add("§fErmöglicht die Verwaltung deiner Freunde");
		meta.setLore(lore);
		skull.setItemMeta(meta);

		inventory.setItemWithNBT(10, new ItemBuilder(Material.SIGN).setDisplayName("§bAllgemeine Befehle").addLore("§fErmöglicht die Verwaltung der Allgemeinen Befehle").build(), "settings", "general");
		inventory.setItemWithNBT(13, new ItemBuilder(Material.GRASS).setDisplayName("§aPlot Verwaltung").addLore("§fErmöglicht die Verwaltung deines Plots").build(), "settings", "plot");
		
		if (Bukkit.getServer().getPluginManager().getPlugin("FriendSystem") != null) {
			if (Bukkit.getServer().getPluginManager().isPluginEnabled("FriendSystem")) {
				inventory.setItemWithNBT(16, skull, "settings", "friends");
			} else {
				inventory.setItem(16, Skull.getCustomSkull("http://textures.minecraft.net/texture/732fe121a63eaabd99ced6d1acc91798652d1ee8084d2f9127d8a315cad5ce4", "§cDiese Funktion ist momentan nicht verfügbar"));
			}
		} else {
			inventory.setItem(16, Skull.getCustomSkull("http://textures.minecraft.net/texture/732fe121a63eaabd99ced6d1acc91798652d1ee8084d2f9127d8a315cad5ce4", "§cDiese Funktion ist momentan nicht verfügbar"));
		}

		inventory.openInventory(player);
	}

	public void openGeneralInventory(Player player) {

		CustomInventory inventory = new CustomInventory("§8Allgemeine Einstellungen", 54);
		customInventorys.put(player, inventory);

		inventory.setItem(1, fileManager.loadItem("Muenzen"));

		if (Bukkit.getServer().getPluginManager().getPlugin("CoinsAPI") != null) {
			if (Bukkit.getServer().getPluginManager().isPluginEnabled("CoinsAPI")) {
				if (CoinsAPI.allowedShowCoins(player.getUniqueId()))
					inventory.setItemWithNBT(2, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aAlle").build(), "settings", "general_toggle_muenzen");
				else
					inventory.setItemWithNBT(2, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cNiemand").build(), "settings", "general_toggle_muenzen");
			} else {
				inventory.setItem(2, new ItemBuilder(Material.PAPER).setDisplayName("§cDiese Funktion ist momentan nicht verfügbar").build());
			}
		} else {
			inventory.setItem(2, new ItemBuilder(Material.PAPER).setDisplayName("§cDiese Funktion ist momentan nicht verfügbar").build());
		}
		inventory.setItem(10, fileManager.loadItem("Scoreboard"));
		if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
			inventory.setItemWithNBT(11, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aAktiviert").build(), "settings", "general_toggle_scoreboard");
		} else {
			inventory.setItemWithNBT(11, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cDeaktiviert").build(), "settings", "general_toggle_scoreboard");
		}

		inventory.setItem(19, fileManager.loadItem("Standardfarbe"));

		if (Bukkit.getServer().getPluginManager().getPlugin("premiumchat") != null) {
			if (Bukkit.getServer().getPluginManager().isPluginEnabled("premiumchat")) {
				if (Premiumchat.getInstance().getDefaultColorMap().containsKey(player.getUniqueId())) {
					if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§a")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 5).setDisplayName("§aHellgrün").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§b")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 3).setDisplayName("§bHellblau").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§c")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 14).setDisplayName("§cRot").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§d")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 6).setDisplayName("§dPink").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§e")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 4).setDisplayName("§eGelb").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§f")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 0).setDisplayName("§fWeiß").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§1")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 11).setDisplayName("§1Dunkelblau").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§2")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 13).setDisplayName("§2Grün").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§3")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 9).setDisplayName("§3Türkis").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§4")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 14).setDisplayName("§4Dunkelrot").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§5")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 10).setDisplayName("§5Lila").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§6")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 1).setDisplayName("§6Gold").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§7")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 8).setDisplayName("§7Hellgrau").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§8")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 7).setDisplayName("§8Grau").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§9")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 11).setDisplayName("§9Blau").build(), "settings", "general_toggle_premiumchat");
					} else if (Premiumchat.getInstance().getDefaultColorMap().get(player.getUniqueId()).equals("§0")) {
						inventory.setItemWithNBT(20, new ItemBuilder(Material.WOOL, (short) 15).setDisplayName("§0Schwarz").build(), "settings", "general_toggle_premiumchat");
					}
				} else {
					inventory.setItemWithNBT(20, new ItemBuilder(Material.INK_SACK, (short) 7).setDisplayName("§7Hell Grau").build(), "settings", "general_toggle_premiumchat");
				}
			} else {
				inventory.setItem(20, new ItemBuilder(Material.PAPER).setDisplayName("§cDiese Funktion ist momentan nicht verfügbar").build());
			}
		} else {
			inventory.setItem(20, new ItemBuilder(Material.PAPER).setDisplayName("§cDiese Funktion ist momentan nicht verfügbar").build());
		}
		inventory.setItem(28, fileManager.loadItem("Onlinezeit"));
		FriendUser friendUser = Settings.getInstance().getFriendSystem().getFriendUser(player.getUniqueId());
		if (friendUser != null) {
			if (friendUser.getSetting("allow_see_onlinetime").equals("false")) {
				inventory.setItemWithNBT(29, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cKeiner").build(), "settings", "general_toggle_onlinetime");
			} else {
				inventory.setItemWithNBT(29, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aAlle").build(), "settings", "general_toggle_onlinetime");
			}
		} else {
			inventory.setItem(20, new ItemBuilder(Material.PAPER).setDisplayName("§cDiese Funktion ist momentan nicht verfügbar").build());
		}

		inventory.setItem(37, fileManager.loadItem("Tag/Nacht"));

		if (player.hasPermission("command.settime")) {
			if (playerTimeChash.containsKey(player)) {
				if (playerTimeChash.get(player).equals("day")) {
					inventory.setItemWithNBT(38, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aTag").build(), "settings", "general_toggle_time");
				} else {
					inventory.setItemWithNBT(38, new ItemBuilder(Material.INK_SACK).setDisplayName("§7Nacht").build(), "settings", "general_toggle_time");
				}
			} else {
				inventory.setItemWithNBT(38, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cDeaktiviert").build(), "settings", "general_toggle_time");
			}
		} else {
			inventory.setItem(38, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§7Keine Rechte").build());
		}

		inventory.setItem(46, fileManager.loadItem("Supportanzeige"));
		if(player.hasMetadata( "disable-actionbar" )) {
			inventory.setItemWithNBT(47, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cDeaktiviert").build(), "settings", "general_toggle_support");
		}else {
			inventory.setItemWithNBT(47, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aAktiviert").build(), "settings", "general_toggle_support");
		}

		// inv.setItem(7, fileManager.loadItem("Near"));
		// inv.setItem(6, new ItemBuilder(Material.INK_SACK, (short)
		// 10).setDisplayName("§aAktiviert").build());

		inventory.setItem(7, fileManager.loadItem("TPA-Anfragen"));
		if(PlayerSettings.from(player).isTeleportEnabled()) {
			inventory.setItemWithNBT(6, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aZugelassen").build(), "settings", "general_toggle_tpa");
		}else {
			inventory.setItemWithNBT(6, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cVerboten").build(), "settings", "general_toggle_tpa");
		}

		inventory.setItem(16, fileManager.loadItem("Sonne/Regen"));
		if (player.hasPermission("command.settime")) {

			if (player.getPlayerWeather() == WeatherType.DOWNFALL) {
				inventory.setItemWithNBT(15, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cRegen").build(), "settings", "general_toggle_weather");
			} else {
				inventory.setItemWithNBT(15, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aSonne").build(), "settings", "general_toggle_weather");
			}

		} else {
			inventory.setItem(15, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§7Keine Rechte").build());
		}

		inventory.setItem(25, fileManager.loadItem("Chat-Filter"));

		if (Bukkit.getServer().getPluginManager().getPlugin("premiumchat") != null) {
			if (Bukkit.getServer().getPluginManager().isPluginEnabled("premiumchat")) {
				if (Premiumchat.getInstance().getPremiumChat().containsKey(player)) {
					if (Premiumchat.getInstance().getPremiumChat().get(player).equals("friend")
							|| Premiumchat.getInstance().getPremiumChat().get(player).equals("freunde")) {
						inventory.setItemWithNBT(24,
								new ItemBuilder(Material.INK_SACK, (short) 14).setDisplayName("§eFreunde").build(), "settings", "general_toggle_chat");
					} else if (Premiumchat.getInstance().getPremiumChat().get(player).equals("premium")) {
						inventory.setItemWithNBT(24,
								new ItemBuilder(Material.INK_SACK, (short) 11).setDisplayName("§6Premium").build(), "settings", "general_toggle_chat");
					} else if (Premiumchat.getInstance().getPremiumChat().get(player).equals("räuber")) {
						inventory.setItemWithNBT(24,
								new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aRaüber").build(), "settings", "general_toggle_chat");
					} else if (Premiumchat.getInstance().getPremiumChat().get(player).equals("ultra")) {
						inventory.setItemWithNBT(24,
								new ItemBuilder(Material.INK_SACK, (short) 12).setDisplayName("§bUltra").build(), "settings", "general_toggle_chat");
					} else if (Premiumchat.getInstance().getPremiumChat().get(player).equals("king")) {
						inventory.setItemWithNBT(24, new ItemBuilder(Material.INK_SACK, (short) 1).setDisplayName("§cKing").build(), "settings", "general_toggle_chat");
					} else if (Premiumchat.getInstance().getPremiumChat().get(player).equals("supreme")) {
						inventory.setItemWithNBT(24,
								new ItemBuilder(Material.INK_SACK, (short) 9).setDisplayName("§dSupreme").build(), "settings", "general_toggle_chat");
					} else if (Premiumchat.getInstance().getPremiumChat().get(player).equals("youtuber")) {
						inventory.setItemWithNBT(24,
								new ItemBuilder(Material.INK_SACK, (short) 5).setDisplayName("§5YouTuber").build(), "settings", "general_toggle_chat");
					} else if (Premiumchat.getInstance().getPremiumChat().get(player).equals("team")) {
						inventory.setItemWithNBT(24, new ItemBuilder(Material.INK_SACK, (short) 6).setDisplayName("§9Team").build(), "settings", "general_toggle_chat");
					}
				} else {
					inventory.setItemWithNBT(24,
							new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cDeaktiviert").build(), "settings", "general_toggle_chat");
				}
			} else {
				inventory.setItem(24, new ItemBuilder(Material.PAPER)
						.setDisplayName("§cDiese Funktion ist momentan nicht verfügbar").build());
			}
		} else {
			inventory.setItem(24, new ItemBuilder(Material.PAPER)
					.setDisplayName("§cDiese Funktion ist momentan nicht verfügbar").build());
		}
		

		inventory.setItemWithNBT(53, new ItemBuilder(Material.INK_SACK, (short) 1).setDisplayName("§9Zurück").build(), "settings", "back_to_main");

		inventory.openInventory(player);
	}

	public void openChatInventory(Player player) {
		CustomInventory inventory = new CustomInventory("§8Chat Einstellungen", 9);
		customInventorys.put(player, inventory);

		inventory.setItemWithNBT(0, new ItemBuilder(Material.INK_SACK, (short) 11).setDisplayName("§6Premium").build(), "settings", "chat_toggle");
		inventory.setItemWithNBT(1, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aRäuber").build(), "settings", "chat_toggle");
		inventory.setItemWithNBT(2, new ItemBuilder(Material.INK_SACK, (short) 12).setDisplayName("§bUltra").build(), "settings", "chat_toggle");
		inventory.setItemWithNBT(3, new ItemBuilder(Material.INK_SACK, (short) 1).setDisplayName("§cKing").build(), "settings", "chat_toggle");
		inventory.setItemWithNBT(4, new ItemBuilder(Material.INK_SACK, (short) 9).setDisplayName("§dSupreme").build(), "settings", "chat_toggle");
		inventory.setItemWithNBT(5, new ItemBuilder(Material.INK_SACK, (short) 5).setDisplayName("§5YouTuber").build(), "settings", "chat_toggle");
		inventory.setItemWithNBT(6, new ItemBuilder(Material.INK_SACK, (short) 6).setDisplayName("§9Team").build(), "settings", "chat_toggle");
		inventory.setItemWithNBT(7, new ItemBuilder(Material.INK_SACK, (short) 14).setDisplayName("§eFreunde").build(), "settings", "chat_toggle");
		inventory.setItemWithNBT(8, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cDeaktiviert").build(), "settings", "chat_toggle");

		inventory.openInventory(player);
	}

	public void openTimeInventory(Player player) {
		CustomInventory inventory = new CustomInventory("§8Zeit Einstellungen", 9);
		customInventorys.put(player, inventory);

		inventory.setItemWithNBT(1, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aTag").build(), "settings", "time_toggle");
		inventory.setItemWithNBT(4, new ItemBuilder(Material.INK_SACK).setDisplayName("§7Nacht").build(), "settings", "time_toggle");
		inventory.setItemWithNBT(7, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cDeaktiviert").build(), "settings", "time_toggle");

		inventory.openInventory(player);
	}

	public void openFriendInventory(Player player) {
		CustomInventory inventory = new CustomInventory("§8Freundes Einstellungen", 36);
		customInventorys.put(player, inventory);

		inventory.setItem(1, new ItemBuilder(Material.SIGN).setDisplayName("§eFreundschaftsanfragen").addLore("§fErmöglicht das Aktivieren und das Deaktivieren\n§fdeiner Freundschaftsanfragen").build());
		FriendUser friendUser = Settings.getInstance().getFriendSystem().getFriendUser(player.getUniqueId());
		if (friendUser != null) {
			if (friendUser.getSetting("allow_friend_requests").equals("false")) {
				inventory.setItemWithNBT(2, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cKeiner").build(), "settings", "friend_toggle_requests");
			} else {
				inventory.setItemWithNBT(2, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aAlle").build(), "settings", "friend_toggle_requests");
			}
		}


		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		ArrayList<String> lore = new ArrayList<>();
		SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		meta.setOwner(player.getName());
		meta.setDisplayName("§dDeine Freunde §8[§e" + friendUser.getFriendList().size() + "§8]");
		lore.add("§fZeigt deine Freunde");
		meta.setLore(lore);
		skull.setItemMeta(meta);

		inventory.setItemWithNBT(13, skull, "settings", "open_friendslist");

		inventory.setItem(10, new ItemBuilder(Material.BOOK).setDisplayName("§bPrivate Nachrichten").addLore("§fErmöglicht das Einstellen welche Spieler\n§fdir eine Private Nachricht senden können").removeAllAtributs().build());
		String messageSetting = friendUser.getSetting("message_retrieval");
		if (messageSetting.isEmpty() || messageSetting.equals("ALL")) {
			inventory.setItemWithNBT(11, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aAlle").build(), "settings", "friend_toggle_msg");
		} else if (messageSetting.equals("FRIENDS")) {
			inventory.setItemWithNBT(11, new ItemBuilder(Material.INK_SACK, (short) 9).setDisplayName("§dFreunde").build(), "settings", "friend_toggle_msg");
		} else if (messageSetting.equals("NONE")) {
			inventory.setItemWithNBT(11, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cKeiner").build(), "settings", "friend_toggle_msg");
		}

		inventory.setItem(19, new ItemBuilder(Material.BOOK).setDisplayName("§eOnline/Offline Nachrichten").addLore("§fErmöglicht die Einstellung,\n§fob man eine Benachríchtigung möchte,\n§fwenn ein Freund Online oder Offline geht").addEnchantment(Enchantment.DURABILITY, 1).removeAllAtributs().build());
		if (!(friendUser.getSetting("allow_join_leave_messages").equals("false"))) {
			inventory.setItemWithNBT(20, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aAktiviert").build(), "settings", "friend_toggle_on/off_msg");
		} else {
			inventory.setItemWithNBT(20, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cDeaktiviert").build(), "settings", "friend_toggle_on/off_msg");
		}

		inventory.setItemWithNBT(7, new ItemBuilder(Material.MAP).setDisplayName("§dOffene Freundschaftsanfragen §8[§e" + friendUser.getFriendRequests().size() + "§8]").addLore("§fHier kannst du deine\n§foffene Freundschaftsanfragen verwalten").removeAllAtributs().build(), "settings", "open_requests");
		inventory.setItemWithNBT(16, new ItemBuilder(Material.PAPER).setDisplayName("§cIgnorierte Spieler §8[§e" + friendUser.getIgnoreList().size() + "§8]").addLore("§fErmöglicht die Verwaltung\n§fder Ignorierten Spieler").removeAllAtributs().build(), "settings", "open_ignore");
		inventory.setItemWithNBT(25,new ItemBuilder(Material.BARRIER).setDisplayName("§cFreundesliste komplett leeren").addLore("§fLöscht §calle §fFreunde deiner Liste!\n§cBitte nur mit Sorgfalt verwenden").removeAllAtributs().build(), "settings", "friends_list_clear");

		inventory.setItemWithNBT(35, new ItemBuilder(Material.INK_SACK, (short) 1).setDisplayName("§9Zurück").build(), "settings", "back_to_main");

		inventory.openInventory(player);
	}

	public void openFriendsInventory(Player player) {
		FriendUser friendUser = Settings.getInstance().getFriendSystem().getFriendUser(player.getUniqueId());
		scalableInventorys.put(player, new ScalableInventory("§8Freunde"));
		for(int i = 0; i < friendUser.getFriendList().size(); i++) {
				FriendListEntry currentRequest = friendUser.getFriendList().get(i);
				SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
				if(currentRequest.isOnline()) {
					meta.setOwner(currentRequest.getName());
				}
				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				meta.setDisplayName(currentRequest.getColor() + currentRequest.getName());
				ArrayList<String> lore = new ArrayList<>();
				lore.add(currentRequest.isOnline() ? "§aOnline" : "§cOffline");
				meta.setLore(lore);
				skull.setItemMeta(meta);
				scalableInventorys.get(player).loadItem(i, skull, "settings", "friends_open_friendinfo");
		}

		scalableInventorys.get(player).openInventory(player);
	}

	public void openRequests(Player player) {
		FriendUser friendUser = Settings.getInstance().getFriendSystem().getFriendUser(player.getUniqueId());
		scalableInventorys.put(player, new ScalableInventory("§8Freundschaftsanfragen"));
		
			for(int i = 0; i < friendUser.getFriendRequests().size(); i++) {
				FriendRequestEntry currentRequest = friendUser.getFriendRequests().get(i);
				SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
				meta.setOwner(currentRequest.getName());
	
				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				meta.setDisplayName(currentRequest.getColor() + currentRequest.getName());
				skull.setItemMeta(meta);
				scalableInventorys.get(player).loadItem(i, skull, "settings", "friends_open_request");
			}
		scalableInventorys.get(player).openInventory(player);
	}
	
	public void openIgnoredPlayers(Player player) {
		FriendUser friendUser = Settings.getInstance().getFriendSystem().getFriendUser(player.getUniqueId());
		scalableInventorys.put(player, new ScalableInventory("§8Ignorierte Spieler"));
		
		for(int i = 0; i < friendUser.getIgnoreList().size(); i++) {
			IgnoreListEntry currentRequest = friendUser.getIgnoreList().get(i);
			SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
			meta.setOwner(currentRequest.getName());

			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
			meta.setDisplayName("§c" + currentRequest.getName());
			skull.setItemMeta(meta);
			scalableInventorys.get(player).loadItem(i, skull, "settings", "friends_open_ignore");
		}
		scalableInventorys.get(player).openInventory(player);
	}
	
	public void openFriendMessagesInventory(Player player) {
		CustomInventory inventory = new CustomInventory("§8Private Nachrichten", 9);
		customInventorys.put(player, inventory);

		inventory.setItemWithNBT(1, new ItemBuilder(Material.INK_SACK, (short) 10).setDisplayName("§aAlle")
				.addLore("§fJeder Spieler kann dir privaten\n§fNachrichten senden").build(), "settings", "friends_toggle_private_msg");
		inventory.setItemWithNBT(4, new ItemBuilder(Material.INK_SACK, (short) 9).setDisplayName("§dFreunde")
				.addLore("§fNur deine Freunde können dir privaten\n§fNachrichten senden").build(), "settings", "friends_toggle_private_msg");
		inventory.setItemWithNBT(7, new ItemBuilder(Material.INK_SACK, (short) 8).setDisplayName("§cKeiner")
				.addLore("§fKeiner kann dir private\n§fNachrichten senden").build(), "settings", "friends_toggle_private_msg");

		inventory.openInventory(player);
	}

	public void openIgnoredPlayerInventory(Player player, ItemStack skull) {
		CustomInventory inventory = new CustomInventory("§8Ignorierter Spieler: " + skull.getItemMeta().getDisplayName(), 27);
		customInventorys.put(player, inventory);

		inventory.setItem(13, skull);

		inventory.setItemWithNBT(11, new ItemBuilder(Material.WOOL, (short) 5).setDisplayName("§aFreigeben").build(), "settings", "friends_unignore_confirm");

		inventory.setItemWithNBT(15, new ItemBuilder(Material.WOOL, (short) 14).setDisplayName("§cAbbrechen").build(), "settings", "friends_unignore_cancle");

		inventory.openInventory(player);
	}

	public void openFriendPlayerInventory(Player player, ItemStack skull) {
		CustomInventory inventory = new CustomInventory("§8Anfrage von: " + skull.getItemMeta().getDisplayName(), 27);
		customInventorys.put(player, inventory);

		inventory.setItem(13, skull);

		inventory.setItemWithNBT(11, new ItemBuilder(Material.WOOL, (short) 5).setDisplayName("§aAnnehmen").build(), "settings", "friends_request_confirm");

		inventory.setItemWithNBT(15, new ItemBuilder(Material.WOOL, (short) 14).setDisplayName("§cAblehnen").build(), "settings", "friends_request_deny");

		inventory.openInventory(player);
	}

	public void openFriendSettingsInventory(Player player, ItemStack skull) {
		CustomInventory inventory = new CustomInventory("§8Freund: " + skull.getItemMeta().getDisplayName(), 27);
		customInventorys.put(player, inventory);

		inventory.setItemWithNBT(13, new ItemBuilder(Material.BARRIER).setDisplayName("§cAus deiner Freundesliste entfernen").build(), "settings", "friends_remove_friend");
		inventory.setItemWithNBT(26, new ItemBuilder(Material.INK_SACK, (short) 1).setDisplayName("§9Zurück").build(), "settings", "bacak_to_friends");

		inventory.openInventory(player);
	}
	
	public void openClearFriendList(Player player, ItemStack skull) {
		CustomInventory inventory = new CustomInventory(skull.getItemMeta().getDisplayName(), 27);
		customInventorys.put(player, inventory);

		inventory.setItemWithNBT(11, new ItemBuilder(Material.WOOL, (short) 5).setDisplayName("§aListe leeren").build(), "settings", "friends_list_clear_confirm");
		inventory.setItemWithNBT(15, new ItemBuilder(Material.WOOL, (short) 14).setDisplayName("§cAbbrechen").build(), "settings", "friends_list_clear_deny");

		inventory.openInventory(player);
	}
	
	public void updatePlayer(Player player) {
		FriendUser friendUser = Settings.getInstance().getFriendSystem().getFriendUser(player.getUniqueId());
		if (friendUser != null) {
			Settings.getInstance().getFriendSystem().removeFriendUser(friendUser);

			Settings.getInstance().getFriendSystem().addFriendUser(
					new FriendUser(Settings.getInstance().getFriendSystem(), player.getUniqueId(), player.getName()));

			FriendRequest friendRequest = new FriendRequest();
			friendRequest.setUuid(friendUser.getUuid());
			IgnoreList ignoreList = new IgnoreList();
			ignoreList.setUuid(friendUser.getUuid());
			Settings.getInstance().getMineAPI().getMineAPIClient().sendPacket(friendRequest);
			Settings.getInstance().getMineAPI().getMineAPIClient().sendPacket(ignoreList);

			Settings.getInstance().getInventoryManager().openMainInventory(player);
		}

	}
	
}

package net.bausucht.hunjy.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.bausucht.hunjy.Settings;
import net.bausucht.hunjy.manager.InventoryManager;
import net.bausucht.hunjy.utils.NBTModifier;
import net.bausucht.hunjy.utils.ScalableInventory;
import net.md_5.bungee.api.ChatColor;
import net.minesucht.friendsystem.FriendSystem;
import net.minesucht.friendsystem.user.FriendUser;
import net.minesucht.protocol.packet.bungeecord.BungeeCordExecuteCommand;
import net.minesucht.protocol.packet.friend.entry.FriendListEntry;
import net.minesucht.protocol.packet.ignore.IgnoreList;
import net.minesucht.protocol.packet.ignore.UnignorePlayer;
import net.minesucht.protocol.packet.ignore.entry.IgnoreListEntry;

public class InventoryClick implements Listener {

	public FriendSystem friendSystem = Settings.getInstance().getFriendSystem();
	public InventoryManager inventoryManager = Settings.getInstance().getInventoryManager();

	public HashMap<Player, ScalableInventory> scalableInventorys = inventoryManager.scalableInventorys;

	@EventHandler
	private void onInvClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getWhoClicked();

		ClickType click = event.getClick();
		Inventory open = event.getInventory();
		ItemStack item = event.getCurrentItem();

		FriendUser friendUser = friendSystem.getFriendUser(player.getUniqueId());
		if (open == null || item == null || friendUser == null)
			return;
		if (inventoryManager.customInventorys.containsKey(player) || scalableInventorys.containsKey(player)) {
			if (event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())) {
				if (click.equals(ClickType.LEFT)) {
					if (NBTModifier.hasNBTTag(item, "settings")) {
						event.setCancelled(true);
						event.setResult(Result.DENY);
						if (NBTModifier.getNBTTag(item, "settings").equals("chat_toggle")) {
							switch (item.getItemMeta().getDisplayName()) {
							case "§cDeaktiviert":
								player.chat("/chat off");
								break;
							case "§6Premium":
								player.chat("/chat Premium");
								break;
							case "§aRäuber":
								player.chat("/chat Räuber");
								break;
							case "§bUltra":
								player.chat("/chat Ultra");
								break;
							case "§cKing":
								player.chat("/chat King");
								break;
							case "§dSupreme":
								player.chat("/chat Supreme");
								break;
							case "§5YouTuber":
								player.chat("/chat YouTuber");
								break;
							case "§9Team":
								player.chat("/chat Team");
								break;
							case "§eFreunde":
								player.chat("/chat Freunde");
								break;
							default:
								break;
							}
							player.closeInventory();
							inventoryManager.openGeneralInventory(player);
						} else if (NBTModifier.getNBTTag(item, "settings").equals("time_toggle")) {
							switch (item.getItemMeta().getDisplayName()) {
							case "§aTag":
								player.chat("/settime day");
								inventoryManager.playerTimeChash.put(player, "day");
								break;
							case "§7Nacht":
								player.chat("/settime night");
								inventoryManager.playerTimeChash.put(player, "night");
								break;
							case "§cDeaktiviert":
								player.chat("/settime off");
								if (inventoryManager.playerTimeChash.containsKey(player))
									inventoryManager.playerTimeChash.remove(player);
								break;
							default:
								break;
							}

							player.closeInventory();
							inventoryManager.openGeneralInventory(player);
						} else if (NBTModifier.getNBTTag(item, "settings").equals("friends_toggle_private_msg")) {
							if (item.getItemMeta().getDisplayName().equals("§aAlle")) {
								friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
										friendUser.getUuid(), "friend", new String[] { "togglemessage", "ALL" }));
								friendUser.setSetting("message_retrieval", "ALL", false);
							} else if (item.getItemMeta().getDisplayName().equals("§dFreunde")) {
								friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
										friendUser.getUuid(), "friend", new String[] { "togglemessage", "FRIENDS" }));
								friendUser.setSetting("message_retrieval", "FRIENDS", false);
							} else if (item.getItemMeta().getDisplayName().equals("§cKeiner")) {
								friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
										friendUser.getUuid(), "friend", new String[] { "togglemessage", "NONE" }));
								friendUser.setSetting("message_retrieval", "NONE", false);
							}
							inventoryManager.openFriendInventory(player);
						}

						switch (NBTModifier.getNBTTag(item, "settings")) {
						case "general":
							inventoryManager.openGeneralInventory(player);
							break;
						case "plot":
							player.closeInventory();
							player.chat("/p");
							break;
						case "friends":
							if (Bukkit.getServer().getPluginManager().getPlugin("FriendSystem") != null) {
								if (Bukkit.getServer().getPluginManager().isPluginEnabled("FriendSystem")) {
									inventoryManager.openFriendInventory(player);
								}
							}
							break;
						case "general_toggle_muenzen":
							if (Bukkit.getServer().getPluginManager().getPlugin("CoinsAPI") != null) {
								if (Bukkit.getServer().getPluginManager().isPluginEnabled("CoinsAPI")) {
									player.chat("/togglecoins");
									inventoryManager.openGeneralInventory(player);
									inventoryManager.openGeneralInventory(player);
								}
							}
							break;
						case "general_toggle_scoreboard":
							player.chat("/scoreboard toggle");
							inventoryManager.openGeneralInventory(player);
							break;
						case "general_toggle_premiumchat":
							if (Bukkit.getServer().getPluginManager().getPlugin("premiumchat") != null) {
								if (Bukkit.getServer().getPluginManager().isPluginEnabled("premiumchat")) {
									player.closeInventory();
									player.chat("/defaultcolor");
								}
							}
							break;
						case "general_toggle_onlinetime":
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
									friendUser.getUuid(), "toggleonlinetime", new String[0]));
							if (friendUser.getSetting("allow_see_onlinetime").equals("false")) {
								friendUser.setSetting("allow_see_onlinetime", "true", false);
							} else {
								friendUser.setSetting("allow_see_onlinetime", "false", false);
							}
							inventoryManager.openGeneralInventory(player);
							break;
						case "general_toggle_time":
							inventoryManager.openTimeInventory(player);
							break;
						case "general_toggle_support":
							player.chat("/togglesupport");
							inventoryManager.openGeneralInventory(player);
							break;
						case "general_toggle_tpa":
							player.chat("/tptoggle");
							inventoryManager.openGeneralInventory(player);
							break;
						case "general_toggle_weather":
							player.chat("/toggleweather");
							inventoryManager.openGeneralInventory(player);
							break;
						case "general_toggle_chat":
							if (Bukkit.getServer().getPluginManager().getPlugin("premiumchat") != null) {
								if (Bukkit.getServer().getPluginManager().isPluginEnabled("premiumchat")) {
									player.closeInventory();
									inventoryManager.openChatInventory(player);
								}
							}
							break;
						case "back_to_main":
							inventoryManager.openMainInventory(player);
							break;
						case "friend_toggle_requests":
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
									friendUser.getUuid(), "friend", new String[] { "toggle" }));
							if (friendUser.getSetting("allow_friend_requests").equals("false")) {
								friendUser.setSetting("allow_friend_requests", "true", true);
							} else {
								friendUser.setSetting("allow_friend_requests", "false", false);
							}
							inventoryManager.openFriendInventory(player);
							break;
						case "friend_toggle_msg":
							player.closeInventory();
							inventoryManager.openFriendMessagesInventory(player);
							break;
						case "friend_toggle_on/off_msg":
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
									friendUser.getUuid(), "friend", new String[] { "togglenotify" }));
							if (friendUser.getSetting("allow_join_leave_messages").equals("false")) {
								friendUser.setSetting("allow_join_leave_messages", "true", true);
							} else {
								friendUser.setSetting("allow_join_leave_messages", "false", false);
							}
							inventoryManager.openFriendInventory(player);
							break;
						case "open_requests":
							inventoryManager.openRequests(player);
							break;
						case "open_ignore":
							inventoryManager.openIgnoredPlayers(player);
							break;
						case "open_friendslist":
							inventoryManager.openFriendsInventory(player);
							break;
						case "back_to_friends":
							inventoryManager.openFriendInventory(player);
							break;
						case "friends_open_friendinfo":
							inventoryManager.openFriendSettingsInventory(player, item);
							break;
						case "friends_accept_all":
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
									friendUser.getUuid(), "friend", new String[] { "acceptall" }));
							inventoryManager.openRequests(player);
							break;
						case "friends_deny_all":
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
									friendUser.getUuid(), "friend", new String[] { "denyall" }));
							inventoryManager.openRequests(player);
							break;
						case "friends_open_request":
							inventoryManager.openFriendPlayerInventory(player, item);
							break;
						case "page_next":
							scalableInventorys.get(player).nextPage(player);
							break;
						case "page_back":
							scalableInventorys.get(player).lastPage(player);
							break;
						case "friends_unignore_all":
							for (IgnoreListEntry ignoreListEntry : friendUser.getIgnoreList()) {
								UnignorePlayer unignorePlayer = new UnignorePlayer();
								unignorePlayer.setUuid(player.getUniqueId());
								unignorePlayer.setWho(ignoreListEntry.getName());
								friendSystem.getMineAPI().getMineAPIClient().sendPacket(unignorePlayer);
							}
							IgnoreList ignoreList = new IgnoreList();
							ignoreList.setUuid(player.getUniqueId());
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(ignoreList);
							inventoryManager.openIgnoredPlayers(player);
							break;
						case "friends_open_ignore":
							player.closeInventory();
							inventoryManager.openIgnoredPlayerInventory(player, item);
							break;
						case "friends_unignore_confirm":
							UnignorePlayer packet = new UnignorePlayer();
							packet.setUuid(player.getUniqueId());
							packet.setWho(open.getTitle().replace("§8Ignorierter Spieler: §c", ""));
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(packet);
							IgnoreList ignoreList1 = new IgnoreList();
							ignoreList1.setUuid(player.getUniqueId());
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(ignoreList1);

							inventoryManager.updatePlayer(player);
							inventoryManager.openFriendInventory(player);
							break;
						case "friends_unignore_cancle":
							inventoryManager.openIgnoredPlayers(player);
							break;
						case "friends_request_confirm":

							friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
									friendUser.getUuid(), "friend", new String[] { "accept",
											ChatColor.stripColor(open.getName()).replace("Anfrage von: ", "") }));
							inventoryManager.updatePlayer(player);
							inventoryManager.openFriendInventory(player);
							break;
						case "friends_request_deny":
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(
									new BungeeCordExecuteCommand(friendUser.getUuid(), "friend", new String[] { "deny",
											ChatColor.stripColor(open.getName()).replace("Anfrage von: ", "") }));

							inventoryManager.updatePlayer(player);
							inventoryManager.openFriendInventory(player);
							break;
						case "friends_remove_friend":
							for (FriendListEntry friendListEntry : friendUser.getFriendList()) {
								if (open.getName().equals(
										"§8Freund: " + friendListEntry.getColor() + friendListEntry.getName())) {
									if (item != null) {
										if (item.getType().equals(Material.BARRIER)) {
											friendSystem.getMineAPI().getMineAPIClient()
													.sendPacket(new BungeeCordExecuteCommand(friendUser.getUuid(),
															"friend",
															new String[] { "remove", friendListEntry.getName() }));
											inventoryManager.updatePlayer(player);
											inventoryManager.openFriendInventory(player);
										}
									}
								}
							}
							break;
						case "bacak_to_friends":
							inventoryManager.openFriendsInventory(player);
							break;
						case "friends_list_clear":
							inventoryManager.openClearFriendList(player, item);
							break;
						case "friends_list_clear_confirm":
							friendSystem.getMineAPI().getMineAPIClient().sendPacket(new BungeeCordExecuteCommand(
									friendUser.getUuid(), "friend", new String[] { "clear" }));
							player.closeInventory();
							break;
						case "friends_list_clear_deny":
							inventoryManager.openFriendInventory(player);
							break;
						default:
							break;
						}
					}
				}
				event.setCancelled(true);
				event.setResult(Result.DENY);
			}
		}
	}
}

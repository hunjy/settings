package net.bausucht.hunjy.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ScalableInventory {

	Inventory inv;
	String title;
	int inventorySize;
	
	int page;
	Map<Integer, ItemStack> itemStackMap;
	
	public ScalableInventory() {
	}
	
	public ScalableInventory(String title) {
		this.title = title;
		this.inventorySize = 54;
		page = 1;
		itemStackMap = new HashMap<>();
	}
	
	public void loadItem(int i, ItemStack item) {
		itemStackMap.put(i, item);
	}

	public void loadItem(int i, ItemStack item, String key, String value) {
		itemStackMap.put(i, NBTModifier.setNBTTag(item, key, value));
	}
	private int getMaxPage() {
		int maxPage = itemStackMap.size() / (inventorySize-9);
	if (itemStackMap.size() % (inventorySize-9) != 0)
		maxPage += 1;
	if (maxPage == 0)
		maxPage += 1;
	return maxPage;
	}
	
	public void setItems() {
		for (int i = (page-1)*(inventorySize-9); i < (inventorySize-9) * page; i++) {
			if(itemStackMap.get(i) != null)
				inv.addItem(itemStackMap.get(i));
		}
		if(itemStackMap.size() >= (page * (inventorySize-9))) {
			setItem(50, new ItemBuilder(Material.PAPER).setDisplayName("§e>>").addLore("§fNächste Seite").build(), "settings", "page_next");
		}
		if (page > 1) {
			setItem(48, new ItemBuilder(Material.PAPER).setDisplayName("§e<<").addLore("§fVorherige Seite").build(), "settings", "page_back");
		}
		setItem(53, new ItemBuilder(Material.INK_SACK, (short)1).setDisplayName("§9Zurück").build(), "settings", "back_to_friends");
		setItem(49, new ItemBuilder(Material.NAME_TAG).setDisplayName("§aSeite " + page + "/" + getMaxPage()).build());
		
		if(title.startsWith("§8Freundschaftsanfragen")) {
			setItem(45, new ItemBuilder(Material.WOOL, (short)5).setDisplayName("§aAlle annehmen").build(), "settings", "friends_accept_all");
			setItem(46, new ItemBuilder(Material.WOOL, (short)14).setDisplayName("§cAlle ablehnen").build(), "settings", "friends_deny_all");
		}else if(title.startsWith("§8Ignorierte Spieler")) {
			setItem(45, new ItemBuilder(Material.WOOL, (short)5).setDisplayName("§aAlle freigeben").build(), "settings", "friends_unignore_all");
		}
		
	}
	
	public void setItem(int i, ItemStack item) {
		inv.setItem(i, item);
	}
	
	public void setItem(int i, ItemStack item, String key, String value) {
		inv.setItem(i, NBTModifier.setNBTTag(item, key, value));
	}
	
	
	public void nextPage(Player player) {
		page +=1;
		openInventory(player);
	}

	public void lastPage(Player player) {
		page -=1;
		openInventory(player);
	}
	
	public void openInventory(Player player) {
		inv = Bukkit.createInventory(null, inventorySize, title);
		setItems();
		player.openInventory(inv);
	}
	
}

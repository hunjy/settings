package net.bausucht.hunjy.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CustomInventory{

	Inventory inventory;
	String title;
	int size;
	
	public CustomInventory(String title, int size) {
		this.title = title;
		this.size = size;
		inventory = Bukkit.createInventory(null, size, title);
	}
	
	public void setItem(int slot, ItemStack item) {
		inventory.setItem(slot, item);
	}
	

	public void setItemWithNBT(int slot, ItemStack item, String key, String value) {
		inventory.setItem(slot, NBTModifier.setNBTTag(item, key, value));
	}
	
	public void openInventory(Player player) {
		player.openInventory(inventory);
	}
	
	
}

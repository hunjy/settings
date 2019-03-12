package net.bausucht.hunjy.utils;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

	private ItemStack item;
	private ItemMeta meta;

	public ItemBuilder(Material matrial) {
		item = new ItemStack(matrial);
		meta = item.getItemMeta();
	}

	public ItemBuilder(Material matrial, int amount) {
		item = new ItemStack(matrial, amount);
		meta = item.getItemMeta();
	}

	public ItemBuilder(Material matrial, short subid) {
		item = new ItemStack(matrial, 1, subid);
		meta = item.getItemMeta();
	}

	public ItemBuilder removeAllAtributs() {
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return this;
	}

	public ItemBuilder setDisplayName(String name) {
		meta.setDisplayName(name);
		return this;
	}

	public ItemBuilder addLore(String lore) {
		ArrayList<String> list = new ArrayList<>();

		if (lore.contains("\n")) {
			for (String current : lore.split("\n")) {
				list.add(current);
			}
		} else {
			list.add(lore);
		}

		meta.setLore(list);

		return this;
	}

	public ItemBuilder addLore(ArrayList<String> lore) {
		ArrayList<String> editLore = new ArrayList<>();
		for (String current : lore) {
			editLore.add("§f"
					+ current.replaceAll("ue", "ü").replaceAll("oe", "ö").replaceAll("ae", "ä").replaceAll("&", "§"));
		}
		meta.setLore(editLore);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		meta.addEnchant(enchantment, level, true);
		return this;
	}

	public ItemStack build() {
		item.setItemMeta(meta);
		return item;
	}

}

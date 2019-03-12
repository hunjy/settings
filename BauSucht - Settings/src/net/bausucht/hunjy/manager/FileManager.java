package net.bausucht.hunjy.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import net.bausucht.hunjy.Settings;
import net.bausucht.hunjy.utils.ItemBuilder;

public class FileManager {

	private File file;
	private YamlConfiguration config;

	public ArrayList<ItemStack> items;

	public FileManager() {
		file = new File(Settings.getInstance().getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(file);

		if (!config.contains("Inventory")) {
			loadDefaults();
		}

		items = new ArrayList<>();
	}

	public ItemStack loadItem(String item) {
		ArrayList<String> lore;
		String name, display, color;
		int subid;
		name = item.replaceAll("ue", "�").replaceAll("oe", "�").replaceAll("ae", "�");
		display = config.getString("Inventory." + item + ".Display");
		lore = new ArrayList<>(config.getStringList("Inventory." + item + ".Lore"));
		color = config.getString("Inventory." + item + ".Color");
		subid = config.getInt("Inventory." + item + ".SubID");

		if (name != null && display != null) {
			return new ItemBuilder(Material.valueOf(display), (short) subid)
					.setDisplayName(color.replaceAll("&", "�") + name).addLore(lore).removeAllAtributs().build();
		}
		return null;
	}

	public void loadDefaults() {
		saveItems(new ItemBuilder(Material.GOLD_INGOT).setDisplayName("�6M�nzen")
				.addLore("�fErm�glicht die Einstellung wer deine M�nzen sehen kann").build());
		saveItems(new ItemBuilder(Material.PAINTING).setDisplayName("�eScoreboard")
				.addLore("�fErm�glicht das Scoreboard zu\n�fverstecken und auch anzuzeigen").build());
		saveItems(new ItemBuilder(Material.SIGN).setDisplayName("�dStandardfarbe")
				.addLore("�f�ndert deine Schriftfarbe,\n�fdie standardm��ig im Chat\n�fverwendet wird").build());
		saveItems(new ItemBuilder(Material.WATCH).setDisplayName("�bOnlinezeit")
				.addLore("�fErm�glicht die Einstellung wer deine Onlinezeit sehen kann").build());
		saveItems(new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("�2Tag/Nacht")
				.addLore("�fErm�glicht den Wechsel zwischen Tag und Nacht").build());
		saveItems(new ItemBuilder(Material.PAPER).setDisplayName("�aSupportanzeige").addLore(
				"�fErm�glicht die Verwaltung der Sichtbarkeit,\n�fob man die Anzeige m�chten,\n�fdass sich Helfer im Support befinden oder nicht")
				.build());
		saveItems(new ItemBuilder(Material.COMPASS).setDisplayName("�cNear")
				.addLore("�fErm�glicht die Einstellung,\n�fwie die Ausgabe von /near erfolgt").build());
		saveItems(new ItemBuilder(Material.ENDER_PEARL).setDisplayName("�bTPA-Anfragen")
				.addLore("�fErm�glicht die Einstellung,\n�fob TPA-Anfragen zugelassen werden oder nicht").build());
		saveItems(new ItemBuilder(Material.GHAST_TEAR).setDisplayName("�eSonne/Regen")
				.addLore("�fErm�glicht den Wechsel zwischen Sonne und Regen").build());
		saveItems(new ItemBuilder(Material.NAME_TAG).setDisplayName("�3Chat-Filter")
				.addLore("&fErm�glicht die Einstellung,\n�fab welchem Rang man\n�fNachrichten empfangen m�chte").build());
	}

	@SuppressWarnings("deprecation")
	public void saveItems(ItemStack item) {

		String color = item.getItemMeta().getDisplayName().substring(0, 2).replace("�", "&");
		String name = item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length())
				.replaceAll("�", "ue").replaceAll("�", "ae").replaceAll("�", "oe");

		ArrayList<String> lore = new ArrayList<>();

		for (String current : item.getItemMeta().getLore()) {
			lore.add(current.replace("�", "&").replaceAll("�", "ue").replaceAll("�", "ae").replaceAll("�", "oe"));
		}

		config.set("Inventory." + name + ".Lore", lore);
		config.set("Inventory." + name + ".Color", color);
		config.set("Inventory." + name + ".Display", item.getType().toString());
		config.set("Inventory." + name + ".SubID", item.getData().getData());

		save();
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ItemStack> getItems() {
		return items;
	}

	public YamlConfiguration getConfig() {
		return config;
	}

}

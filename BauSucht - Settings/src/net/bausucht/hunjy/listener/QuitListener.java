package net.bausucht.hunjy.listener;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.bausucht.hunjy.Settings;
import net.bausucht.hunjy.manager.InventoryManager;
import net.bausucht.hunjy.utils.ScalableInventory;

public class QuitListener implements Listener {

	public InventoryManager inventoryManager = Settings.getInstance().getInventoryManager();

	public HashMap<Player, ScalableInventory> scalableInventorys = inventoryManager.scalableInventorys;
	@EventHandler
	private void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (Settings.getInstance().getInventoryManager().playerTimeChash.containsKey(player))
			Settings.getInstance().getInventoryManager().playerTimeChash.remove(player);
		if (inventoryManager.customInventorys.containsKey(player)) {
			inventoryManager.customInventorys.remove(player);
		}else if(scalableInventorys.containsKey(player)) {
			scalableInventorys.remove(player);
		}
	}

}

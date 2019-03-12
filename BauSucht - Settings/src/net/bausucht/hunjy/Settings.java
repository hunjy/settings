package net.bausucht.hunjy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.bausucht.hunjy.command.openInventory;
import net.bausucht.hunjy.listener.InventoryClick;
import net.bausucht.hunjy.listener.QuitListener;
import net.bausucht.hunjy.manager.FileManager;
import net.bausucht.hunjy.manager.InventoryManager;
import net.minesucht.friendsystem.FriendSystem;
import net.minesucht.mineapi.MineAPI;

public class Settings extends JavaPlugin {

	public static Settings instance;

	public InventoryManager inventoryManager;
	public FileManager fileManager;
	public FriendSystem friendSystem;

	private MineAPI mineAPI;

	@Override
	public void onEnable() {
		instance = this;

		fileManager = new FileManager();
		inventoryManager = new InventoryManager();
		friendSystem = (FriendSystem) this.getServer().getPluginManager().getPlugin("FriendSystem");

		this.mineAPI = (MineAPI) this.getServer().getPluginManager().getPlugin("MineAPI");

		this.getCommand("settings").setExecutor(new openInventory());

		Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
		Bukkit.getPluginManager().registerEvents(new QuitListener(), this);

	}

	public void executeBungeecordCommand(String command, Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ExecuteBungeeCommand");
		out.writeUTF(command);

		player.sendPluginMessage(this, "YourChannel", out.toByteArray());
	}

	public static Settings getInstance() {
		return instance;
	}

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public FriendSystem getFriendSystem() {
		return friendSystem;
	}

	public MineAPI getMineAPI() {
		return mineAPI;
	}

}

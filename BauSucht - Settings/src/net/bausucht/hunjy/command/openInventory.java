package net.bausucht.hunjy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.bausucht.hunjy.Settings;
import net.minesucht.friendsystem.user.FriendUser;
import net.minesucht.protocol.packet.friend.FriendRequest;
import net.minesucht.protocol.packet.ignore.IgnoreList;

public class openInventory implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;
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
		return true;
	}

}

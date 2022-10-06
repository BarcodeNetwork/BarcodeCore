package net.Indyuce.mmocore.api.util.input;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.util.Consumer;

import net.Indyuce.mmocore.MMOCore;

public class ChatInput extends PlayerInput {
	public ChatInput(Player player, InputType type, Consumer<String> output) {
		super(player, output);

		player.closeInventory();
		MMOCore.plugin.configManager.getSimpleMessage("player-input.chat." + type.getLowerCaseName()).send(player);
		MMOCore.plugin.configManager.getSimpleMessage("player-input.chat.cancel").send(player);
	}

	@Override
	public void close() {
		AsyncPlayerChatEvent.getHandlerList().unregister(this);
		InventoryOpenEvent.getHandlerList().unregister(this);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void a(AsyncPlayerChatEvent event) {
		if (event.getPlayer().equals(getPlayer())) {
			close();
			event.setCancelled(true);

			if (!event.getMessage().equals("취소"))
				Bukkit.getScheduler().scheduleSyncDelayedTask(MMOCore.plugin, () -> output(event.getMessage()));
		}
	}

	@EventHandler
	public void b(InventoryOpenEvent event) {
		if (event.getPlayer().equals(getPlayer()))
			close();
	}
}

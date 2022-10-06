package net.Indyuce.mmocore.listener;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.manager.ConfigManager;
import net.Indyuce.mmocore.manager.SoundManager;
import net.Indyuce.mmocore.skill.Skill.SkillInfo;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SpellCast implements Listener {

    @EventHandler
    public void a(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ConfigManager.SwapAction action = player.isSneaking() ? MMOCore.plugin.configManager.sneakingSwapAction : MMOCore.plugin.configManager.normalSwapAction;

        // Vanilla action does nothing
        if (action == ConfigManager.SwapAction.VANILLA)
            return;

        // Always cancel event if it's not the vanilla action
        event.setCancelled(true);

        /*
         * Hotbar swap feature, this entirely switches the
         * player's hotbar with their 9 lowest inventory slots
         */
        if (action == ConfigManager.SwapAction.HOTBAR_SWAP) {
            MMOCore.plugin.soundManager.play(player, SoundManager.SoundEvent.HOTBAR_SWAP);
            for (int j = 0; j < 9; j++) {
                ItemStack replaced = player.getInventory().getItem(j + 9 * 3);
                player.getInventory().setItem(j + 9 * 3, player.getInventory().getItem(j));
                player.getInventory().setItem(j, replaced);
            }
            return;
        }

        // Enter spell casting
        PlayerData playerData = PlayerData.get(player);
        if (player.getGameMode() != GameMode.SPECTATOR
                && (MMOCore.plugin.configManager.canCreativeCast || player.getGameMode() != GameMode.CREATIVE)
                && !playerData.isCasting()
                && playerData.getBoundSkills().size() > 0) {
            playerData.skillCasting = new SkillCasting(playerData);
            MMOCore.plugin.soundManager.play(player, SoundManager.SoundEvent.SPELL_CAST_BEGIN);
        }
    }

    public class SkillCasting extends BukkitRunnable implements Listener {
        private final PlayerData playerData;

        private final String ready = MMOCore.plugin.configManager.getSimpleMessage("casting.action-bar.ready").message();
        private final String onCooldown = MMOCore.plugin.configManager.getSimpleMessage("casting.action-bar.on-cooldown").message();
        private final String noMana = MMOCore.plugin.configManager.getSimpleMessage("casting.action-bar.no-mana").message();
        private final String split = MMOCore.plugin.configManager.getSimpleMessage("casting.split").message();

        private int j;

        public SkillCasting(PlayerData playerData) {
            this.playerData = playerData;

            Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
            runTaskTimer(MMOCore.plugin, 0, 1);
        }

        @EventHandler()
        public void onSkillCast(PlayerItemHeldEvent event) {
            Player player = event.getPlayer();
            if (!playerData.isOnline()) return;
            if (!event.getPlayer().equals(playerData.getPlayer()))
                return;

            /*
             * when the event is cancelled, another playerItemHeldEvent is
             * called and previous and next slots are equal. the event must not
             * listen to that non-player called event.
             */
            if (event.getPreviousSlot() == event.getNewSlot())
                return;

            event.setCancelled(true);
            int slot = event.getNewSlot() + (event.getNewSlot() >= player.getInventory().getHeldItemSlot() ? -1 : 0);

            /*
             * the event is called again soon after the first since when
             * the event is called again soon after the first since when
             * cancelling the first one, the player held item slot must go back
             * to the previous one.
             */
            if (slot >= 0 && playerData.hasSkillBound(slot))
                playerData.cast(playerData.getBoundSkill(slot));
        }

        @EventHandler
        public void stopCasting(PlayerSwapHandItemsEvent event) {
            Player player = event.getPlayer();
            ConfigManager.SwapAction action = player.isSneaking()
                    ? MMOCore.plugin.configManager.sneakingSwapAction
                    : MMOCore.plugin.configManager.normalSwapAction;
            if (action != ConfigManager.SwapAction.SPELL_CAST || !playerData.isOnline()) return;
            if (event.getPlayer().equals(playerData.getPlayer())) {
                MMOCore.plugin.soundManager.play(player, SoundManager.SoundEvent.SPELL_CAST_END);
                //MMOCore.plugin.configManager.getSimpleMessage("casting.no-longer").send(playerData.getPlayer());
                close();
            }
        }

        public void close() {
            playerData.skillCasting = null;
            HandlerList.unregisterAll(this);
            cancel();
        }

        public String getFormat(PlayerData data) {
            StringBuilder str = new StringBuilder();
            if (!data.isOnline()) return str.toString();
            for (int j = 0; j < data.getBoundSkills().size(); j++) {
                SkillInfo skill = data.getBoundSkill(j);
                str.append((str.length() == 0) ? "" : split).append((onCooldown(data, skill)
                        ? onCooldown.replace("{cooldown}", String.valueOf(data.getCooldownMap().getInfo(skill.getSkill()).getRemaining() / 1000))
                        : noMana(data, skill) ? noMana : ready).replace("{index}", "" + (j + 1 + (data.getPlayer().getInventory().getHeldItemSlot()
                        <= j ? 1 : 0))).replace("{skill}", data.getBoundSkill(j).getSkill().getName()));
            }

            return str.toString();
        }

        /**
         * We don't even need to check if the skill has the 'cooldown'
         * modifier. We just look for an entry in the cooldown map which
         * won't be here if the skill has no cooldown.
         */
        private boolean onCooldown(PlayerData data, SkillInfo skill) {
            return data.getCooldownMap().isOnCooldown(skill.getSkill());
        }

        private boolean noMana(PlayerData data, SkillInfo skill) {
            return skill.getSkill().hasModifier("mana") && skill.getModifier("mana", data.getSkillLevel(skill.getSkill())) > data.getMana();
        }

        @Override
        public void run() {
            if (!playerData.isOnline() || playerData.getPlayer().isDead()) {
                close();
                return;
            }

            //if (j % 20 == 0)
            //    playerData.displayActionBar(getFormat(playerData));

//            for (int k = 0; k < 2; k++) {
//                double a = (double) j++ / 5;
//                playerData.getProfess().getCastParticle()
//                        .display(playerData.getPlayer().getLocation().add(Math.cos(a), 1 + Math.sin(a / 3) / 1.3, Math.sin(a)));
//            }
        }
    }
}

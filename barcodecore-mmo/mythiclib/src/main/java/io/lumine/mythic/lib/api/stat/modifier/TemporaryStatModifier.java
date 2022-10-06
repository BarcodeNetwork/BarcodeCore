package io.lumine.mythic.lib.api.stat.modifier;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.StatInstance;
import org.bukkit.scheduler.BukkitRunnable;

public class TemporaryStatModifier extends StatModifier implements Closable {
    private final BukkitRunnable runnable;

    /**
     * Used to register a stat modifier which will be unregistered from the stat
     * instance after a set period of time. As of MythicLib 1.2.8 it is not used
     * anywhere in MMOCore or MMOItems and is only there as extra public API
     * stuff
     *
     * @param d
     *            The stat modifier value
     * @param duration
     *            The duration of this stat modifier
     * @param type
     *            The stat modifier type (relative/flat)
     * @param key
     *            The string key of the stat
     * @param ins
     *            The StatInstance the modifier must be registered in
     */
    public TemporaryStatModifier(double d, long duration, ModifierType type, EquipmentSlot slot, ModifierSource source, String key, StatInstance ins) {
        super(d, type, slot, source);

        (runnable = new BukkitRunnable() {
            public void run() {
                ins.remove(key);
            }
        }).runTaskLater(MythicLib.plugin, duration);
    }

    /**
     * @deprecated Use the constructor with an EquipmentSlot and ModifierSource instead.
     */
    @Deprecated
    public TemporaryStatModifier(double d, long duration, ModifierType type, String key, StatInstance ins) {
        this(d, duration, type, EquipmentSlot.OTHER, ModifierSource.OTHER, key, ins);
    }

    @Override
    public void close() {
        if (!runnable.isCancelled())
            runnable.cancel();
    }
}

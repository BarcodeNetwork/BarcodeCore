package io.lumine.mythic.lib.api.stat.provider;

import io.lumine.mythic.lib.api.item.NBTItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * This class can be used to cache the equipment from
 * a mob and read its statistics like Defense, Elemental
 * defense or mitigation stats.
 * <p>
 * This way, mechanics which were initially limited to
 * players can now be generalized to any monster
 * <p>
 * TODO
 * Implement the following mecanics so that they also
 * work on monsters
 * - Damage mitigation
 * - Critical strikes
 *
 * @author indyuce
 */
public class EntityStatProvider implements StatProvider {
    private final Set<NBTItem> equipment = new HashSet<>();

    public EntityStatProvider(LivingEntity entity) {
        for (ItemStack equipped : entity.getEquipment().getArmorContents())
            equipment.add(NBTItem.get(equipped));
        equipment.add(NBTItem.get(entity.getEquipment().getItemInMainHand()));
        equipment.add(NBTItem.get(entity.getEquipment().getItemInOffHand()));
    }

    @Override
    public double getStat(String id) {
        double d = 0;

        for (NBTItem nbt : equipment)
            d += nbt.getStat(id);

        return 0;
    }
}

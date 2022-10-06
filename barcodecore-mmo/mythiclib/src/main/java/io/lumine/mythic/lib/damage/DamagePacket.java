package io.lumine.mythic.lib.damage;

import java.util.Set;

/**
 * The NEXT LEVEL damage system. A damage packet is a damage weighted
 * by damage types. Why not simply having ONE double value for the entire
 * AttackMetadata?
 * <p>
 * For instance, a melee sword attack would add one physical-weapon damage packet.
 * Then, casting an on-hit ability like Starfall would add an extra magic-skill
 * damage packet, independently of the packet that is already there.
 * <p>
 * Then, if we were to apply the 'Melee Damage' stat, it should only apply
 * on the damage packet that is due to the weapon attack. Damage packets help
 * divide an attack into multiple parts that can be independently modified.
 *
 * @author mmolover
 */
public class DamagePacket implements Cloneable {
    private final DamageType[] types;

    private double value;

    @Deprecated
    public DamagePacket(double value, Set<DamageType> types) {
        this(value, types.toArray(new DamageType[0]));
    }

    public DamagePacket(double value, DamageType... types) {
        this.value = value;
        this.types = types;
    }

    public double getValue() {
        return value;
    }

    public DamageType[] getTypes() {
        return types;
    }

    /**
     * @return Checks if the current packet has that damage type
     */
    public boolean hasType(DamageType type) {
        for (DamageType checked : this.types)
            if (checked == type)
                return true;
        return false;
    }

    public void addValue(double value) {
        this.value += value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void multiplyValue(double coef) {
        this.value *= coef;
    }

    @Override
    public DamagePacket clone() {
        return new DamagePacket(value, types);
    }
}

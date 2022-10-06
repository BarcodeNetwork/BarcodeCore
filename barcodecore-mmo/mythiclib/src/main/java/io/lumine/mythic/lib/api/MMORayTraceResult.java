package io.lumine.mythic.lib.api;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

public class MMORayTraceResult {
    private final LivingEntity entity;
    private final double range;

    /***
     * Used after using ray tracing based skills or weapons to make it easier to
     * identify if it hit a target or not. Hit block is not important and should
     * only prevent from targeting entities hidden behind walls
     *
     * @param entity The entity that was hit
     * @param range The ranged that was used
     */
    public MMORayTraceResult(LivingEntity entity, double range) {
        this.range = range;
        this.entity = entity;
    }

    public boolean hasHit() {
        return entity != null;
    }

    public LivingEntity getHit() {
        return entity;
    }

    public double getRange() {
        return range;
    }

    /***
     * Draws a line of redstone particle around a line
     *
     * @param source
     *            The initial location
     * @param vec
     *            The direction
     * @param c
     *            Multiplicate constant for the ray trace range
     * @param color
     *            Redstone color
     */
    public void draw(Location source, Vector vec, double c, Color color) {
        draw(source, vec, c, loc -> loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, new Particle.DustOptions(Color.RED, 1)));
    }

    /***
     * Draws something along a line
     *
     * @param loc
     *            The initial location
     * @param vec
     *            The direction
     * @param c
     *            Multiplicate constant for the ray trace range
     * @param tick
     *            Action performed on every step
     */
    public void draw(Location loc, Vector vec, double c, Consumer<Location> tick) {
        vec = vec.multiply(1 / c);
        for (int j = 0; j < range * c; j++)
            tick.accept(loc.add(vec));
    }
}

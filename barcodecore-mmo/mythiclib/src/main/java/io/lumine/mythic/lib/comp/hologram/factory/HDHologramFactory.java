package io.lumine.mythic.lib.comp.hologram.factory;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.comp.hologram.MMOItemsHologram;
import io.lumine.utils.holograms.Hologram;
import io.lumine.utils.holograms.HologramFactory;
import io.lumine.utils.serialize.Position;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Handles compatibility with HolographicDisplays using HologramFactory from LumineUtils.
 * <p>
 * HolographicDisplays is by far the best hologram plugin we
 * can use to display indicators.
 *
 * @author indyuce
 */
public class HDHologramFactory implements HologramFactory {

   /* public void displayIndicator(Location loc, String format, Player player) {
        Hologram hologram = HologramsAPI.createHologram(MythicLib.plugin, loc);
        hologram.appendTextLine(format);
        if (player != null)
            hologram.getVisibilityManager().hideTo(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(MythicLib.plugin, hologram::delete, 20);
    }*/

    @NotNull
    @Override
    public Hologram newHologram(@NotNull Position position, @NotNull List<String> list) {
        return new HDHologram(position, list);
    }

    public class HDHologram extends MMOItemsHologram {
        private final com.gmail.filoghost.holographicdisplays.api.Hologram holo;
        private final List<String> lines;

        public HDHologram(Position position, List<String> list) {
            holo = HologramsAPI.createHologram(MythicLib.plugin, position.toLocation());
            this.lines = list;
            for (String line : lines)
                holo.appendTextLine(line);
        }

        @Override
        public List<String> getLines() {
            return lines;
        }

        @Override
        public void updateLines(@NotNull List<String> list) {
            throw new RuntimeException("Adding lines is not supported");
        }

        @Override
        public Position getPosition() {
            return Position.of(holo.getLocation());
        }

        @Override
        public void updatePosition(@NotNull Position position) {
            holo.teleport(position.toLocation());
        }

        @Override
        public void despawn() {
            super.despawn();

            holo.delete();
        }
    }
}

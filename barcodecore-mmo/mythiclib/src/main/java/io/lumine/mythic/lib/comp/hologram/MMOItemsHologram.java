package io.lumine.mythic.lib.comp.hologram;

import io.lumine.utils.gson.JsonBuilder;
import io.lumine.utils.gson.JsonObject;
import io.lumine.utils.holograms.Hologram;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public abstract class MMOItemsHologram implements Hologram {
    private boolean spawned = true;

    public abstract List<String> getLines();

    @Nonnull
    public JsonObject serialize() {
        return JsonBuilder.object().add("position", getPosition()).add("lines", JsonBuilder.array().addStrings(getLines()).build()).build();
    }

    @Override
    public void spawn() {
        throw new RuntimeException("Hologram spawns on class instanciation");
    }

    @Override
    public void setClickCallback(@Nullable Consumer<Player> consumer) {
        // Not supported
    }

    @Override
    public void close() throws Exception {
        despawn();
    }

    @Override
    public void despawn() {
        Validate.isTrue(spawned, "Hologram despawned already");

        spawned = false;
    }

    @Override
    public boolean isSpawned() {
        return !hasTerminated();
    }

    @Override
    public boolean isClosed() {
        return hasTerminated();
    }

    @Override
    public boolean hasTerminated() {
        return !spawned;
    }
}

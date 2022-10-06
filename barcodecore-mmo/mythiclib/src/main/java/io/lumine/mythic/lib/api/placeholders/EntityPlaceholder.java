package io.lumine.mythic.lib.api.placeholders;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A mythic placeholder that provides information on
 * any entity. There is usually a way to get it from
 * Players using PAPI but this works on everything.
 *
 * @author Gunging
 */
public class EntityPlaceholder implements MythicPlaceholder {
    @NotNull
    @Override
    public String getAuthorName() { return "gunging"; }

    @NotNull
    @Override
    public String getMythicIdentifier() { return "entity"; }

    @Nullable
    @Override
    public String parse(@NotNull String text, @NotNull Object thing) {

        // Cast into usable type
        Entity asEntity = (Entity) thing;

        // What is it asking from us?
        switch (text) {
            case "type": return asEntity.getType().toString();
            case "name": return asEntity.getName();
            case "uuid": return asEntity.getUniqueId().toString();
            case "world": return asEntity.getLocation().getWorld() == null ? asEntity.getLocation().getWorld().getName() : "null";
            case "x": return String.valueOf(asEntity.getLocation().getX());
            case "y": return String.valueOf(asEntity.getLocation().getY());
            case "z": return String.valueOf(asEntity.getLocation().getZ()); }

        // Unsupported I guess
        return null;
    }

    @Override
    public boolean forUseWith(@NotNull Object obj) { return (obj instanceof Entity); }
}

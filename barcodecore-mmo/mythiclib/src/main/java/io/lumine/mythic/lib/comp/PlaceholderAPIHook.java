package io.lumine.mythic.lib.comp;

import io.lumine.mythic.lib.api.placeholders.MythicPlaceholder;
//todo import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A mythic placeholder that just passes on to PAPI
 * to do all the parsing.
 *
 * @author Gunging
 */
public class PlaceholderAPIHook implements MythicPlaceholder {
    @NotNull
    @Override
    public String getAuthorName() { return "gunging"; }

    @NotNull @Override public String getMythicIdentifier() { return ""; }

    @Nullable @Override public String parse(@NotNull String text, @NotNull Object thing) { return ""; }
    //@Nullable @Override public String parse(@NotNull String text, @NotNull Object thing) { return PlaceholderAPI.setPlaceholders((OfflinePlayer) thing, text); }

    @Override public boolean forUseWith(@NotNull Object obj) { return (obj instanceof OfflinePlayer); }
}

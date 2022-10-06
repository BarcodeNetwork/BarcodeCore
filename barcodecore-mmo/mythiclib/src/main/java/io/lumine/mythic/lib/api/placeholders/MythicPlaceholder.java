package io.lumine.mythic.lib.api.placeholders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * This is basically PlaceholderAPI's 'Placeholder Expansion' class,
 * has a few less features but whatever, I don't have 4 years of improvement.
 *
 * @author Gunging
 */
public interface MythicPlaceholder {

    /**
     * @return The name of the author of this placeholder
     */
    @NotNull String getAuthorName();

    /**
     * Following the convention set by PlaceholderAPI,
     * The identifier has to be lowercase and can't contain _ or %
     *
     * @return The identifier in {@code %<mythic-identifier>_<value>%}
     */
    @NotNull String getMythicIdentifier();

    /**
     * @param text Provided string with placeholders
     *
     * @param thing The object that you will access to parse stuff.
     *
     * @return The string with all placeholders parsed.
     *         <p></p>
     *         Return <code>null</code> to cancel this operation.}
     */
    @Nullable String parse(@NotNull String text, @NotNull Object thing);

    /**
     * @param obj Object that was provided to parse placeholders
     *
     * @return <code><b>true</b></code> IF this placeholder is expecting this object.
     *         <p></p>
     *         If this object wont generate class cast exceptions I mean, say this
     *         placeholder is intended to parse {@link org.bukkit.block.Block} placeholders,
     *         then this method would just check that <code>obj instanceof Block</code>
     */
    boolean forUseWith(@NotNull Object obj);
}

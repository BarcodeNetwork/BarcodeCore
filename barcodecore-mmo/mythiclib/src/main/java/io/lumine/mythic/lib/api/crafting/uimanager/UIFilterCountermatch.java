package io.lumine.mythic.lib.api.crafting.uimanager;

import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Sometimes, plugins may have conflicting filters (or more usually, conflict with the
 * vanilla filter). This provides a means of cross-compatibility that will make an item
 * specifically not match a filter that it otherwise would have.
 * <p></p>
 * Example:
 * <p>
 *     The user has plugin MMOItems installed, and has a powerful MMOItem Catalyst
 *     worth thousands of $$$, which material is an emerald. However, the currency
 *     of the server is also emeralds, so that 1 emerald = 1 $, and has shops setup
 *     that consume emeralds.
 *     <p></p>
 *     It would be a shame if the vanilla filter <code>v EMERALD 0</code> targeted
 *     and consumed the special MMOItems emerald as if it was a common emerald,
 *     taking it as a $1 payment.
 *     <p></p>
 *     To prevent this, MMOItems inserts one of these <code>UIFilterCountermatch</code>es
 *     into the vanilla {@link UIFilter}, that prevents MMOItems from matching it.
 * </p>
 *
 * @author Gunging
 */
public interface UIFilterCountermatch {

    /**
     * This item shall not match (assuming it would have matched).
     * @param item The item in question
     * @param ffp Tell the user why you are preventing this item from matching.
     * @return <code>true</code> to prevent this item from matching.
     */
    boolean preventsMatching(@NotNull ItemStack item, @Nullable FriendlyFeedbackProvider ffp);
}

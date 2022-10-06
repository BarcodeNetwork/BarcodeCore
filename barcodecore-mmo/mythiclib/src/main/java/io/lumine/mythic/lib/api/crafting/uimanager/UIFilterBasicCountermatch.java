package io.lumine.mythic.lib.api.crafting.uimanager;

import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A countermatch that is used to test that UIFilters account for the
 * {@link UIFilter#cancelMatch(ItemStack, FriendlyFeedbackProvider)}
 * method when matching items.
 *
 * @author Gunging
 */
public class UIFilterBasicCountermatch implements UIFilterCountermatch {

    /**
     * During the loading phase, this countermatch will prevent all matching.
     */
    private boolean loading = true;
    private boolean evaluated = false;

    /**
     * During the loading phase, this countermatch will prevent all matching.
     */
    public void load() { loading = false; }
    public boolean wasEvaluated() { return evaluated; }

    /**
     * During the loading phase, this countermatch will prevent all matching.
     */
    public UIFilterBasicCountermatch() {}

    @Override
    public boolean preventsMatching(@NotNull ItemStack item, @Nullable FriendlyFeedbackProvider ffp) {

        // Has been evaluated
        evaluated = true;

        // Will prevent all matching while loading
        return loading;
    }
}

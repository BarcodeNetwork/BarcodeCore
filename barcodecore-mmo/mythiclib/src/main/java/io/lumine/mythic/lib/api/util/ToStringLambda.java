package io.lumine.mythic.lib.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Ever wished to write your own toString method on the spot?
 *
 * Well now you can with this handy transcriber.
 * <p></p>
 * Check this example out:
 * <p></p>
 * <code>
 * ArrayList<String> itemNames = SilentNumbers.transcribeList(itemsList, (o)->SilentNumbers.getItemName( ((ItemStack)o) ));
 * </code>
 *
 * @see io.lumine.mythic.lib.api.util.ui.SilentNumbers#transcribeList(List, ToStringLambda)
 *
 * @author Gunging
 */
public interface ToStringLambda {

    /**
     * Its basically an external toString() method
     * that you get to choose :)
     *
     * @param o Object you'd like to rewrite
     *
     * @return A string representation of it.
     */
    @NotNull String rewrite(@Nullable Object o);
}

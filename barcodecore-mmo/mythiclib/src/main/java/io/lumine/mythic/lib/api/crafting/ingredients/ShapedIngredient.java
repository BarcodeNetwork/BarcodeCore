package io.lumine.mythic.lib.api.crafting.ingredients;

import io.lumine.mythic.lib.api.crafting.recipes.ShapedRecipe;
import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.util.Ref;
import io.lumine.mythic.lib.api.util.ui.FFPMythicLib;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * For recipes that require a pattern of stuff to craft.
 * <p></p>
 * (Basically the most popular: Workbench)
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class ShapedIngredient extends MythicRecipeIngredient implements Cloneable {

    /**
     * Positional ingredients have a little more information:
     *
     * @param ingredient {@inheritDoc}
     *
     * @param horizontalOffset The horizontal shift in slots, always positive (rightward).
     * @param verticalOffset The vertical shift in slots, always negative (downward).
     */
    public ShapedIngredient(@NotNull MythicIngredient ingredient, int horizontalOffset, int verticalOffset) {
        super(ingredient);

        // Get those horizontal and vertical offsets
        setHorizontalOffset(horizontalOffset);
        setVerticalOffset(verticalOffset);
    }

    /**
     * Positional ingredients have a little more information:
     *
     * @param ingredient {@inheritDoc}
     *
     * @param horizontalOffset The horizontal shift in slots, always positive (rightward).
     * @param verticalOffset The vertical shift in slots, always negative (downward).
     */
    public ShapedIngredient(@NotNull ProvidedUIFilter ingredient, int horizontalOffset, int verticalOffset) {
        super(ingredient);

        // Get those horizontal and vertical offsets
        setHorizontalOffset(horizontalOffset);
        setVerticalOffset(verticalOffset);
    }

    /**
     * This ingredient now in a recipe? Let it be known.
     */
     @Nullable
    ShapedRecipe recipe = null;
    /**
     * This ingredient now in a recipe? Let it be known.
     *
     * @param recipe Which recipe.
     */
    public void linkToRecipe(@NotNull ShapedRecipe recipe) { this.recipe = recipe; }
    @Nullable ShapedRecipe getRecipe() { return recipe; }

    /**
     * The vertical shift in slots relative to the top left corner.
     * <p></p>
     * Always Negative
     */
    public int getVerticalOffset() {
        return verticalOffset;
    }

    /**
     * The vertical shift in slots relative to the top left corner.
     * <p></p>
     * Always Negative
     */
    public void setVerticalOffset(int verticalOffset) {
        this.verticalOffset = verticalOffset;
        if (getRecipe() != null) { getRecipe().recalculateSize(); }
    }

    /**
     * The vertical shift in slots relative to the top left corner.
     * <p></p>
     * Always Negative
     */
    int verticalOffset = 0;

    /**
     * The horizontal shift in slots relative to the top left corner.
     * <p></p>
     * Always Positive
     */
    public int getHorizontalOffset() {
        return horizontalOffset;
    }

    /**
     * The horizontal shift in slots relative to the top left corner.
     * <p></p>
     * Always Positive
     */
    public void setHorizontalOffset(int horizontalOffset) {
        this.horizontalOffset = horizontalOffset;
        if (getRecipe() != null) { getRecipe().recalculateSize(); }
    }

    /**
     * The horizontal shift in slots relative to the top left corner.
     * <p></p>
     * Always Positive
     */
    int horizontalOffset = 0;

    /**
     * When it matches, the amount of the provided filter will
     * be moved here for ease of access.
     * <p></p>
     * If the provided UIFilter happened to have the ANY amount
     * Quick Number Range <code>..</code>, this quantity will
     * be ZERO and the item wont be consumed by recipes.
     */
    public int amountOfSuccess = 0;

    /**
     * When it matches, the amount of the provided filter will
     * be moved here for ease of access.
     * <p></p>
     * If the provided UIFilter happened to have the ANY amount
     * Quick Number Range <code>..</code>, this quantity will
     * be ZERO and the item wont be consumed by recipes.
     */
    public int getAmountOfSuccess() { return amountOfSuccess; }

    /**
     * Does this ingredient match this item?
     *
     * @param item Item that you're testing, must fit any of this ingredients' substitutes.
     *             for this to be a success, and be within the correct amount range.
     *
     * @return <code>true</code> if the item is of a correct filter and amount.
     */
    public boolean matches(@NotNull ItemStack item) {

        // Store match
        Ref<ArrayList<ProvidedUIFilter>> match = new Ref<>();

        FriendlyFeedbackProvider ffp = null;
        //MCH//FriendlyFeedbackProvider ffp = new FriendlyFeedbackProvider(FFPMythicLib.get());
        //MCH//ffp.activatePrefix(true, "\u00a78Ingredient \u00a78{\u00a79" + getHorizontalOffset() + " " + getVerticalOffset() + "\u00a78}");

        // Use this' ingredient
        //noinspection ConstantConditions
        if (getIngredient().matches(item, false, match, true, ffp)) {

            // All right so now we have a list of all the provided UIFilters matched
            ArrayList<ProvidedUIFilter> matched = match.getValue();

            // Sort
            assert matched != null;
            Collections.sort(matched);
            //MCH// MythicCraftingManager.log("\u00a78Matches \u00a7cS\u00a7c Filters: \u00a7f" + SilentNumbers.collapseList( SilentNumbers.transcribeList(matched, (s) -> { if (s != null) { return "\u00a78 --\u00a7c-\u00a78-\u00a7e " + ((ProvidedUIFilter) s).toString() + " \u00a77 at \u00a7c" + ((ProvidedUIFilter) s).getAmount(0); } return "null"; }) , "\u00a77, \u00a7f"));

            // Yes, sore amount of the first element, the one of the LEAST amount yes
            ProvidedUIFilter used = match.getValue().get(0);
            amountOfSuccess = used.getAmount(0);
            //MCH// MythicCraftingManager.log( "\u00a78Chose \u00a7e" + used.toString() + "\u00a77 at \u00a7c" + amountOfSuccess);

            //MCH//ffp.sendAllTo(MythicLib.plugin.getServer().getConsoleSender());

            // That's a match
            return true;
        }

        //MCH//ffp.sendTo(FriendlyFeedbackCategory.ERROR, MythicLib.plugin.getServer().getConsoleSender());
        //MCH//ffp.sendTo(FriendlyFeedbackCategory.FAILURE, MythicLib.plugin.getServer().getConsoleSender());

        // Nope
        return false;
    }

    @Override
    public ShapedIngredient clone() {
        try { super.clone(); } catch (CloneNotSupportedException ignored) {}
        return new ShapedIngredient(getIngredient().clone(), getHorizontalOffset(), getVerticalOffset());
    }

    /**
     * Clones this ingredient, and recalculates offsets based on this inventory size
     * to make them <b>the actual slot</b> this ingredient is expected to be in.
     *
     * @param height The height of the recipe space (always positive)
     * @param width The width of the recipe space (always positive)
     *
     * @return <code>null</code> if these indexes overflow the area (inventory is too small for this recipe).
     */
    @Nullable public ShapedIngredient adapt(int width, int height) {

        // Adapt with the starter coordinates = 0
        return adapt(width, height, 0, 0);
    }

    /**
     * Clones this ingredient, and recalculates offsets based on this inventory size
     * to make them <b>the actual slot</b> this ingredient is expected to be in.
     *
     * @param height The height of the recipe space (always positive)
     * @param width The width of the recipe space (always positive)
     * @param w0 Start width co-ordinate (always positive)
     * @param h0 Start height co-ordinate (always negative)
     *
     * @return <code>null</code> if these indexes overflow the area (inventory is too small for this recipe).
     *
     * @throws IllegalArgumentException when you don't respect the positivity or negativity of the offsets.
     */
    @Nullable public ShapedIngredient adapt(int width, int height, int w0, int h0) throws IllegalArgumentException {

        // Stuff that is absolutely illegal
        if (width < 0) { throw new IllegalArgumentException(FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                "Width of inventories $fcannot be negative$b. Provided $u{0}$b. ", String.valueOf(width))); }
        if (height < 0) { throw new IllegalArgumentException(FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                "Height of inventories $fcannot be negative$b. Provided $u{0}$b. ", String.valueOf(height))); }

        if (w0 < 0) { throw new IllegalArgumentException(FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                "Width offset for positional recipe ingredients $fcannot be negative$b. Provided $u{0}$b. ", String.valueOf(w0))); }
        if (h0 > 0) { throw new IllegalArgumentException(FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                "Height offset for positional recipe ingredients $fcannot be positive$b. Provided $u{0}$b. ", String.valueOf(h0))); }

        if (getHorizontalOffset() < 0) { throw new IllegalArgumentException(FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                "Horizontal offset for positional recipe ingredients $fcannot be negative$b. Provided $u{0}$b. ", String.valueOf(getHorizontalOffset()))); }
        if (getVerticalOffset() > 0) { throw new IllegalArgumentException(FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                "Vertical offset for positional recipe ingredients $fcannot be positive$b. Provided $u{0}$b. ", String.valueOf(getVerticalOffset()))); }


        // Do they overflow?
        if (overflowsWidthFromOffset(width, w0) || overflowsHeightFromOffset(height, h0)) { return null; }

        // Accept
        return new ShapedIngredient(getIngredient().clone(), w0 + getHorizontalOffset(), h0 + getVerticalOffset());
    }

    /**
     *  Would this overflow at such start index <code>w0</code>?
     *
     * @param width Width of table, positive number.
     * @param w0 Width shift, <b>positive</b> number.
     */
    public boolean overflowsWidthFromOffset(int width, int w0) {

        // Calculate expected offsets
        int expectedHorizontalPosition = w0 + getHorizontalOffset();

        // Does it overflow?
        return expectedHorizontalPosition >= width;
    }

    /**
     *  Would this overflow at such  index <code>h0</code>?
     *
     * @param height Height of table, positive number.
     * @param h0 Height shift, <b>negative</b> number.
     */
    public boolean overflowsHeightFromOffset(int height, int h0) {

        // Calculate expected offsets
        int expectedVerticalPosition = h0 + getVerticalOffset();

        // Does it overflow?
        return (-expectedVerticalPosition) >= height;
    }
}

package io.lumine.mythic.lib.api.util.ui;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * You see how vanilla minecraft handles number ranges?
 * <p></p>
 * I'm talking about the command selectors, <code>@e[distance=..10]</code> for example.
 * <p></p>
 * Well, this system is genius :
 * Allows negative and decimal numbers, as well as infinity as one of the bounds of detection.
 * <p></p>
 * Use {@link #getFromString(String, FriendlyFeedbackProvider)} to generate one of these bad boys from user input!
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class QuickNumberRange implements Cloneable {

    /**
     * If defined, this indicates the lower range the user specified.
     */
    @Nullable Double minimumInclusive;

    /**
     * If defined, this indicates the higher range the user specified.
     */
    @Nullable Double maximumInclusive;

    /**
     * If defined, this indicates the lower range the user specified.
     * @see #hasMin()
     */
    @Nullable public Double getMinimumInclusive() { return minimumInclusive; }

    /**
     * If defined, this indicates the higher range the user specified.
     * @see #hasMax()
     */
    @Nullable public Double getMaximumInclusive() { return maximumInclusive; }

    /**
     * Indicates if the user specified the lower range {@link #getMinimumInclusive()}.
     */
    public boolean hasMin() { return minimumInclusive != null; }

    /**
     * Indicates if the user specified the higher range {@link #getMaximumInclusive()}.
     */
    public boolean hasMax() { return maximumInclusive != null; }

    /**
     * This '<i>Quick Number Range</i>' is actually just a number,
     * because the user specified the same as the min and max.
     */
    @SuppressWarnings("ConstantConditions")
    public boolean isSimple() {

        // If either bound is missing, this is not simple.
        if (!hasMax() || !hasMin()) { return false; }

        // They match-yo?
        return getMaximumInclusive().equals(getMinimumInclusive());
    }

    /**
     * @param def Value to use if this has no bounds
     *
     * @return This its simplest form, in the following priority:
     *         <p></p>
     *         1: Lower bound, if there is one <br>
     *         2: Upper bound, if there is one <br>
     *         3: def
     */
    @SuppressWarnings("ConstantConditions")
    public double getAsDouble(double def) {
        if (hasMin()) { return getMinimumInclusive(); }
        if (hasMax()) { return getMaximumInclusive(); }
        return def;
    }

    /**
     * Supported Formats:
     * <p><code>[m]..[M]</code>  (Vanilla range, the way used in command selectors)
     * </p><code>[n]</code> (Just a number; basically <code>=</code> operator)
     * <p><code>..[M]</code> (anything up to this; basically <code><=</code> operator)
     * </p><code>[m]..</code> (this and on; basically <code>>=</code> operator)
     * <p></p>
     * Obviously, number <code>[m]</code> is the minimum, and <code>[M]</code> the maximum;
     * They may not even be specified.
     *
     * @return <code>null</code>> if incorrect format.
     */
    @Contract("null -> null") @Nullable public static QuickNumberRange getFromString(@Nullable String qnr) { return getFromString(qnr, null); }

    /**
     * Supported Formats:
     * <p><code>[m]..[M]</code>  (Vanilla range, the way used in command selectors)
     * </p><code>[n]</code> (Just a number; basically <code>=</code> operator)
     * <p><code>..[M]</code> (anything up to this; basically <code><=</code> operator)
     * </p><code>[m]..</code> (this and on; basically <code>>=</code> operator)
     * <p></p>
     * Obviously, number <code>[m]</code> is the minimum, and <code>[M]</code> the maximum;
     * They may not even be specified.
     *
     * @param ffp Tell the user why their input doesn't make sense with one of these
     *
     * @return <code>null</code>> if incorrect format.
     */
    @Contract("null, _ -> null") @Nullable public static QuickNumberRange getFromString(@Nullable String qnr, @Nullable FriendlyFeedbackProvider ffp) {

        // Parsing errors before any parsing errors could have existed...
        if (qnr == null) {

            // Appendable Message
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "No value provided to parse QuickNumberRange. ");

            // No
            return null;
        }

        // The special case, when neither bound is specified (bruh but ok)
        if (qnr.equals("..")) {

            // Return
            return new QuickNumberRange(null, null);
        }

        // Number itself? So its basically an EXACTLY this value, I'll allow it.
        Double asItself = SilentNumbers.DoubleParse(qnr);
        if (asItself != null) {

            // Return
            return new QuickNumberRange(asItself, asItself);
        }

        // Split
        if (qnr.contains("..")) {

            // Split
            String[] blit = qnr.split("\\.\\.");

            // Must be length two
            if (blit.length == 2 || blit.length == 1) {

                // Failure?
                boolean failure = false;

                /*
                 *   Identify minor and major bounds
                 */
                String m = "", M = "";

                // Are both specified?
                if (blit.length == 2) {

                    // In order
                    m = blit[0];
                    M = blit[1];

                // Only one is specified
                } else {

                    // Unrestricted Bounds
                    if(qnr.startsWith("..")) {

                        // Its second
                        M = blit[0];

                    } else {

                        // Its first
                        m = blit[0];
                    }
                }

                /*
                 *  Parse both of them
                 */
                Double min = SilentNumbers.DoubleParse(m);
                Double max = SilentNumbers.DoubleParse(M);

                // Try to parse both
                if (m.length() > 0 && min == null) {

                    // Number Format Error
                    failure = true;

                    // Appendable Message
                    FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                            "Expected numeric value (negative and decimals supported) instead of $u{0}. ", m);
                }

                // Try to parse both
                if (M.length() > 0 && max == null) {

                    // Number Format Error
                    failure = true;

                    // Appendable Message
                    FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                            "Expected numeric value (negative and decimals supported) instead of $u{0}. ", M);
                }

                // Success?
                if (!failure) {

                    // Return
                    return new QuickNumberRange(min, max);
                }
            }
        }

        // Appendable Message
        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                "Invalid syntax in '$u{0}$b': Expected a $enumber$b, or a range in the format $e[m]..[M]$b where m is the minimum number, and M is the maximum number (both optional). ", qnr);

        // Something went wrong
        return null;
    }

    /**
     * An easy way of testing if a number falls between two numbers.
     *
     * @param min The (optional) lower bound
     * @param max The (optional) upper bound
     */
    public QuickNumberRange(@Nullable Double min, @Nullable Double max) {
        minimumInclusive = min;
        maximumInclusive = max;
    }

    /**
     * @param test Number in question.
     * @return If a number is within the boundaries.
     */
    @SuppressWarnings({"ConstantConditions", "RedundantIfStatement"})
    public boolean inRange(double test) {
        //CHK// MythicCraftingManager.log("\u00a78QNR \u00a7eA\u00a77 Testing number \u00a7b" + test + "\u00a77 to fit in \u00a73" + this.toString());

        // If has minimum
        if (hasMin()) {

            // Fail if below minimum
            if (test < getMinimumInclusive()) {
                //CHK// MythicCraftingManager.log("\u00a78QNR \u00a7eA\u00a77 Test less than the minimum \u00a7b" + getMinimumInclusive() + ", \u00a7cFail");
                return false; }
        }

        // If has maximum
        if (hasMax()) {

            // Fail if above maximum
            if (test > getMaximumInclusive()) {
                //CHK// MythicCraftingManager.log("\u00a78QNR \u00a7eA\u00a77 Test more than the minimum \u00a7b" + getMaximumInclusive() + ", \u00a7cFail");
                return false; }
        }

        // Success
        //CHK// MythicCraftingManager.log("\u00a78QNR \u00a7eA\u00a77 Test within range \u00a7aSuccess");
        return true;
    }

    /**
     * Basically reverts {@link #getFromString(String, FriendlyFeedbackProvider)}
     *
     * @return A String that may become a Quick Number Range again.
     *
     * @see #toStringColored()
     */
    @Override public String toString() {

        // Simple?
        if (isSimple()) { return String.valueOf(getMaximumInclusive()); }

        // Get
        String minn = ""; String maxn = "";
        if (hasMin()) { minn = String.valueOf(getMinimumInclusive()); }
        if (hasMax()) { maxn = String.valueOf(getMaximumInclusive()); }

        // Build
        return minn + ".." + maxn;
    }

    /**
     * Its basically a <code>toString()</code> method but looks nicer.
     * Use if you're going to display the value in the console idk.
     * <p></p>
     * <b>Uses the {@link FriendlyFeedbackPalette} color codes system.</b>
     *
     * @see #toString()
     */
    @NotNull public String toStringColored() {

        // Simple?
        if (isSimple()) { return "$r" + getMaximumInclusive(); }

        // Get
        String minn = "-∞"; String maxn = "∞";
        if (hasMin()) { minn = String.valueOf(getMinimumInclusive()); }
        if (hasMax()) { maxn = String.valueOf(getMaximumInclusive()); }

        // Build
        return "$r" + minn + " $bto$r " + maxn;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public QuickNumberRange clone() { return new QuickNumberRange(getMinimumInclusive(), getMaximumInclusive()); }
}


package io.lumine.mythic.lib.api.util.ui;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * A most compact way for an user to perform operations on a variable.
 * <p>From a string input of the user, get one from this (which will
 * return <code>null</code> if it could not parse correctly): {@link #getFromString(String)}</p>
 * Once you have a PMP, apply it to whatever number with {@link #apply(double)}.
 * <p></p>
 * Examples (let the base number be <b>200</b>):
 * <p> +5   = <b>205</b>     <code>'Add'</code>
 * </p> -5  = <b>195</b>     <code>'Subtract'</code>
 * <p> 5    = <b>5</b>       <code>'Set'</code>
 * </p> n5  = <b>-5</b>      <code>'Set'</code>
 * <p> 5%   = <b>10</b>      <code>'Multiply'</code>
 * </p> +5% = <b>210</b>     <code>'Percent Bonus'</code>
 * <p> -5%  = <b>190</b>     <code>'Percent Deduction'</code>
 * </p> n5% = <b>-10</b>     <code>'Flip Multiplication'</code>
 *
 * @author Gunging
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class PlusMinusPercent {
    double value;
    boolean relative;
    boolean multiply;

    /**
     * Build a new PMP without needing to parse a string.
     * @param constant A number that represents this operation. Not to be confused with the <i>base</i>.
     *                 The base is the external number you will apply this operation to. We'll call it
     *                 <b>C</b> for now. <p></p>
     * @param additive Will <b>C</b> be added/subtracted from the base? (If <code>false</code>, the
     *                 base will be <i>set</i> to <b>C</b>). <p></p>
     * @param multiplicative If not additive, instead of <i>setting</i> the base
     *                       to <b>C</b>, the base will be multiplied by <b>C</b>.
     *                       <p></p>
     *                       If additive, instead of adding <b>C</b> to the base,
     *                       the base will be multiplied by <b>1 + C</b> (Which is identical
     *                       to the expression <u><code>base = base + <b>C</b>*base</code></u>)
     * @see #getFromString(String)
     */
    public PlusMinusPercent(double constant, boolean additive, boolean multiplicative) {
        value = constant;
        relative = additive;
        multiply = multiplicative;
    }

    /**
     *  Perform the operation represented by this PMP onto the <code>base</code>.
     */
    public double apply(double base) {

        // Stores a copy of the constant that may be freely modified through this method (makes math easier)
        double oV = value;

        // Is it multiplicative?
        if (multiply) {

            // Relative is on base 100%; so +50% means it multiplies by 1.5; while -50% is *0.5
            if (relative) { oV += 1; }

            return base * oV;

       // Its not a multiplicative, its scalar
        } else {

            // If it is relative to the source
            if (relative) {

                // Just shift by the source
                return  base + oV;

                // Otherwise its a straight up set
            } else {

                // Thets the set
                return oV;
            }
        }
    }

    /**
     *  Does the opposite operation represented by this <b>except</b> if its a <i>set</i> in which all previous information is lost.
     *  <p></p>
     *  Reverts the operation represented by this, if called {@link #apply(double)} right before.
     */
    public double reverse(double base) {

        // Stores a copy of the constant that may be freely modified through this method (makes math easier)
        double oV = value;

        // Is it multiplicative?
        if (multiply) {

            // Relative is on base 100%; so +50% means it multiplies by 1.5; while -50% is *0.5
            if (relative) { oV += 1; }

            return base / oV;

            // Its not a multiplicative, its scalar
        } else {

            // If it is relative to the source
            if (relative) {

                // Just shift by the source
                return  base - oV;

                // Otherwise its a straight up set
            } else {

                // Thets the set
                return base;
            }
        }
    }

    public void setConstant(double c) { value = c; }
    public void setAdditive(boolean additivity) { relative = additivity; }
    public void setMultiplicative(boolean multiplicativity) { multiply = multiplicativity; }

    /**
     * The constant of the operation this PMP executes.
     * <p></p>
     * I call it <b>C</b>.
     */
    public double getConstant() { return value; }
    /**
     * Will <b>C</b> ({@link #getConstant()}) have an add or set behaviour?
     * <p></p>
     * Basically:
     * <p> <code>true</code>: <u><code>base = base + <b>C</b></code></u>
     * </p> <code>false</code>: <u><code>base = <b>C</b></code></u>
     * <p></p>
     * And
     * <p> if additive and multiplicative: <u><code>base = base + <b>C</b>*base</code></u>
     * @see #isMultiplicative()
     */
    public boolean isAdditive() { return relative; }
    /**
     * Will <b>C</b> ({@link #getConstant()}) have an multiplicative behaviour?
     * <p></p>
     * Enabling this will mean:
     * <p> if not additive: <u><code>base = <b>C</b>*base</code></u>
     * </p> if additive: <u><code>base = base + <b>C</b>*base</code></u>
     * @see #isAdditive()
     */
    public boolean isMultiplicative() { return multiply; }

    /**
     * Will return a PMP from the given string, or <code>null</code> if it doesn't parse.
     */
    @Nullable
    @Contract("null -> null")
    public static PlusMinusPercent getFromString(@Nullable String pmp) { return getFromString(pmp, null); }

    /**
     * Will return a PMP from the given string, or <code>null</code> if it doesn't parse.
     */
    @Nullable
    @Contract("null, _ -> null")
    public static PlusMinusPercent getFromString(@Nullable String pmp, @Nullable FriendlyFeedbackProvider ffp) {

        // Parsing errors before any parsing errors could have existed...
        if (pmp == null) {

            // Appendable Message
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "No value provided to parse PlusMinusPercent. ");
            return null;
        }

        // Interpret the user's input
        boolean relativity = false;
        boolean multiplicativity = false;
        double positivity = 1.0;
        double value;
        String unsignedArg = pmp;

        if (pmp.startsWith("-")) {
            relativity = true;
            positivity = -1.0;
            unsignedArg = pmp.substring(1);

        } else if (pmp.startsWith("+")) {
            relativity = true;
            unsignedArg = pmp.substring(1);

        } else if (pmp.startsWith("n")) {
            positivity = -1.0;
            unsignedArg = pmp.substring(1);
        }

        if (pmp.endsWith("%")) {
            multiplicativity = true;
            positivity *= 0.01;
            unsignedArg = unsignedArg.substring(0, unsignedArg.length() - 1);
        }

        // Attempt to parse the constant
        Double constnt = SilentNumbers.DoubleParse(unsignedArg);

        // Success?
        if (constnt != null) {

            // Use
            value = constnt * positivity;

        // Fail
        } else {

            // Appendable Message
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "Cant parse numeric value from '$r{0}$b' (The numeric part of $i{1}$b: After removing ±, n, and %). ", unsignedArg, pmp);
            return null;
        }

        // Success
        return new PlusMinusPercent(value, relativity, multiplicativity);
    }

    @Override public String toString() {
        StringBuilder str = new StringBuilder();

        // Relative?
        if (isAdditive() && !isMultiplicative()) {

            // n or no prefix?
            if (getConstant() < 0) { str.append('-'); } else { str.append('+'); }

        } else {

            // n or no prefix?
            if (getConstant() < 0) { str.append('n'); }
        }

        // Appent absolute value
        double val = getConstant();
        if (isMultiplicative()) { val *= 100; }

        // Append thay
        str.append(SilentNumbers.readableRounding(val, 2));

        // Append percent
        if (isMultiplicative()) { str.append('%'); }

        // Build
        return str.toString();
    }
}

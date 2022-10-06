package io.lumine.mythic.lib.api.util.ui;

import io.lumine.utils.version.ServerVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper class for a string to contain a bit more of
 * information about the style it shall follow.
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class FriendlyFeedbackMessage implements Cloneable {
    @NotNull String message;
    boolean withPrefix = false;

    /**
     *  Clones this <code>FriendlyFeedbackMessage</code>.
     */
    @Override public FriendlyFeedbackMessage clone() {
        try { super.clone(); } catch (CloneNotSupportedException ignored) {}
        return new FriendlyFeedbackMessage(getMessage(), hasPrefix(), getSubdivision());
    }

    /**
     * Actual message (before parsing color codes) being sent.
     */
    public void setMessage(@NotNull String message) {
        this.message = message;
    }

    /**
     * Whether or not this should use the prefix from the
     * {@link FriendlyFeedbackPalette} it is colored with.
     */
    public void togglePrefix(boolean usePrefix) {
        this.withPrefix = usePrefix;
    }

    /**
     * If using prefix, this will be some extra
     * keyword included in the prefix. Very nice!
     * @see #hasPrefix()
     */
    public void setSubdivision(@Nullable String subdivision) {
        this.withSubdivision = subdivision;
    }

    @Nullable String withSubdivision = null;

    /**
     * Actual message (before parsing color codes) being sent.
     */
    @NotNull public String getMessage() { return message; }

    /**
     * Whether or not this should use the prefix from the
     * {@link FriendlyFeedbackPalette} it is colored with.
     */
    public boolean hasPrefix() { return withPrefix; }

    /**
     * If using prefix, this will be some extra
     * keyword included in the prefix. Very nice!
     * @see #hasPrefix()
     */
    @Nullable public String getSubdivision() { return withSubdivision; }

    /**
     *  Straight up, just a message.
     */
    public FriendlyFeedbackMessage(@NotNull String message) {
        // Set the message
        this.message = message;
    }

    /**
     *  A message containing some style information
     */
    public FriendlyFeedbackMessage(@NotNull String message, @Nullable String subdivision) {
        this.message = message;
        if (subdivision != null) {
            withPrefix = true;
            withSubdivision = subdivision;
        }
    }

    /**
     *  A message containing some style information
     */
    public FriendlyFeedbackMessage(@NotNull String message, boolean usePrefix) {
        this.message = message;
        withPrefix = usePrefix;
    }

    /**
     *  A message containing some style information
     */
    public FriendlyFeedbackMessage(@NotNull String message, boolean usePrefix, @Nullable String subdivision) {
        this.message = message;
        withPrefix = usePrefix;
        withSubdivision = subdivision;
    }

    @Override public String toString() { return getMessage(); }

    /**
     *   Parses a message intended to be read in-game.
     *   Basically supporting HEX codes in 1.16+
     *   <p></p>
     *   <b>This does not parse color codes</b> other than those concerning the palette.
     *   <p></p>
     *   Will delegate to {@link #forConsole(FriendlyFeedbackPalette)}
     *   in previous minecraft versions because it is assumed
     *   that the console colors have no HEX
     */
    @NotNull public String forPlayer(@NotNull FriendlyFeedbackPalette pal) {

        // If below 1.16, parse as console (which is guaranteed to have no HEX colors)
        if (ServerVersion.get().getMinor() < 16) { return forConsole(pal); }
        StringBuilder actualMessage = new StringBuilder();

        // Add appropriate prefix
        if (hasPrefix()) {

            // Add (accounting for subdivision)
            actualMessage.append(pal.parseForPlayer(pal.getPrefix(getSubdivision())));
        }

        // Add colored message
        actualMessage.append(pal.parseForPlayer(pal.parseForPlayer(getMessage())));

        // Return built
        return actualMessage.toString();
    }

    /**
     * Parses a message intended to be read through the console.
     * Consoles don't support many colors, in fact, they support
     * the same colors supported until MC 1.15
     * <p></p>
     * <b>This does not parse color codes</b> other than those concerning the palette.
     * <p></p>
     * Will also use this method if parsing a message in a version
     * of minecraft less than 1.16
     */
    @NotNull public String forConsole(@NotNull FriendlyFeedbackPalette pal) {
        StringBuilder actualMessage = new StringBuilder();

        // Add appropriate prefix
        if (hasPrefix()) {

            // Add (accounting for subdivision)
            actualMessage.append(pal.parseForConsole(pal.consolePrefix(getSubdivision())));
        }

        // Add colored message
        actualMessage.append(pal.parseForConsole(pal.parseForConsole(getMessage())));

        // Return built
        return actualMessage.toString();
    }
}

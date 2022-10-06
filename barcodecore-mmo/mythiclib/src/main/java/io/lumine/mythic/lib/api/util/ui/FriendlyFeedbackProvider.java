package io.lumine.mythic.lib.api.util.ui;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.version.ServerVersion;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * In our case, us developers, Java throws {@link Exception}s at us when we fuck something up.
 * Normal users don't need to be blasted with such technical messages, but they also
 * make mistakes.
 * <p></p>
 * This interface is meant to provide a better user experience by telling
 * the user why their input has failed with easier implementation in our side (not having
 * to check the input before trying to use it elsewhere).
 * <p></p>
 * This is designed with <b>commands</b> in mind, where the only user input is ultimately
 * {@link String}s, and the messages users can receive are console lines or chat messages.
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class FriendlyFeedbackProvider {

    //region Main
    @NotNull FriendlyFeedbackPalette ffPalette;

    /**
     * The palette used by this feedback provider.
     * <p></p>
     * It has the color stuff, and the prefix most notably.
     */
    public FriendlyFeedbackPalette getPalette() { return ffPalette; }

    /**
     * To initialize a feedback provider you just need a palette.
     */
    public FriendlyFeedbackProvider(@NotNull FriendlyFeedbackPalette palette) {
        ffPalette = palette;
    }
    //endregion

    //region Collecting Messages
    @NotNull HashMap<FriendlyFeedbackCategory, ArrayList<FriendlyFeedbackMessage>> feedback = new HashMap<>();

    /**
     * Get the feedback of this category that has been registered.
     * <p></p>
     * Will return an empty array if no messages have been issued concerning this topic.
     */
    @NotNull public ArrayList<FriendlyFeedbackMessage> getFeedbackOf(@NotNull FriendlyFeedbackCategory category) {

        // Make sure it is registered
        return feedback.computeIfAbsent(category, k -> new ArrayList<>());
    }

    /**
     * Clears the feedback of this category that has been registered.
     * <p></p>
     * Will not do anything if theres no feedback
     */
    public void clearFeedbackOf(@NotNull FriendlyFeedbackCategory category) {

        // Make sure it is registered
        feedback.put(category, new ArrayList<>());
    }

    /**
     * Clears all the feedback stored in this FFP.
     * <p></p>
     * Will not do anything if theres no feedback
     */
    public void clearFeedback() { feedback.clear(); }

    /**
     * Include a message to be sent later under this category.
     * <b>This does not actually send a message</b>
     * <p></p>
     * Fails silently if the message is <code>null</code> or empty.
     * <p></p>
     * The first <code>$b</code> is included. Check out the codes
     * accepted in the description of {@link FriendlyFeedbackPalette}
     * @param replaces The (ordered) list of string variables to be replaced.
     *                 <p></p>
     *                 Suppose your <code>message</code> is
     *                 <b><code>"Your input $i{0}$b is not a number!"</code></b>
     *                 <p></p>
     *                 This means that the first element of the array will be
     *                 inserted in the place of that <code>{0}</code>.
     */
    public void log(@NotNull FriendlyFeedbackCategory category, @Nullable String message, String... replaces) {

        // Cancel if null
        if (message == null) { return; }
        if (message.isEmpty()) { return; }

        // Add, simple
        getFeedbackOf(category).add(getMessage(message, replaces));
    }

    /**
     * Gets a message from these arguments, applying prefix if needed! <p>
     * (Wont include palette yet, that is applied immediately before actually sending)
     */
    @NotNull public FriendlyFeedbackMessage getMessage(@NotNull String message, String... replaces) {

        // That's the result
        return generateMessage(prefixSample, message, replaces);
    }

    /**
     * Shorthand for: <p><code>if (ffp != null) { ffp.Log(category, message, replaces);</code></p>
     * <p></p>
     * To read what this actually does, the description is in {@link #log(FriendlyFeedbackCategory, String, String...)}
     * <p></p>
     * <b>It is convention to end all your logs in a period and a space</b> so that, if anything else gets added on top,
     * we don't have to worry about it looking like this:
     * <p><code>You messed that up man, try againEncountered error at file 3.Give me the plant</code></p>
     * <p></p>
     * Instead of like this:
     * <p><code>You messed that up man, try again. Encountered error at file 3. Give me the plant</code></p>
     *
     * @param ffp FriendlyFeedbackProvided that may be null.
     */
    public static void log(@Nullable FriendlyFeedbackProvider ffp, @NotNull FriendlyFeedbackCategory category, @Nullable String message, String... replaces) { if (ffp != null) { ffp.log(category, message, replaces); } }

    @NotNull FriendlyFeedbackMessage prefixSample = new FriendlyFeedbackMessage("");
    /**
     * Call this method to make incoming messages acquire a prefix of your choosing.
     * <p></p>
     * The prefix is added as soon as the message is registered, so you may change
     * (or remove) the prefix afterwards for new messages without messing with old
     * ones.
     * @param usePrefix Whether to actually use prefix
     * @param subdivision A subdivision to add to the prefix
     */
    public void activatePrefix(boolean usePrefix, @Nullable String subdivision) {

        // If used
        prefixSample.togglePrefix(usePrefix);
        prefixSample.setSubdivision(subdivision);
    }

    /**
     * @return How many messages are in this category
     */
    public int messagesTotal(@NotNull FriendlyFeedbackCategory ofCategory) { return getFeedbackOf(ofCategory).size(); }

    /**
     * @return How many messages are in this Friendly Feedback Provider
     */
    public int messagesTotal() { int t = 0; for (FriendlyFeedbackCategory cat : FriendlyFeedbackCategory.values()) { t+= messagesTotal(cat); } return t; }
    //endregion

    //region Sending Messages
    /**
     * Sends all stored messages to both a console and a player.
     */
    public void sendAllTo(@NotNull Player player, @NotNull ConsoleCommandSender console) {

        // For each category
        for (FriendlyFeedbackCategory cat : feedback.keySet()) {

            // Log all
            sendTo(cat, player);
            sendTo(cat, console);
        }
    }
    /**
     * Sends all stored messages of this category to both a console and a player.
     */
    public void sendTo(@NotNull FriendlyFeedbackCategory category, @NotNull Player player, @NotNull ConsoleCommandSender console) {

        // Send to both I guess
        sendTo(category, player);
        sendTo(category, console);
    }

    /**
     * Sends all stored messages to a player.
     */
    public void sendAllTo(@NotNull Player player) {

        // For each category
        for (FriendlyFeedbackCategory cat : feedback.keySet()) {

            // Log all
            sendTo(cat, player);
        }
    }
    /**
     * Sends all stored messages of this category to a player.
     */
    public void sendTo(@NotNull FriendlyFeedbackCategory category, @NotNull Player player) {

        // Get List and foreach
        for (FriendlyFeedbackMessage msg : getFeedbackOf(category)) {

            // Send to player
            player.sendMessage(MythicLib.plugin.parseColors(msg.forPlayer(getPalette())));
        }
    }

    /**
     * Sends all stored messages to the console.
     */
    public void sendAllTo(@NotNull ConsoleCommandSender console) {

        // Delegate
        sendAllTo((CommandSender) console);
    }
    /**
     * Sends all stored messages of this category to the console.
     */
    public void sendTo(@NotNull FriendlyFeedbackCategory category, @NotNull ConsoleCommandSender console) {

        // Delegate
        sendTo(category, (CommandSender) console);
    }
    /**
     * Sends all stored messages to the console.
     */
    public void sendAllTo(@NotNull CommandSender sender) {

        // For each category
        for (FriendlyFeedbackCategory cat : feedback.keySet()) {

            // Log all
            sendTo(cat, sender);
        }
    }
    /**
     * Sends all stored messages of this category to the console.
     */
    public void sendTo(@NotNull FriendlyFeedbackCategory category, @NotNull CommandSender sender) {

        // Get List and foreach
        for (FriendlyFeedbackMessage msg : getFeedbackOf(category)) {

            // Send to console
            sender.sendMessage(MythicLib.plugin.parseColors(msg.forConsole(getPalette())));
        }
    }
    //endregion

    //region Ease-Of-Use Utils
    /**
     * Generates a {@link FriendlyFeedbackMessage} from the string and args you provide.
     * Remember that the first <code>$b</code> is included.
     * @param message Actual message you are sending.
     *                <p></p>
     *                For example:
     *                <p><code>Hey! You forgot your $e{0}$b! Come back!</code></p>
     * @param replaces What to replace each variable of the message with.
     *                 <p></p>
     *                 For example:
     *                 <p><code>Lunchbox</code> (That, as the index zero of this array, will replace the variable <code>{0}</code>)</p>
     * @return A wrapped message. Colors have not parsed yet (Notice that you didn't even specify a palette).
     */
    @NotNull public static FriendlyFeedbackMessage generateMessage(@NotNull String message, String... replaces) {

        // That's the result
        return generateMessage(null, message, replaces);
    }

    /**
     * Generates a {@link FriendlyFeedbackMessage} from the string and args you provide.
     * Remember that the first <code>$b</code> is included.
     * @param prefixTemplate A dummy, empty message with the following information:
     *                       <p> > Will the message be prefixed?
     *                       </p> > Which subdivision to put in the prefix?
     * @param message Actual message you are sending.
     *                <p></p>
     *                For example:
     *                <p><code>Hey! You forgot your $e{0}$b! Come back!</code></p>
     * @param replaces What to replace each variable of the message with.
     *                 <p></p>
     *                 For example:
     *                 <p><code>Lunchbox</code> (That, as the index zero of this array, will replace the variable <code>{0}</code>)</p>
     * @return A message with prefix information. Colors have not parsed yet (Notice that you didn't even specify a palette).
     */
    @NotNull public static FriendlyFeedbackMessage generateMessage(@Nullable FriendlyFeedbackMessage prefixTemplate, @NotNull String message, String... replaces) {

        // Fresh (non-prefixed) message if unspecified.
        if (prefixTemplate == null) { prefixTemplate = new FriendlyFeedbackMessage(""); }

        // Bake message
        for (int i = 0; i < replaces.length; i++) {
            String rep = replaces[i];
            if (rep == null) { rep = ""; }
            message =
                    message.
                            replace(
                                    "{" + i + "}",
                                    rep
                            );
        }

        // Build with prefix
        FriendlyFeedbackMessage msg = prefixTemplate.clone();
        msg.setMessage("$b" + message);

        // That's the result
        return msg;
    }

    /**
     * Straight up get the styled message ready to be sent to the console!
     * <p></p>
     * Only parses {@link FriendlyFeedbackPalette} style codes, you may parse other
     * color codes afterwards.
     * @param palette Palette to style the color codes of the message.
     * @param message Actual message you are sending.
     *                <p></p>
     *                For example:
     *                <p><code>Hey! You forgot your $e{0}$b! Come back!</code></p>
     * @param replaces What to replace each variable of the message with.
     *                 <p></p>
     *                 For example:
     *                 <p><code>Lunchbox</code> (That, as the index zero of this array, will replace the variable <code>{0}</code>)</p>
     * @return A message ready to be sent to the console (or a pre 1.16 client that supports no HEX codes).
     */
    @NotNull public static String quickForConsole(@NotNull FriendlyFeedbackPalette palette, @NotNull String message, String... replaces) {

        // Generate
        FriendlyFeedbackMessage msg = generateMessage(null, message, replaces);

        // Style and send
        return msg.forConsole(palette);
    }

    /**
     * Straight up get the styled message ready to be sent to a player!
     * <p></p>
     * Only parses {@link FriendlyFeedbackPalette} style codes, you may parse other
     * color codes afterwards.
     * @param palette Palette to style the color codes of the message.
     * @param message Actual message you are sending.
     *                <p></p>
     *                For example:
     *                <p><code>Hey! You forgot your $e{0}$b! Come back!</code></p>
     * @param replaces What to replace each variable of the message with.
     *                 <p></p>
     *                 For example:
     *                 <p><code>Lunchbox</code> (That, as the index zero of this array, will replace the variable <code>{0}</code>)</p>
     * @return A message ready to be sent to a player. As always, if mc version is less than 1.16,
     *         it instead delegates to {@link #quickForConsole(FriendlyFeedbackPalette, String, String...)} which
     *         is assumed t have no HEX codes.
     */
    @NotNull public static String quickForPlayer(@NotNull FriendlyFeedbackPalette palette, @NotNull String message, String... replaces) {

        // Choose
        if (ServerVersion.get().getMinor() < 16) { return quickForConsole(palette, message, replaces); }

        // Generate
        FriendlyFeedbackMessage msg = generateMessage(null, message, replaces);

        // Style and send
        return msg.forPlayer(palette);
    }
    //endregion
}

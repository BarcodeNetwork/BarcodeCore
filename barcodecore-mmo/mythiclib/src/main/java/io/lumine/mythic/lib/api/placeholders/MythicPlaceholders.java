package io.lumine.mythic.lib.api.placeholders;

import io.lumine.mythic.lib.comp.PlaceholderAPIHook;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * PlaceholderAPI is cool, but have you heard of ItemStacks,
 * Blocks, heck, Entities in general?
 * <p></p>
 * With the lack of support for more common object types,
 * MythicLib now shall have a <i>better</i>, more powerful
 * tool to parse strings.
 *
 * @author Gunging
 */
public class MythicPlaceholders {

    /**
     * @param str String with placeholders
     * @param objects List of objects to parse this string with.
     *                This will search the placeholder intended to
     *                parse this object among those registered.
     *
     * @return A string parsed to completion, as possible.
     */
    @NotNull public static String parse(@NotNull String str, @NotNull Object... objects) {

        // No placeholders no service
        if (!str.contains("%")) { return str; }

        // Attempt to find
        for (Object o : objects) {

            // Skip null
            if (o == null) { continue; }

            // Find
            ArrayList<MythicPlaceholder> parsers = getParserFor(o);
            if (parsers.isEmpty()) { continue; }

            // Parse with each
            for (MythicPlaceholder p : parsers) {

                // Skip
                if (p == null) { continue; }

                String result = parseWithPlaceholder(str, o, p);
                if (result == null) { continue; }

                // Save
                str = result;
            }
        }

        // Yes
        return str;
    }

    /**
     * @param str String to parse
     * @param o Object that has the information
     * @param parser MythicPlaceholder to parse with
     *
     * @return The string now parsed using the provided placeholder.
     *         <p></p>
     *         <code>null</code> If there was any error or something.
     */
    @Nullable public static String parseWithPlaceholder(@NotNull String str, @NotNull Object o, @NotNull MythicPlaceholder parser) {

        // No placeholders no service
        if (!str.contains("%")) { return null; }

        // Cancel
        if (!parser.forUseWith(o)) { return null; }

        // Special case
        if (parser instanceof PlaceholderAPIHook && ((o instanceof OfflinePlayer))) {

            // Just parse through that one
            return parser.parse(str, o); }

        // Split into those, according to the parser's identifier
        @NotNull String builtIdentifier = "%" + parser.getMythicIdentifier() + "_";
        @NotNull String[] args = str.split(builtIdentifier);
        @NotNull StringBuilder built = new StringBuilder();
        boolean within = false;

        // Actually parse
        for (String arg : args) {

            // First term treated differently
            if (!within) {

                /*
                 * The very first arg is always irrelevant, it is
                 * never part of a placeholder and must be just added.
                 */
                within = true;
                built.append(arg); }

            // Skip
            if (arg == null) {

                /*
                 * This is theoretically impossible anyways
                 */

                continue; }

            // Ignore
            int percentEnd = arg.indexOf("%");
            if (percentEnd < 0) {

                /*
                 * This would be caused if the string ends in an incomplete
                 * placeholder, in which case we just append the arg and be done.
                 */
                built.append(builtIdentifier).append(arg);
                continue; }

            // What is the actual segment
            String parsing = arg.substring(0, percentEnd);

            // Parse
            String parsed = parser.parse(parsing, o);
            if (parsed == null) {

                /*
                 * Placeholder had an error, just leave it untouched.
                 */
                built.append(builtIdentifier).append(arg);
                continue; }

            // All right, insert the parsed in the place of the placeholder
            String rem = arg.substring(percentEnd + 1);
            built.append(parsed).append(rem);
        }

        // Yes
        return built.toString();
    }

    /**
     * All the registered placeholder parsers
     */
    @NotNull static final ArrayList<MythicPlaceholder> objectParsers = new ArrayList<>();

    /**
     * @param obj Object (Casted as required) of what you are trying to parse.
     *
     * @return The registered placeholder that should parse this object.
     */
    @NotNull public static ArrayList<MythicPlaceholder> getParserFor(@NotNull Object obj) {
        ArrayList<MythicPlaceholder> ret = new ArrayList<>();

        // For every placeholder
        for (MythicPlaceholder mp : objectParsers) {

            // Skip
            if (mp == null) { continue; }

            // CHeck cast
            if (mp.forUseWith(obj)) { ret.add(mp); }
        }

        // Find
        return ret;
    }

    /**
     * Activate your placeholder so it gets used everywhere
     *
     * @param placeholder Placeholder you made
     */
    public static void registerPlaceholder(@NotNull MythicPlaceholder placeholder) {

        // Prevent double registrations
        for (MythicPlaceholder mp : objectParsers) {
            if (mp.getMythicIdentifier().equals(placeholder.getMythicIdentifier())) {

                // Cancel
                return; } }

        // Add bruh
        objectParsers.add(placeholder); }
}

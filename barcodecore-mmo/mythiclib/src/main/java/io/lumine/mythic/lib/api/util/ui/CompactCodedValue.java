package io.lumine.mythic.lib.api.util.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * To read compact lists from user input, of different keys separated by semicolons.
 * <p></p>
 * Example:<p>
 *
 *     <code>fruit=apple;color=red;taste=sweet;amount=3</code>
 * </p>
 * <p></p>
 * Notice that that is a list of compact-coded values, where a sole
 * ccv is just <code>fruit=apple</code>, a string and its meaning,
 * a <b>code</b> and its <b>value</b>.
 * <p></p>
 * <code><b>code</b>=<b>value</b></code>.
 * <p></p>
 * Use <code><&sc></code> or <code><$sc></code> as semicolon placeholders,
 * that wont split into a list, when using {@link #getListFromString(String, FriendlyFeedbackProvider)}
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class CompactCodedValue {
    @NotNull String code, value;

    /**
     * The code that stands for the value
     */
    @NotNull public String getID() { return code; }

    /**
     * The value encoded by the code
     */
    @NotNull
    public String getValue() { return value; }

    /**
     * Creates a Compact Variable characterized by a code and a value it encodes for.
     */
    public CompactCodedValue(@NotNull String code, @NotNull String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * Gets all those compact variables encoded in string.
     * <p> </p>
     * Format:
     * <p>[ID]=[VALUE];[ID1]=[VALUE1];[ID2]=[VALUE2]</p>
     * <p></p>
     * Use <&sc> or <$sc> as placeholder for semicolons within values.
     *
     * @param ffp Tell the user why any entry did not parse into a compact coded value
     *
     * @return A list with all valid ID=VALUE pairs contained in the string.
     *         Empty at worst if nothing was in the correct format.
     */
    @NotNull public static ArrayList<CompactCodedValue> getListFromString(@Nullable String source, @Nullable FriendlyFeedbackProvider ffp) {

        // Return Value
        ArrayList<CompactCodedValue> ret = new ArrayList<>();

        // Early Quit
        if (source == null) {

            // Appendable Message
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "No string provided to parse list of CompactCodedValues. ");

            return ret;
        }
        if (!source.contains("=")) {

            // Appendable Message
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "String provided '$u{0}$b' has not a single $e=$b (syntax of CompactCodedValues). ", source);

            return ret;
        }

        // Split
        ArrayList<String> encoded = new ArrayList<>();
        if (source.contains(";")) { encoded.addAll(Arrays.asList(source.split(";"))); } else { encoded.add(source); }

        // Foreach
        for (String encode : encoded) {

            // Parse Attempt
            CompactCodedValue ccv = getFromString(encode, ffp);

            // Add if valid
            if (ccv != null) { ret.add(ccv); }
        }

        // Return thay
        return ret;
    }

    /**
     * Reads a code-value pair from a string
     * <p></p>
     * Format:
     * <p>[ID]=[VALUE]</p>
     * <p></p>
     * All <&sc> and <$sc> are replaced by semicolons.
     *
     * @return Will return null if its not in a valid format (has no <code>=</code>)
     */
    @Nullable
    public static CompactCodedValue getFromString(@Nullable String source, @Nullable FriendlyFeedbackProvider ffp) {

        // Early quit
        if (source == null) {

            // Appendable Message
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "No string provided to parse list of CompactCodedValues. ");

            return null;
        }

        int locationOfEquals = source.indexOf("=");
        if ((source.length() < 3) || (locationOfEquals < 1)) {

            // Appendable Message
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "Incorrect syntax in '$u{0}$b': Expected a Code=Value pair like $efruit=pear$b. ");

            return null;
        }

        // Get ID and Value
        String id = source.substring(0, locationOfEquals);
        String val = source.substring(locationOfEquals + 1);

        // Parse
        id = id.replace("<&sc>", ";").replace("<$sc>", ";");
        val = val.replace("<&sc>", ";").replace("<$sc>", ";");

        // Bake and return
        return new CompactCodedValue(id, val);
    }
}

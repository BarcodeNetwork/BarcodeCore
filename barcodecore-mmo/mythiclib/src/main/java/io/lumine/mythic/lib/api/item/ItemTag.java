package io.lumine.mythic.lib.api.item;

import com.google.gson.*;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  Forget about <b>nms</b>! You can add NBT to items through {@link NBTItem}, which
 *  is made up of a list of these <code>ItemTag</code>s.
 *  <p></p>
 *  The <b>path</b> is some address it will be written onto, it may be any string.
 *  <p>The <b>value</b> is whatever you are trying to encode</p>
 * <p></p>
 * Note that only the following types of value are guaranteed to be supported:
 * <p><b>+ </b> Integer
 * </p><b>+ </b> String
 * <p><b>+ </b> Boolean
 * </p><b>+ </b> Double
 * <p></p>
 * Hint: Use Google's {@link JsonObject} stuff to fit any data into a <code>String</code>,
 * there are a few utility methods in here for ease of use, using {@link JsonArray} for
 * lists for example.
 *
 * @author Gunging (utility methods)
 */
@SuppressWarnings({"unused"})
public class ItemTag {
    @NotNull private final String path;
    @NotNull private final Object value;

    /**
     * Note that only the following types of value are guaranteed to be supported:
     * <p><b>+ </b> Integer
     * </p><b>+ </b> String
     * <p><b>+ </b> Boolean
     * </p><b>+ </b> Double
     * @param path Some address that the value will be written onto.
     * @param value Whatever you are trying to save in an item's NBT
     */
    public ItemTag(@NotNull String path, @NotNull Object value) {
        this.path = path;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemTag)) { return false; }
        if (!((ItemTag) obj).getValue().equals(getValue())) { return false; }
        return ((ItemTag) obj).getPath().equals(getPath()); }

    @NotNull public String getPath() {
        return path;
    }
    @NotNull public Object getValue() {
        return value;
    }

    //region Utility Methods

    /**
     * Used to easily get the tag you are searching from
     */
    @Nullable public static ItemTag getTagAtPath(@NotNull String path, @NotNull ArrayList<ItemTag> from) {

        // Look into each tag
        for (ItemTag str : from) {

            // Not null eh
            if (str != null) {

                // Correct path?
                if (path.equals(str.getPath())) {

                    // Ya that one
                    return str;
                }
            }
        }
        return null;
    }

    /**
     * If the NBT item has such tag, it returns it as a workable MythicLib Item Tag.
     * @param expect It is ambiguous between BOOLEAN and INTEGER, so cant just make it smart.
     *               Please indicate what you expect this path to provide.
     */
    @Nullable public static ItemTag getTagAtPath(@NotNull String path, @NotNull NBTItem from, @NotNull SupportedNBTTagValues expect) {

        // Not there? Not loaded!
        if (!from.hasTag(path)) { return null; }

        // Obtain based on what to expect
        switch (expect) {
            case INTEGER:
                return new ItemTag(path, from.getInteger(path));
            case STRING:
                return new ItemTag(path, from.getString(path));
            case DOUBLE:
                return new ItemTag(path, from.getDouble(path));
            default:
                return new ItemTag(path, from.getBoolean(path));
        }
    }

    /**
     * Easily get an ItemTag from a list.
     * <p></p>
     * It basically makes it a JSON array and all that, don't worry about that anymore :p
     * <p></p>
     * This can be reversed with  {@link #getStringListFromTag(ItemTag)}
     */
    @NotNull public static ItemTag fromStringList(@NotNull String path, @NotNull List<String> list) {

        // Make JSON Array, Fresh
        JsonArray array = new JsonArray();

        // Add every string from the list
        list.forEach(array::add);

        // Return a built tag
        return toItemTag(path, array);
    }

    /**
     * Easily get the String List encoded inside an ItemTag.
     * <p></p>
     * This is the reverse of {@link #fromStringList(String, List)}
     */
    @NotNull public static ArrayList<String> getStringListFromTag(@NotNull ItemTag tagThatContainsAnEncodedList) {

        // Parse as array
        JsonArray parsed = toJsonArray(tagThatContainsAnEncodedList);

        // Well
        ArrayList<String> ret = new ArrayList<>();
        parsed.forEach(str -> ret.add(str.getAsString()));

        // Return that
        return ret;
    }

    /**
     * Easily get an ItemTag from a JSON Array.
     */
    @NotNull public static JsonArray toJsonArray(@NotNull ItemTag someTag) {

        // Make JSON Parser
        JsonParser pJSON = new JsonParser();

        // Parse as array
        return pJSON.parse((String) someTag.getValue()).getAsJsonArray();
    }

    /**
     * Easily get an ItemTag from a JSON Array.
     */
    @NotNull public static ItemTag toItemTag(@NotNull String pathToAssign, @NotNull JsonArray someJsonArray) {

        // Return a built tag
        return new ItemTag(pathToAssign, someJsonArray.toString());
    }

    /**
     * To serialize a list of Item Tags, just call <code>toString()</code>
     * from your finished JSON Object!
     * <p></p>
     * Note that only the following types are supported:
     * <p><b>+ </b> Integer
     * </p><b>+ </b> String
     * <p><b>+ </b> Boolean
     * </p><b>+ </b> Double
     * <p><b>+ </b> List of Strings
     * <p></p>
     * This method is somewhat heavy, and not meant to use tags you'd read and write very often.
     * In fact, you cant even read them correctly normally (I've had to add a codification to
     * distinguish Booleans from Integers), you must use {@link #decompressTags(JsonArray)}
     */
    @NotNull public static JsonArray compressTags(@NotNull ArrayList<ItemTag> tags) {
        JsonArray JsonList = new JsonArray();

        // For Each ItemTag
        for (ItemTag t : tags) {

            // Not null

            // Make new JSON Object
            JsonObject tagAsJson = new JsonObject();

            // As number
            if (t.getValue() instanceof Integer) { tagAsJson.addProperty(t.getPath() + compression_INTEGER, (Integer) t.getValue()); }
            else if (t.getValue() instanceof Double) { tagAsJson.addProperty(t.getPath() + compression_DOUBLE, (Double) t.getValue()); }
            else if (t.getValue() instanceof String) { tagAsJson.addProperty(t.getPath() + compression_STRING, (String) t.getValue()); }
            else if (t.getValue() instanceof Boolean) { tagAsJson.addProperty(t.getPath() + compression_BOOLEAN, (Boolean) t.getValue()); }
            else if (t.getValue() instanceof List) {

                // Dig even deeper...
                JsonArray JSONception = new JsonArray();

                // Must be a supported-data list tho
                for (Object e : (List) t.getValue()) {

                    // Add I guess
                    if (e instanceof Number) { JSONception.add(SilentNumbers.readableRounding(((Number) t.getValue()).doubleValue(), 3)); }
                    else if (e instanceof String) { JSONception.add((String) t.getValue()); }
                    else if (e instanceof Boolean) { JSONception.add((Boolean) t.getValue()); }
                }

                // Put that in there
                tagAsJson.add(t.getPath() + compression_SLIST, JSONception);
            }

            // Add to the list itself?
            JsonList.add(tagAsJson);
        }

        // Thats it
        return JsonList;
    }

    /**
     * Meant to revert {@link #compressTags(ArrayList)} so it will do exactly that.
     */
    @NotNull public static ArrayList<ItemTag> decompressTags(@NotNull JsonArray compressedTags) {

        // All right.
        ArrayList<ItemTag> returningList = new ArrayList<>();

        for (JsonElement compressedTag : compressedTags) {

            // It should be an object
            if (compressedTag.isJsonObject()) {

                // Operate as such
                JsonObject cTag = compressedTag.getAsJsonObject();

                // The tag's properties
                Set<Map.Entry<String, JsonElement>> entries = cTag.entrySet();

                // This should only have a size of ONE
                for (Map.Entry<String, JsonElement> entry : entries) {

                    // The path is the string
                    String rawPath = entry.getKey();

                    // Split the last chars
                    String compressed_extension = rawPath.substring(rawPath.length() - compression_SLIST.length());
                    String path = rawPath.substring(0, rawPath.length() - compression_SLIST.length());

                    // Attempt to get the path
                    JsonElement e = entry.getValue();

                    // Object
                    Object value = null;

                    // It really must be primitive
                    if (e.isJsonPrimitive()) {

                        // As primitive
                        JsonPrimitive p = e.getAsJsonPrimitive();

                        // What to decompress as?
                        switch (compressed_extension) {
                            case compression_BOOLEAN:
                                // Object is boolean
                                value = p.getAsBoolean();
                                break;
                            case compression_DOUBLE:
                                // Object is boolean
                                value = p.getAsDouble();
                                break;
                            case compression_INTEGER:
                                // Object is boolean
                                value = p.getAsInt();
                                break;
                            case compression_STRING:
                                // Object is boolean
                                value = p.getAsString();
                                break;
                            case compression_SLIST:
                                // Object is boolean
                                value = p.getAsJsonArray();
                                break;
                        }

                    // Or it may be an array :thinking:
                    } else if (e.isJsonArray()) {

                        /*
                         *  If this method is receiving a JSON Array, it must be a Stat that encodes for a String List.
                         */

                        // Get Json Array
                        JsonArray a = e.getAsJsonArray();

                        // Make a new arraylist
                        ArrayList<String> ret = new ArrayList<>();

                        // Whats the contents therein?
                        for (JsonElement str : a) {

                            // Must be a string
                            ret.add(str.getAsString());
                        }

                        // Thats the value
                        value = ret;
                    }

                    // Make tag and add
                    if (value != null) {

                        // Add thisse tag
                        returningList.add(new ItemTag(path, value));
                    }
                }
            }
        }

        // Thats it
        return returningList;
    }

    /*
     *   Codes that tell the decompressor how to decompress such tag.
     *
     *   They must all have the same length.
     */
    static final String compression_STRING = "_ñstr";
    static final String compression_DOUBLE = "_ñdbl";
    static final String compression_BOOLEAN = "_ñbol";
    static final String compression_INTEGER = "_ñint";
    static final String compression_SLIST = "_ñlst";

    //endregion
}


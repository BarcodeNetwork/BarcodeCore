package io.lumine.mythic.lib.api.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * To know what to expect when reading NBT, because
 * minecraft saves booleans as ZERO or ONE which is
 * ambiguous with INTEGER.
 */
public enum SupportedNBTTagValues {

    /**
     * A segment of text. May be a {@link JsonElement#toString()}, who knows?
     * <p></p>
     * In the latter case, just use {@link JsonParser#parse(String)} to read.
     */
    STRING,

    /**
     * A number.
     */
    DOUBLE,

    /**
     * Either true or false
     */
    BOOLEAN,

    /**
     * A number with no decimal values.
     */
    INTEGER
}

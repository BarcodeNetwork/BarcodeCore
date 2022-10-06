package io.lumine.mythic.lib.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.lang.Validate;

public class MMOLineConfig {
    private final String key, value;
    private final String[] args;
    private final JsonObject json;

    /**
     * Reads a JSON config from a string line. These are used everywhere in
     * MMOItems/MMOCore configs to register quest triggers, objectives, crafting
     * ingredients... Throws IAE
     *
     * @param value
     *            The string to parse
     */
    public MMOLineConfig(String value) {
        this.value = value;

        /*
         * if there is no config, no need to parse the json object. split,
         * define key and find arg
         */
        if (!value.contains("{") || !value.contains("}")) {
            String[] split = value.split(" ");
            key = split[0];
            args = split.length > 1 ? value.replace(key + " ", "").split(" ") : new String[0];
            json = new JsonObject();
            return;
        }

        /*
         * load json and extra args
         */
        int begin = value.indexOf("{"), end = value.lastIndexOf("}") + 1;
        key = value.substring(0, begin);

        try {
            json = new JsonParser().parse(value.substring(begin, end)).getAsJsonObject();
        } catch (JsonParseException exception) {
            throw new IllegalArgumentException("Could not load config");
        }

        String format = value.substring(Math.min(value.length(), end + 1));
        args = format.isEmpty() ? new String[0] : format.split(" ");
    }

    /**
     * @return Extra arguments outside the config brackets. These are used for
     *         instance in MMOCore drop items for drop chance, item amounts and
     *         drop item weights.
     */
    public String[] args() {
        return args;
    }

    /**
     * @return The string key in front of the brackets
     */
    public String getKey() {
        return key;
    }

    public String getString(String path) {
        return json.get(path).getAsString();
    }

    public String getString(String path, String def) {
        return json.has(path) ? getString(path) : def;
    }

    public double getDouble(String path) {
        return json.get(path).getAsDouble();
    }

    public double getDouble(String path, double def) {
        return json.has(path) ? getDouble(path) : def;
    }

    public int getInt(String path) {
        return json.get(path).getAsInt();
    }

    public int getInt(String path, int def) {
        return json.has(path) ? getInt(path) : def;
    }

    public long getLong(String path) {
        return json.get(path).getAsLong();
    }

    public boolean getBoolean(String path) {
        return json.get(path).getAsBoolean();
    }

    public boolean getBoolean(String path, boolean def) {
        return json.has(path) ? getBoolean(path) : def;
    }

    public boolean contains(String path) {
        return json.has(path);
    }

    /**
     * Throws IAE if the config is missing any of these paths
     *
     * @param paths
     *            The config paths to check
     */
    public void validate(String... paths) {
        for (String path : paths)
            Validate.isTrue(contains(path), "Config is missing parameter '" + path + "'");
    }

    /**
     * Throws IAE if the config has less than X parameters
     *
     * @param count
     *            The amount of arguments
     */
    public void validateArgs(int count) {
        Validate.isTrue(args.length >= count, "Config must have at least " + count + " parameters");
    }

    @Override
    public String toString() {
        return value;
    }
}

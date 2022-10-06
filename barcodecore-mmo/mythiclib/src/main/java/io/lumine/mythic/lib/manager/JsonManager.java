package io.lumine.mythic.lib.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class JsonManager {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public <T> T parse(String s, Class<T> c) {
        return gson.fromJson(s, c);
    }

    public String toString(JsonElement json) {
        return gson.toJson(json);
    }
}

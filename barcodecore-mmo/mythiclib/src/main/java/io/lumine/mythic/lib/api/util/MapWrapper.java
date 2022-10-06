package io.lumine.mythic.lib.api.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapWrapper<T, U> {
    private final Map<T, U> map = new HashMap<>();

    public U getValue(T key) {
        return this.map.get(key);
    }

    public void setValue(T key, U value) {
        this.map.put(key, value);
    }

    public boolean hasKey(T key) {
        return this.map.containsKey(key);
    }

    public boolean hasValue(U value) {
        return this.map.containsValue(value);
    }

    public int size() {
        return this.map.size();
    }

    public Collection<U> getValues() {
        return this.map.values();
    }

    public Set<T> getKeys() {
        return this.map.keySet();
    }

    public Set<Map.Entry<T, U>> getEntries() {
        return this.map.entrySet();
    }

    public Map<T, U> getMap() {
        return this.map;
    }

    @Override
    public String toString() {
        return this.map.toString();
    }
}

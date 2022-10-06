package io.lumine.mythic.lib.api.stat.modifier;

/**
 * Used when a stat modifier does something extra specific (eg registers a
 * Listener) and needs to perform a specific script whenever the modifier is
 * removed from a StatMap
 *
 * @author indyuce
 */
public interface Closable {

    /**
     * Called whenever a stat modifier is removed from a StatInstance or
     * expires. This method should be used to unregister eventual listeners,
     * cancel repeating tasks, etc.
     */
    void close();
}

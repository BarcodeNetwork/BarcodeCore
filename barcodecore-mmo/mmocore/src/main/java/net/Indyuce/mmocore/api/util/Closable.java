package net.Indyuce.mmocore.api.util;

/**
 * Indicates that a class temporarily registers something
 * such as a Bukkit event, which needs to be unregistered
 * when the class is finally garbage collected.
 */
public interface Closable {

    /**
     * Method that must be called before the class
     * is garbage collected
     */
    void close();
}

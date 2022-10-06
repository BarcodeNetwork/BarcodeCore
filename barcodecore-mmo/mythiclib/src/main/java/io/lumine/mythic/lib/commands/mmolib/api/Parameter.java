package io.lumine.mythic.lib.commands.mmolib.api;

import org.bukkit.Bukkit;

import java.util.List;
import java.util.function.BiConsumer;

public class Parameter {
    private final String key;
    private final BiConsumer<CommandTreeExplorer, List<String>> autoComplete;

    // public static final Parameter PROFESSION = new
    // Parameter("<profession/main>", list -> {
    // MMOCore.plugin.professionManager.getAll().forEach(profession ->
    // list.add(profession.getId()));
    // list.add("main");
    // });
    public static final Parameter PLAYER = new Parameter("<player>",
            (explorer, list) -> Bukkit.getOnlinePlayers().forEach(online -> list.add(online.getName())));
    // public static final Parameter QUEST = new Parameter("<quest>",
    // list -> MMOCore.plugin.questManager.getAll().forEach(quest ->
    // list.add(quest.getId())));
    public static final Parameter PLAYER_OPTIONAL = new Parameter("(player)",
            (explorer, list) -> Bukkit.getOnlinePlayers().forEach(online -> list.add(online.getName())));
    public static final Parameter AMOUNT = new Parameter("<amount>", (explorer, list) -> {
        for (int j = 0; j <= 10; j++)
            list.add("" + j);
    });

    public Parameter(String key, BiConsumer<CommandTreeExplorer, List<String>> autoComplete) {
        this.key = key;
        this.autoComplete = autoComplete;
    }

    public String getKey() {
        return key;
    }

    public void autoComplete(CommandTreeExplorer explorer, List<String> list) {
        autoComplete.accept(explorer, list);
    }
}

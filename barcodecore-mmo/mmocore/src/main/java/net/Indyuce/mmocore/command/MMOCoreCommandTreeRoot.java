package net.Indyuce.mmocore.command;

import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeRoot;
import io.lumine.mythic.lib.commands.mmolib.api.Parameter;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.command.rpg.ReloadCommandTreeNode;
import net.Indyuce.mmocore.command.rpg.admin.AdminCommandTreeNode;
import net.Indyuce.mmocore.command.rpg.booster.BoosterCommandTreeNode;
import net.Indyuce.mmocore.command.rpg.debug.DebugCommandTreeNode;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public class MMOCoreCommandTreeRoot extends CommandTreeRoot implements CommandExecutor, TabCompleter {
	public static final Parameter PROFESSION = new Parameter("<profession/main>", (explorer, list) -> {
		MMOCore.plugin.professionManager.getAll().forEach(profession -> list.add(profession.getId()));
		list.add("main");
	});

	public MMOCoreCommandTreeRoot() {
		super("mmocore", "mmocore.admin");

		addChild(new ReloadCommandTreeNode(this));
		addChild(new AdminCommandTreeNode(this));
		addChild(new DebugCommandTreeNode(this));
		addChild(new BoosterCommandTreeNode(this));
	}
}

package net.Indyuce.mmocore.command.rpg.admin;

import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.command.CommandSender;

public class AdminCommandTreeNode extends CommandTreeNode {
	public AdminCommandTreeNode(CommandTreeNode parent) {
		super(parent, "admin");

		addChild(new NoCooldownCommandTreeNode(this));
		addChild(new ResetCommandTreeNode(this));
		addChild(new InfoCommandTreeNode(this));
		addChild(new ClassCommandTreeNode(this));
		addChild(new ForceClassCommandTreeNode(this));

		addChild(new ExperienceCommandTreeNode(this));
		addChild(new LevelCommandTreeNode(this));
		addChild(new AttributeCommandTreeNode(this));

		addChild(new PointsCommandTreeNode("skill", this, PlayerData::setSkillPoints, PlayerData::giveSkillPoints, PlayerData::getSkillPoints));
		addChild(new PointsCommandTreeNode("attribute", this, PlayerData::setAttributePoints, PlayerData::giveAttributePoints, PlayerData::getAttributePoints));
		addChild(new PointsCommandTreeNode("attr-realloc", this, PlayerData::setAttributeReallocationPoints, PlayerData::giveAttributeReallocationPoints, PlayerData::getAttributeReallocationPoints));
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		return CommandResult.THROW_USAGE;
	}
}

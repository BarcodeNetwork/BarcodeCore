package net.Indyuce.mmocore.skill;

import net.Indyuce.mmocore.skill.metadata.SkillMetadata;
import org.bukkit.event.Listener;

public class PassiveSkill extends Skill implements Listener {
    public PassiveSkill() {
        super();
    }

    public PassiveSkill(String id) {
        super(id);
    }

    @Override
    public SkillMetadata whenCast(CasterMetadata casterMeta, SkillInfo skill) {
        return new SkillMetadata(casterMeta, skill);
    }
}

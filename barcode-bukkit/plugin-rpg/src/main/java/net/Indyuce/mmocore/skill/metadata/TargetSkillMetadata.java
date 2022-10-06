package net.Indyuce.mmocore.skill.metadata;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.MMORayTraceResult;
import io.lumine.mythic.lib.comp.target.InteractionType;
import net.Indyuce.mmocore.api.util.MMOCoreUtils;
import net.Indyuce.mmocore.skill.CasterMetadata;
import net.Indyuce.mmocore.skill.Skill.SkillInfo;
import org.bukkit.entity.LivingEntity;

public class TargetSkillMetadata extends SkillMetadata {
    private LivingEntity target;

    /**
     * @param caster Player casting the skill
     * @param skill  Skill being cast
     * @param range  Skill raycast range
     */
    public TargetSkillMetadata(CasterMetadata caster, SkillInfo skill, double range) {
        this(caster, skill, range, InteractionType.OFFENSE_SKILL);
    }

    /**
     * @param caster Player casting the skill
     * @param skill  Skill being cast
     * @param range  Skill raycast range
     * @param interaction If the skill is a friendly or offense skill. This determines if it
     *                    can be cast on party members or not.
     */
    public TargetSkillMetadata(CasterMetadata caster, SkillInfo skill, double range, InteractionType interaction) {
        super(caster, skill);

        if (isSuccessful()) {
            MMORayTraceResult result = MythicLib.plugin.getVersion().getWrapper().rayTrace(caster.getPlayer(), range, entity -> MMOCoreUtils.canTarget(caster.getPlayerData(), entity, interaction));
            if (!result.hasHit())
                abort();
            else
                target = result.getHit();
        }
    }

    public LivingEntity getTarget() {
        return target;
    }
}

package com.prewarm.diagnosis.agent.skill;

import java.util.Objects;

/**
 * Skill Router 的选择结果。
 */
public record SkillSelection(
        SkillId skillId,
        boolean requiresTriage,
        String reason
) {

    public SkillSelection {
        Objects.requireNonNull(skillId, "skillId must not be null");
        Objects.requireNonNull(reason, "reason must not be null");
    }
}

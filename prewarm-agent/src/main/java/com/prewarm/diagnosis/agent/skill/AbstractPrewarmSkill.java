package com.prewarm.diagnosis.agent.skill;

import java.util.Objects;

/**
 * 预热诊断 Skill 的公共基类。
 */
public abstract class AbstractPrewarmSkill implements AgentSkill {

    private final SkillDefinition definition;

    protected AbstractPrewarmSkill(SkillDefinition definition) {
        this.definition = Objects.requireNonNull(definition, "definition must not be null");
    }

    @Override
    public final SkillDefinition definition() {
        return definition;
    }
}

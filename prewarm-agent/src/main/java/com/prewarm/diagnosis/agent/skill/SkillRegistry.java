package com.prewarm.diagnosis.agent.skill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Agent Runtime 内的 Skill 注册表。
 *
 * <p>注册表只负责管理可用 Skill，不负责根据自然语言做路由决策。</p>
 */
public final class SkillRegistry {

    private final Map<SkillId, AgentSkill> skills;

    public SkillRegistry(Collection<? extends AgentSkill> skills) {
        Objects.requireNonNull(skills, "skills must not be null");

        Map<SkillId, AgentSkill> entries = new LinkedHashMap<>();
        for (AgentSkill skill : skills) {
            Objects.requireNonNull(skill, "skill must not be null");
            SkillId skillId = skill.definition().id();
            if (entries.putIfAbsent(skillId, skill) != null) {
                throw new IllegalArgumentException("Duplicate Skill id: " + skillId);
            }
        }
        this.skills = Collections.unmodifiableMap(entries);
    }

    public Optional<AgentSkill> find(SkillId skillId) {
        return Optional.ofNullable(
                skills.get(Objects.requireNonNull(skillId, "skillId must not be null")));
    }

    public List<AgentSkill> all() {
        return new ArrayList<>(skills.values());
    }
}

package com.prewarm.diagnosis.agent.skill;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Skill 中一个诊断阶段的静态定义。
 *
 * <p>当前只描述阶段、候选 Tool 和停止条件，实际执行由后续 Workflow Executor 负责。</p>
 */
public record SkillStageDefinition(
        String name,
        List<String> toolNames,
        boolean parallel,
        Set<String> stopConditions
) {

    public SkillStageDefinition {
        Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        toolNames = List.copyOf(Objects.requireNonNull(toolNames, "toolNames must not be null"));
        stopConditions = Set.copyOf(
                Objects.requireNonNull(stopConditions, "stopConditions must not be null"));
    }
}

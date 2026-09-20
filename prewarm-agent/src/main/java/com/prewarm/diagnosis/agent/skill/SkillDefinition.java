package com.prewarm.diagnosis.agent.skill;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Skill 的受控元数据。
 *
 * <p>该定义承载场景触发条件、上下文要求、Tool 白名单、阶段关系、证据要求和切换边界，
 * 不允许模型绕过 Runtime 直接构造线上查询。</p>
 */
public record SkillDefinition(
        SkillId id,
        String displayName,
        Set<String> triggerConditions,
        Set<String> requiredContextFields,
        Set<String> allowedTools,
        List<SkillStageDefinition> stages,
        Set<String> evidenceRequirements,
        Set<String> stopConditions,
        Set<SkillId> switchTargets
) {

    public SkillDefinition {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        if (displayName.isBlank()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }
        triggerConditions = Set.copyOf(
                Objects.requireNonNull(triggerConditions, "triggerConditions must not be null"));
        requiredContextFields = Set.copyOf(
                Objects.requireNonNull(requiredContextFields, "requiredContextFields must not be null"));
        allowedTools = Set.copyOf(
                Objects.requireNonNull(allowedTools, "allowedTools must not be null"));
        stages = List.copyOf(Objects.requireNonNull(stages, "stages must not be null"));
        for (SkillStageDefinition stage : stages) {
            if (!allowedTools.containsAll(stage.toolNames())) {
                throw new IllegalArgumentException(
                        "Stage contains a Tool outside the Skill allowlist: " + stage.name());
            }
        }
        evidenceRequirements = Set.copyOf(
                Objects.requireNonNull(evidenceRequirements, "evidenceRequirements must not be null"));
        stopConditions = Set.copyOf(
                Objects.requireNonNull(stopConditions, "stopConditions must not be null"));
        switchTargets = Set.copyOf(
                Objects.requireNonNull(switchTargets, "switchTargets must not be null"));
    }
}

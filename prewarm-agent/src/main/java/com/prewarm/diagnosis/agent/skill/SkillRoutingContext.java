package com.prewarm.diagnosis.agent.skill;

import java.util.Objects;
import java.util.Set;

/**
 * Skill Router 使用的最小输入。
 *
 * <p>自然语言提取和会话上下文合并由 Runtime 其他组件负责，Router 只消费结构化后的路由输入。</p>
 */
public record SkillRoutingContext(
        String userMessage,
        Set<String> extractedSignals,
        boolean contextComplete
) {

    public SkillRoutingContext {
        Objects.requireNonNull(userMessage, "userMessage must not be null");
        extractedSignals = Set.copyOf(
                Objects.requireNonNull(extractedSignals, "extractedSignals must not be null"));
    }
}

package com.prewarm.diagnosis.agent.skill;

/**
 * Agent Runtime 中的 Skill 路由边界。
 *
 * <p>后续根据结构化上下文区分明确场景和模糊场景：
 * 明确场景进入专项 Skill，模糊场景先进入预热问题初筛 Skill。</p>
 */
public interface SkillRouter {

    SkillSelection route(SkillRoutingContext context);
}

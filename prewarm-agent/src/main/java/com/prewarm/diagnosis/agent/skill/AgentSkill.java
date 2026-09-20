package com.prewarm.diagnosis.agent.skill;

/**
 * Agent Runtime 面向具体诊断场景的 Skill 边界。
 *
 * <p>Skill 描述受控的诊断剧本，不直接负责 MCP 协议通信或外部系统访问。</p>
 */
public interface AgentSkill {

    SkillDefinition definition();
}

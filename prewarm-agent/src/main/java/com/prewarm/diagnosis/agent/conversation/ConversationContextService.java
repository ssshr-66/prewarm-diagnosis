package com.prewarm.diagnosis.agent.conversation;

/**
 * 会话上下文服务。
 *
 * <p>后续负责读取 MySQL 中的会话、合并本轮消息、识别缺失字段，
 * 决定继续追问还是创建诊断 Run。</p>
 */
public final class ConversationContextService {
    // TODO: 实现上下文合并、冲突处理和会话状态迁移。
}

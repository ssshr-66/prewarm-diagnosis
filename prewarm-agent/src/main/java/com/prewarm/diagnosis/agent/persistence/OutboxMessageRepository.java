package com.prewarm.diagnosis.agent.persistence;

/**
 * 飞书 Outbox 持久化边界。
 *
 * <p>后续用 MySQL 保存待发送消息和重试状态，避免诊断完成但结果丢失。</p>
 */
public final class OutboxMessageRepository {
    // TODO: 实现 outbox_message 的领取、发送状态和失败重试。
}

package com.prewarm.diagnosis.agent.alert;

/**
 * 告警事件接入边界（暂缓）。
 *
 * <p>当前版本不直接接入告警平台，先由组内同学把告警现象转发到飞书机器人；
 * 后续确认告警平台协议后，再接入同一套会话和诊断流程。</p>
 */
public final class AlertEventController {
    // TODO: 后续确认告警平台协议后，再实现 Webhook 或消息订阅接入。
}

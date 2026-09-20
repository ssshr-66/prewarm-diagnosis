package com.prewarm.diagnosis.agent.skill.mq;

import com.prewarm.diagnosis.agent.skill.AbstractPrewarmSkill;
import com.prewarm.diagnosis.agent.skill.SkillDefinition;
import com.prewarm.diagnosis.agent.skill.SkillId;
import com.prewarm.diagnosis.agent.skill.SkillStageDefinition;

import java.util.List;
import java.util.Set;

/**
 * 消息投递与消费诊断 Skill。
 */
public final class MqDeliverySkill extends AbstractPrewarmSkill {

    private static final SkillDefinition DEFINITION = new SkillDefinition(
            SkillId.MQ_DELIVERY,
            "消息投递与消费诊断",
            Set.of("topic_missing", "consumer_missing", "message_not_consumed", "consumer_lag",
                    "retry_or_dead_letter"),
            Set.of("message_id", "topic", "cluster", "time_range"),
            Set.of(
                    "mq.query_topic",
                    "mq.query_consumer",
                    "mq.query_message",
                    "mq.query_consumer_lag",
                    "mq.query_retry_or_dead_letter"
            ),
            List.of(
                    new SkillStageDefinition(
                            "topic_and_consumer",
                            List.of("mq.query_topic", "mq.query_consumer"),
                            true,
                            Set.of("topic_not_found", "consumer_not_found", "consumer_ready")
                    ),
                    new SkillStageDefinition(
                            "message_consumption",
                            List.of("mq.query_message"),
                            false,
                            Set.of("message_not_found", "message_consumed")
                    ),
                    new SkillStageDefinition(
                            "lag_and_failure",
                            List.of("mq.query_consumer_lag", "mq.query_retry_or_dead_letter"),
                            true,
                            Set.of("lag_confirmed", "dead_letter_confirmed")
                    )
            ),
            Set.of("topic_status", "consumer_status", "message_status", "lag", "retry_or_dead_letter"),
            Set.of("message_not_found", "message_consumed", "dead_letter_confirmed"),
            Set.of(SkillId.PREWARM_TRIAGE)
    );

    public MqDeliverySkill() {
        super(DEFINITION);
    }
}

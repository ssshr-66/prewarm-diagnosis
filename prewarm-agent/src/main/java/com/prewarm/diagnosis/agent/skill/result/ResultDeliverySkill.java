package com.prewarm.diagnosis.agent.skill.result;

import com.prewarm.diagnosis.agent.skill.AbstractPrewarmSkill;
import com.prewarm.diagnosis.agent.skill.SkillDefinition;
import com.prewarm.diagnosis.agent.skill.SkillId;
import com.prewarm.diagnosis.agent.skill.SkillStageDefinition;

import java.util.List;
import java.util.Set;

/**
 * 结果消息回传诊断 Skill。
 */
public final class ResultDeliverySkill extends AbstractPrewarmSkill {

    private static final SkillDefinition DEFINITION = new SkillDefinition(
            SkillId.RESULT_DELIVERY,
            "结果消息回传诊断",
            Set.of("result_missing", "result_not_consumed", "prewarm_completed_without_result"),
            Set.of("message_id", "cluster", "time_range"),
            Set.of(
                    "cache_manager.query_task",
                    "cache_manager.query_log",
                    "mq.query_message",
                    "mq.query_consumer",
                    "mq.query_retry_or_dead_letter"
            ),
            List.of(
                    new SkillStageDefinition(
                            "task_completion",
                            List.of("cache_manager.query_task", "cache_manager.query_log"),
                            true,
                            Set.of("task_incomplete", "result_generation_unknown", "task_completed")
                    ),
                    new SkillStageDefinition(
                            "result_message",
                            List.of(
                                    "mq.query_message",
                                    "mq.query_consumer",
                                    "mq.query_retry_or_dead_letter"
                            ),
                            true,
                            Set.of("result_missing", "result_not_consumed", "result_consumed")
                    )
            ),
            Set.of("task_completion", "result_generation", "result_message_status", "consumer_status"),
            Set.of("result_missing", "result_not_consumed", "result_consumed"),
            Set.of(SkillId.PREWARM_TRIAGE, SkillId.MQ_DELIVERY)
    );

    public ResultDeliverySkill() {
        super(DEFINITION);
    }
}

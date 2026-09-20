package com.prewarm.diagnosis.agent.skill.triage;

import com.prewarm.diagnosis.agent.skill.AbstractPrewarmSkill;
import com.prewarm.diagnosis.agent.skill.SkillDefinition;
import com.prewarm.diagnosis.agent.skill.SkillId;
import com.prewarm.diagnosis.agent.skill.SkillStageDefinition;

import java.util.List;
import java.util.Set;

/**
 * 预热问题快速初筛 Skill。
 */
public final class PrewarmTriageSkill extends AbstractPrewarmSkill {

    private static final SkillDefinition DEFINITION = new SkillDefinition(
            SkillId.PREWARM_TRIAGE,
            "预热问题快速初筛",
            Set.of("prewarm_slow", "prewarm_no_result", "unknown_stage"),
            Set.of("message_id", "topic", "cluster", "time_range"),
            Set.of(
                    "mq.query_message",
                    "cache_manager.query_event",
                    "cache_manager.query_task"
            ),
            List.of(
                    new SkillStageDefinition(
                            "message_baseline",
                            List.of("mq.query_message"),
                            false,
                            Set.of("message_missing", "message_status_known")
                    ),
                    new SkillStageDefinition(
                            "task_baseline",
                            List.of("cache_manager.query_event", "cache_manager.query_task"),
                            true,
                            Set.of("stage_identified", "insufficient_context")
                    )
            ),
            Set.of("message_status", "task_status", "stage_hint"),
            Set.of("stage_identified", "need_more_context"),
            Set.of(
                    SkillId.MQ_DELIVERY,
                    SkillId.TASK_LIFECYCLE,
                    SkillId.LOAD_EXECUTION,
                    SkillId.WORKER_READ,
                    SkillId.METADATA_CONSISTENCY,
                    SkillId.RESULT_DELIVERY
            )
    );

    public PrewarmTriageSkill() {
        super(DEFINITION);
    }
}

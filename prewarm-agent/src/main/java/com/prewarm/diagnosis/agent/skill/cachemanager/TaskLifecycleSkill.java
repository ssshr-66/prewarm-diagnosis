package com.prewarm.diagnosis.agent.skill.cachemanager;

import com.prewarm.diagnosis.agent.skill.AbstractPrewarmSkill;
import com.prewarm.diagnosis.agent.skill.SkillDefinition;
import com.prewarm.diagnosis.agent.skill.SkillId;
import com.prewarm.diagnosis.agent.skill.SkillStageDefinition;

import java.util.List;
import java.util.Set;

/**
 * Cache Manager 任务生命周期诊断 Skill。
 */
public final class TaskLifecycleSkill extends AbstractPrewarmSkill {

    private static final SkillDefinition DEFINITION = new SkillDefinition(
            SkillId.TASK_LIFECYCLE,
            "Cache Manager 任务生命周期诊断",
            Set.of("event_without_task", "task_not_progressing", "queue_blocked", "task_retrying"),
            Set.of("message_id", "cluster", "time_range"),
            Set.of(
                    "cache_manager.query_apollo_config",
                    "cache_manager.query_event",
                    "cache_manager.query_task",
                    "cache_manager.query_task_history",
                    "cache_manager.query_log"
            ),
            List.of(
                    new SkillStageDefinition(
                            "event_and_configuration",
                            List.of("cache_manager.query_apollo_config", "cache_manager.query_event"),
                            true,
                            Set.of("event_missing", "configuration_constraint_found")
                    ),
                    new SkillStageDefinition(
                            "task_progress",
                            List.of(
                                    "cache_manager.query_task",
                                    "cache_manager.query_task_history",
                                    "cache_manager.query_log"
                            ),
                            true,
                            Set.of("task_not_created", "task_stalled", "task_failed")
                    )
            ),
            Set.of("configuration_constraints", "event_status", "task_status", "task_history", "log_evidence"),
            Set.of("task_not_created", "task_stalled", "task_failed"),
            Set.of(SkillId.PREWARM_TRIAGE)
    );

    public TaskLifecycleSkill() {
        super(DEFINITION);
    }
}

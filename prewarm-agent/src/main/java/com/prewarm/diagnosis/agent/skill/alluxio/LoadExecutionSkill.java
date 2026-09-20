package com.prewarm.diagnosis.agent.skill.alluxio;

import com.prewarm.diagnosis.agent.skill.AbstractPrewarmSkill;
import com.prewarm.diagnosis.agent.skill.SkillDefinition;
import com.prewarm.diagnosis.agent.skill.SkillId;
import com.prewarm.diagnosis.agent.skill.SkillStageDefinition;

import java.util.List;
import java.util.Set;

/**
 * Alluxio Load 执行诊断 Skill。
 */
public final class LoadExecutionSkill extends AbstractPrewarmSkill {

    private static final SkillDefinition DEFINITION = new SkillDefinition(
            SkillId.LOAD_EXECUTION,
            "Alluxio Load 执行诊断",
            Set.of("load_not_started", "load_not_progressing", "load_failed"),
            Set.of("path", "cluster", "time_range"),
            Set.of(
                    "alluxio.query_job",
                    "observability.query_xray_metric",
                    "observability.query_grafana_metric",
                    "alluxio.query_worker"
            ),
            List.of(
                    new SkillStageDefinition(
                            "load_metrics",
                            List.of(
                                    "observability.query_xray_metric",
                                    "observability.query_grafana_metric"
                            ),
                            true,
                            Set.of("load_metric_abnormal", "load_metric_normal")
                    ),
                    new SkillStageDefinition(
                            "load_job",
                            List.of("alluxio.query_job"),
                            false,
                            Set.of("job_missing", "job_stalled", "job_progressing")
                    ),
                    new SkillStageDefinition(
                            "load_worker",
                            List.of("alluxio.query_worker"),
                            false,
                            Set.of("worker_missing", "worker_abnormal")
                    )
            ),
            Set.of("load_metrics", "job_status", "job_progress", "worker_status"),
            Set.of("job_missing", "job_stalled", "job_progressing"),
            Set.of(SkillId.PREWARM_TRIAGE, SkillId.WORKER_READ)
    );

    public LoadExecutionSkill() {
        super(DEFINITION);
    }
}

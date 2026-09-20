package com.prewarm.diagnosis.agent.skill.alluxio;

import com.prewarm.diagnosis.agent.skill.AbstractPrewarmSkill;
import com.prewarm.diagnosis.agent.skill.SkillDefinition;
import com.prewarm.diagnosis.agent.skill.SkillId;
import com.prewarm.diagnosis.agent.skill.SkillStageDefinition;

import java.util.List;
import java.util.Set;

/**
 * Worker 与 UFS 读取诊断 Skill。
 */
public final class WorkerReadSkill extends AbstractPrewarmSkill {

    private static final SkillDefinition DEFINITION = new SkillDefinition(
            SkillId.WORKER_READ,
            "Worker 与 UFS 读取诊断",
            Set.of("ufs_read_slow", "ufs_read_failed", "worker_not_participating"),
            Set.of("path", "cluster", "time_range"),
            Set.of(
                    "alluxio.query_worker",
                    "alluxio.query_ufs_read",
                    "observability.query_xray_metric",
                    "observability.query_grafana_metric"
            ),
            List.of(
                    new SkillStageDefinition(
                            "worker_and_metrics",
                            List.of(
                                    "alluxio.query_worker",
                                    "observability.query_xray_metric",
                                    "observability.query_grafana_metric"
                            ),
                            true,
                            Set.of("worker_missing", "read_metric_abnormal")
                    ),
                    new SkillStageDefinition(
                            "ufs_read",
                            List.of("alluxio.query_ufs_read"),
                            false,
                            Set.of("read_slow", "read_failed", "read_normal")
                    )
            ),
            Set.of("worker_status", "read_latency", "read_throughput", "read_failure"),
            Set.of("read_slow", "read_failed", "read_normal"),
            Set.of(SkillId.PREWARM_TRIAGE, SkillId.LOAD_EXECUTION)
    );

    public WorkerReadSkill() {
        super(DEFINITION);
    }
}

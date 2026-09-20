package com.prewarm.diagnosis.agent.skill.alluxio;

import com.prewarm.diagnosis.agent.skill.AbstractPrewarmSkill;
import com.prewarm.diagnosis.agent.skill.SkillDefinition;
import com.prewarm.diagnosis.agent.skill.SkillId;
import com.prewarm.diagnosis.agent.skill.SkillStageDefinition;

import java.util.List;
import java.util.Set;

/**
 * Block / Location 元信息一致性诊断 Skill。
 */
public final class MetadataConsistencySkill extends AbstractPrewarmSkill {

    private static final SkillDefinition DEFINITION = new SkillDefinition(
            SkillId.METADATA_CONSISTENCY,
            "Block / Location 元信息一致性诊断",
            Set.of("block_missing", "location_not_updated", "metadata_inconsistent"),
            Set.of("path", "cluster", "time_range"),
            Set.of(
                    "cache_manager.query_task",
                    "cache_manager.query_log",
                    "alluxio.query_job",
                    "alluxio.query_block_location"
            ),
            List.of(
                    new SkillStageDefinition(
                            "task_and_load_state",
                            List.of("cache_manager.query_task", "alluxio.query_job"),
                            true,
                            Set.of("task_incomplete", "load_incomplete", "load_complete")
                    ),
                    new SkillStageDefinition(
                            "block_location",
                            List.of(
                                    "alluxio.query_block_location",
                                    "cache_manager.query_log"
                            ),
                            true,
                            Set.of("block_missing", "location_missing", "metadata_consistent")
                    )
            ),
            Set.of("task_status", "load_status", "block_status", "location_status", "log_evidence"),
            Set.of("block_missing", "location_missing", "metadata_consistent"),
            Set.of(SkillId.PREWARM_TRIAGE, SkillId.LOAD_EXECUTION)
    );

    public MetadataConsistencySkill() {
        super(DEFINITION);
    }
}

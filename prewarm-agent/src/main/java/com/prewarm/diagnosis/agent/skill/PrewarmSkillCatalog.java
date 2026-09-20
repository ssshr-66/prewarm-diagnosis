package com.prewarm.diagnosis.agent.skill;

import com.prewarm.diagnosis.agent.skill.alluxio.LoadExecutionSkill;
import com.prewarm.diagnosis.agent.skill.alluxio.MetadataConsistencySkill;
import com.prewarm.diagnosis.agent.skill.alluxio.WorkerReadSkill;
import com.prewarm.diagnosis.agent.skill.cachemanager.TaskLifecycleSkill;
import com.prewarm.diagnosis.agent.skill.mq.MqDeliverySkill;
import com.prewarm.diagnosis.agent.skill.result.ResultDeliverySkill;
import com.prewarm.diagnosis.agent.skill.triage.PrewarmTriageSkill;

import java.util.List;

/**
 * 预热诊断 Skill 的默认目录。
 */
public final class PrewarmSkillCatalog {

    private PrewarmSkillCatalog() {
    }

    public static SkillRegistry createDefault() {
        return new SkillRegistry(List.of(
                new PrewarmTriageSkill(),
                new MqDeliverySkill(),
                new TaskLifecycleSkill(),
                new LoadExecutionSkill(),
                new WorkerReadSkill(),
                new MetadataConsistencySkill(),
                new ResultDeliverySkill()
        ));
    }
}

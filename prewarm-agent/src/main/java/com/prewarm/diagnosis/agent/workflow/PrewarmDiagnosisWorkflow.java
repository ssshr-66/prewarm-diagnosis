package com.prewarm.diagnosis.agent.workflow;

 /**
 * 预热诊断 Workflow。
 *
 * <p>后续由 Runtime 执行 Skill 选中的阶段，编排指标、MQ、Apollo、Cache Manager、
 * 日志和 Alluxio 查询；Skill 负责声明受控工具范围和诊断剧本，
 * Workflow 负责执行阶段、并行关系、分支和恢复，不让模型自由决定高风险工具调用顺序。</p>
 */
public final class PrewarmDiagnosisWorkflow {
    // TODO: 接入 Skill 定义，执行阶段依赖、并行查询、停止条件和断点恢复。
}

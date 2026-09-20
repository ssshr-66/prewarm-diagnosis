package com.prewarm.diagnosis.mcp.tool.observability;

/**
 * 指标与 Alluxio 诊断入口工具。
 *
 * <p>后续先查询 XRay/Grafana 的 PromSQL 指标；只有指标已经暴露异常时，
 * 才进一步调用受限的只读 Alluxio API。</p>
 */
public final class ObservabilityInspectionTool {
    // TODO: 编排 PromSQL 查询和条件式 Alluxio 只读查询。
}

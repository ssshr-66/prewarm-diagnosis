package com.prewarm.diagnosis.mcp.connector.observability;

/**
 * 指标异常后的 Alluxio 只读客户端。
 *
 * <p>Alluxio API 是 XRay/Grafana 指标诊断入口的条件式数据源，
 * 只封装 Job、Worker、Block 等诊断所需查询。</p>
 */
public final class AlluxioClient {
    // TODO: 接入受限的 Alluxio 只读 API。
}

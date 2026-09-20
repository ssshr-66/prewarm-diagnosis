package com.prewarm.diagnosis.agent.mcp;

/**
 * Agent 到 MCP Server 的调用边界。
 *
 * <p>后续负责工具发现、强约束参数组装、超时和错误转换；
 * 诊断 Workflow 不直接依赖 Apollo、MQ 或 Alluxio 客户端。</p>
 */
public final class PrewarmMcpClient {
    // TODO: 接入 MCP 客户端协议和工具调用审计。
}

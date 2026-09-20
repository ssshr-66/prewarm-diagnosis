package com.prewarm.diagnosis.mcp.connector.cachemanager;

/**
 * Cache Manager 数据库只读客户端。
 *
 * <p>后续按白名单 SQL 或 Repository 查询 event 消息表、UFS 表和任务状态，
 * 禁止模型直接提交任意 SQL。</p>
 */
public final class CacheManagerDatabaseClient {
    // TODO: 接入 Cache Manager MySQL 只读账号和固定查询。
}

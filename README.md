# prewarm-diagnosis

预热链路智能诊断 Agent。当前仓库先承载整体架构和模块边界，后续再逐步接入真实的飞书、MySQL、Apollo、MQ、XRay/Grafana、日志平台和 Alluxio 能力。

## 1. 当前产品定位

这是一个面向预热运维团队的内部诊断 Copilot，而不是直接面向所有业务方的自动运维平台。

- 飞书机器人是交互入口，不单独开发 Web 前端。
- 无论问题来自业务反馈还是线上告警，第一响应人都是组内同学；组内同学把现象转述给机器人。
- Agent 自动串联预热链路，采集证据、判断可能根因并生成报告。
- 信息不足时，Agent 向组内同学追问集群、消息、时间范围等关键信息；信息足够后并发排查整条链路。
- 组内同学对 Agent 返回的带证据结论做选择性确认，涉及生产变更时仍由人确认和执行。

告警自动接入暂缓。当前版本先接收组内同学转述的告警现象，后续确认告警平台协议后，再把告警 Webhook 接入同一套诊断流程。

## 2. 需要诊断的预热链路

典型链路如下：

```text
上游数据列表（Spark / 爬虫 / 业务）
        ↓
RocketMQ 预热消息
        ↓
Cache Manager 消费、校验和任务落库
        ↓
队列调度、并发度和资源配额控制
        ↓
Alluxio Load
        ↓
Job Worker 读取 UFS，并将数据写入 Storage Worker 的 Block
        ↓
Block / Location 元信息更新、结果消息发送
        ↓
Spark 或业务读取 Alluxio 缓存
```

用户说“某条消息预热很慢”时，Agent 需要沿着这条链路判断问题停在哪一段，例如：

- 消息有没有进入正确的 Topic；
- Consumer 是否消费、是否积压或重试；
- Cache Manager 是否收到消息并创建任务；
- 任务是否卡在队列、并发度或限流；
- Alluxio Load 是否启动、是否推进；
- Worker 是否在读取 UFS、写入 Block；
- 结果消息是否产生并被消费。

## 3. 总体数据流

```text
业务反馈 / 线上告警（由组内同学转述）
        ↓
Agent 接入层
        ↓
会话上下文提取与补全
        ├── 信息不足：生成追问并通过飞书回复
        └── 信息完整：创建一次诊断 Run
                         ↓
                   Skill 路由
                    ├── 明确场景：专项 Skill
                    └── 模糊场景：预热问题初筛
                         ↓
                   Skill 固定 Workflow
                         ↓
                    MCP Client
                         ↓
                    MCP Server
       ┌─────────────────┼──────────────────────┐
       ↓                 ↓                      ↓
    MQ 工具       Cache Manager 工具       XRay/Grafana 工具
       ↓                 ↓                      ↓
  RMQ 平台       Apollo / Cache Manager DB   PromQL 指标
                 / Cache Manager 日志             ↓
                                      必要时调用只读 Alluxio API
                         ↓
                   证据归并与规则判断
                         ↓
                    诊断报告生成
                         ↓
                 MySQL Outbox 可靠投递
                         ↓
                    飞书回复结果
```

当前版本的核心使用方式是“人先发现，Agent 负责跨系统排查，人确认结果”。业务反馈和线上告警只是问题来源，进入 Agent 后统一转换为会话和诊断 Run；告警平台直接调用 Agent 的能力暂时搁置。

其中，Alluxio 诊断的入口优先是 XRay 和 Grafana 指标：先通过服务选择和 PromQL 查询判断是否存在异常，再在确实需要时调用受限的 Alluxio 只读 API 查询任务、Worker、Block 等细节，避免一开始就无边界地查询 Alluxio。

## 4. 模块划分

仓库采用 Maven 多模块结构。

### `prewarm-contract`

跨模块共享的数据契约，只描述 Agent 和 MCP Server 之间传递的结构，不放外部系统连接逻辑。

- `conversation`：会话上下文、已提取字段和缺失字段；
- `diagnosis`：一次诊断任务及其状态；
- `evidence`：结构化证据、来源和可追溯链接；
- `mcp`：MCP 请求和工具调用结果；
- `notification`：飞书消息相关的内部消息结构。

### `prewarm-agent`

面向用户和诊断流程的 Agent Runtime。

- `feishu`：接收飞书事件、校验消息并转换为内部请求；
- `alert`：未来接入告警平台的入口，当前只保留占位，暂不实现自动接入；
- `conversation`：从自然语言提取上下文、合并多轮信息、判断是否需要追问；
- `llm`：模型调用抽象，只负责理解、抽取和表达，不直接决定工具调用边界；
- `mcp`：Agent 到 MCP Server 的调用边界，负责参数约束、超时和错误转换；
- `skill`：Skill 定义、注册表、路由边界、Tool 白名单和面向故障场景的诊断剧本；
- `workflow`：执行 Skill 选中的阶段，负责顺序、并行、分支、停止和恢复；
- `diagnosis`：基于证据的规则判断、根因分类和置信度；
- `evidence`：并行采集、时间线对齐和证据归并；
- `report`：把结构化结论组织成面向人的诊断报告；
- `persistence`：MySQL 会话、Run、证据和 Outbox 持久化；
- `notification`：飞书结果发送、失败重试和投递状态管理；
- `config`：Agent 自身配置和依赖装配。

### `prewarm-mcp`

对外部系统能力做安全、结构化、可审计封装的 MCP Server。

- `server`：MCP Server 启动和工具注册；
- `tool.mq`：以 RMQ 管理平台为数据源，确认 Topic、Topic 下 Consumer 是否建立，并查询消息消费情况和实际消费它的 Consumer；
- `tool.cachemanager`：组合查询 Apollo 上的 `cluster-name / consumer / topic` 配置、Cache Manager DB 的 event/UFS 表状态和 Cache Manager 日志；
- `tool.observability`：查询 XRay/Grafana 的 PromQL 指标，只有指标暴露异常时才进一步调用受限的只读 Alluxio API；
- `connector`：各外部系统的客户端实现，工具层不直接处理连接细节；
- `policy`：只读权限、集群白名单、时间范围、返回大小和审计限制；
- `config`：MCP Server 和外部依赖配置。

工具层和连接器层仍然分开：Tool 定义“一个诊断入口允许 Agent 做什么”，Connector 定义“如何访问该入口依赖的线上系统”。
这样既保持职责隔离，也避免把 Apollo、日志、任意 SQL、Shell、URL 或无限范围查询直接暴露给模型。

## 5. MySQL 持久化方案

当前确定使用 MySQL，不引入向量数据库。第一版也不把 Redis 作为事实来源；如果以后需要分布式锁或热点缓存，再单独引入。

### 会话和诊断任务不是一回事

- `diagnosis_session`：保存多轮对话上下文，解决“用户第一次没说全，下一条消息继续补充”的问题；
- `diagnosis_run`：保存一次诊断执行过程，解决服务重启、失败重试和阶段恢复的问题。

### 计划中的表

| 表 | 主要职责 |
| --- | --- |
| `diagnosis_session` | 会话 ID、飞书会话信息、当前状态、`context_json`、缺失字段、过期时间 |
| `diagnosis_message` | 收发消息审计；以外部 `message_id` 唯一约束实现幂等去重 |
| `diagnosis_run` | 一次诊断的 Workflow、上下文快照、当前阶段、状态、结论和置信度 |
| `diagnosis_evidence` | 每个阶段的查询条件、数据摘要、来源系统、时间和链接 |
| `outbox_message` | 待发送、已发送、失败待重试的飞书消息 |

动态性较强的上下文可以使用 MySQL JSON 字段保存，例如消息 ID、集群、路径和时间范围；会话 ID、状态、外部消息 ID、创建时间等需要使用普通字段和索引。

不保存无限聊天记录、完整原始日志或模型隐式思考过程。证据保存结构化摘要和原始系统链接，既能追溯，又不会让数据库变成日志仓库。

## 6. 会话状态

```text
COLLECTING_CONTEXT
        ↓ 信息补全
READY
        ↓ 创建诊断任务
DIAGNOSING
        ├── 需要人工确认 → WAITING_USER
        ├── 失败可重试   → DIAGNOSING
        └── 完成         → COMPLETED
        ↓ 超时
EXPIRED
```

会话应按 `conversation_id + thread_id/root_message_id` 归属，不能只按群聊 ID 保存，避免同一个群里多个问题互相串上下文。

## 7. 可靠性原则

- 飞书回调先快速确认，再异步执行诊断；
- 入站消息按外部消息 ID 幂等；
- 每个诊断阶段保存状态，服务重启后可以恢复；
- 每次 Run 保存上下文快照，避免诊断执行过程中上下文被后续消息覆盖；
- 最终回复通过 Outbox 投递，失败可重试；
- 外部查询默认只读，并限制集群、时间范围、结果数量和权限；
- 规则负责证据到结论的判断，LLM 负责自然语言理解和结果表达，不能让模型自由越过权限边界。

## 8. 当前不做的事情

- 不开发独立 Web 前端，飞书承担交互界面；
- 暂不实现告警平台自动触发，先由组内同学转述告警；
- 不自动修改 Apollo、MQ、Cache Manager 或 Alluxio 线上配置；
- 不开放任意 SQL、Shell 或无限制外部 API；
- 不引入向量数据库做知识库；
- 不把第一版包装成面向所有业务的全自动运维平台。

## 9. 后续实现顺序

1. 固化 contract 和 MySQL 表结构；
2. 固化 Skill 定义、Tool 白名单和 Workflow 骨架；
3. 实现飞书接入、消息幂等和会话补全；
4. 实现 MySQL Repository、Run 状态和 Outbox；
5. 实现 MCP Server 及 Apollo、MQ、Cache Manager、XRay/Grafana、日志、Alluxio 工具；
6. 实现预热诊断 Workflow、证据归并、规则和报告；
7. 使用 Mock 外部依赖完成端到端测试，再逐个替换为线上连接器。

## 10. 当前代码状态

当前提交只创建模块、包和职责示例文件。Skill 包目前只包含定义、注册和路由边界，
示例类没有真实诊断、MCP 调用或路由实现，作用是固定后续代码应该放在哪个边界内。

包名 `com.prewarm.diagnosis` 是当前骨架使用的临时命名空间，接入真实服务前可以按团队 Java 包名规范统一替换。

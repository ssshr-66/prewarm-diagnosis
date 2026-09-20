# MySQL migration placeholder

后续在这里添加 Flyway 或 Liquibase migration。

第一版预计初始化以下表：

- `diagnosis_session`
- `diagnosis_message`
- `diagnosis_run`
- `diagnosis_evidence`
- `outbox_message`

当前只记录表职责，不提交真实表结构；等字段和状态机确认后再落 SQL。

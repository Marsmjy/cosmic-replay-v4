# 变更影响面 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证
> **confidence**: verified

---

## 一、变更操作影响速查

| 操作 | 影响范围 | 影响级别 | 说明 |
|---|---|---|---|
| 新建 positiontpl | 创建岗位模板记录 | 中 | 须所在 BU 已开启 openpositiontpl |
| 修改 name/posttpltype/fieldrange 等 | 级联同步下游岗位 + BEC 消息 | ⭐ 高 | PositionTplChangeSyncPosService + sendMsg |
| 修改 enable=0（禁用）| 本记录 | 中 | 禁用后不可设适用范围 |
| 修改 enable=1（启用）| 本记录 | 中 | 启用后可设适用范围 |
| 设置适用范围 | hbpm_applicationscope 关联表 | 低 | 影响哪些 BU 可用此模板 |
| 删除 positiontpl | 岗位模板记录 + 关联适用范围 | ⭐ 高 | 下游引用此模板的岗位可能受影响 |

---

## 二、下游受影响实体

| 下游实体 | 关联字段 | 影响操作 | 影响方式 |
|---|---|---|---|
| `hbpm_positionhr`（岗位信息维护）| positiontpl 关联 | save/modify | PositionTplChangeSyncPosService 级联同步 |
| `bos_position`（平台岗位）| — | save/modify | IBosPositionService.commonSyncPositions() |
| `hbpm_applicationscope`（适用范围）| positiontpl 关联 | 设置适用范围操作 | 直接写入 |
| 下游系统（BEC 订阅方）| — | save/modify | ChangeMsgServiceImpl.sendMsg() 通知 |

---

## 三、ISV 开发影响面

| ISV 变更 | 风险 | 建议 |
|---|---|---|
| modifyMeta 加字段 | 低 | 按 PR-001 流程 · 前缀+key≤24字符 |
| 并列挂 OP 插件（save 后）| 低 | rowkey 排在 PositionTplSaveOp 之后 |
| 并列挂 FormPlugin | 低 | 不干扰标品逻辑 |
| 继承场景专属类 | ❌ 高 | PR-001 禁止 |
| 绕过 OP 直接改 enable | ❌ 高 | 绕过 R-2 校验 → 数据不一致 |
| 在 endOperationTransaction 做耗时操作 | ❌ 高 | 同事务 · 耗时导致整体回滚 |

---

## 四、BU 开关对功能的影响

| BU 参数 | 对应功能 |
|---|---|
| `openpositiontpl=false` | 列表新建按钮不可用 · 编辑页 modify 不可用 · modifystrategy 隐藏 |
| `openpositiontpl=true` | 全功能可用 |
| `positiontplismodify=true` | fieldrange 字段可见 |
| `positiontplchangepos=true` | modify 时展示公告提示 |

# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Added

- Comprehensive analysis by 9 professional roles
- SQLite database integration (schema designed)
- Security hardening (API authentication planned)
- OpenAPI specification (in progress)
- Performance optimization framework
- Monitoring and alerting system design

### Fixed (2026-04-30)

#### pageId 链路修复（`lib/replay.py`）

- **`_pending_by_app` 初始化**：`__init__` 加 `self._pending_by_app = {}`
- **`_harvest_virtual_tab_pageids` 调用**：在 `invoke()` 响应处理后调用，用于捕获 `addVirtualTab` 下发的 pageId
- **pageId 查找后备**：当 `page_ids[form_id]` 为空时，检查 `_pending_by_app[app_id]` 作为后备
- **`ac="new"` 追踪**：`("addnew", "modify", "copyBill", "edit")` 列表加 `"new"`，确保 `ac=new` 响应中的新 pageId 被保存
- **L2 pageId 优先级修复**：`_pending_by_app` 的 pageId（来自 `addVirtualTab`，32hex 格式）优先于 L2 pageId（`{menuId}root{baseId}` 格式），但不覆盖 32hex 表单级 pageId

#### HAR 导入修复（`lib/har_extractor.py`）

- **`newentry` 步骤 post_data 变量化**：新增 `ac="newentry"` 分支处理，将新增条目行的 name/number 等字段值抽为变量，避免出现 "ppppp1" 等硬编码值
- **`_CLASSIFY_KEY_EXCLUSIONS` 排除列表**：新增排除集，`ename`（属性名称）不再被后缀匹配误分类为 name 字段，保持 HAR 原始内容不变
- **`_SAVE_BUTTON_KEYS` 修正**：`btnsave`/`btnsaveandnew`/`btnsaveaddnew`/`btnsavenew` 标记为 `tier: core`（不被标 optional）
- **不移改 ac**：`ac=click` 的 btnsave 保留 `ac=click`，不改为 `saveandeffect`（某些表单的保存就是 `ac=click`）

#### 环境修复

- **`_find_login_script()` 搜索路径**：优先搜索 `lib/cosmic_login.py`（与 `replay.py` 同目录）
- **Web UI 自动加载 `.env`**：`lib/webui/server.py` 的 `main()` 启动时调用 `_load_dotenv()` 读取项目根 `.env` 文件
- **清除所有相关的 `__pycache__` 和 `.pyc`**

---

## [2.0.0] - 2026-04-28

### Added

- **v2 Project Creation**
  - New project structure with port 8766
  - Preserved all core capabilities from v1
  - Professional multi-perspective optimization

- **UI Enhancements**
  - Task history tab with batch execution records
  - Report dialog with summary statistics
  - Clickable case names linking to case details
  - Environment switch confirmation dialog
  - Failed step auto-expand and highlight

- **Backend Improvements**
  - Task management system (lib/task_manager.py)
  - Execution report API (/api/tasks/{id}/report)
  - Health check endpoint (/api/health)
  - Execution history with business-friendly display

- **Documentation**
  - Comprehensive analysis report (10,320+ lines)
  - Data architecture design (10 tables + views)
  - DevOps solution design (K8s + Prometheus)
  - Performance analysis report (15 bottlenecks)
  - Load test design with scripts

### Changed

- Report dialog moved to global scope (fixes visibility issue)
- Task buttons show different actions based on status
- Step descriptions now show business meaning

### Fixed

- Report dialog not showing on logs page
- Case names not clickable in report details
- Pending tasks showing "view report" button incorrectly

---

## [1.0.0] - 2026-04-27

### Added

- **Core Features**
  - HAR to YAML intelligent conversion
  - Web UI for case management
  - Real-time execution monitoring via SSE
  - Batch execution support
  - Failure diagnosis with advisor
  - Multi-environment configuration

- **HAR Extraction**
  - Automatic noise step filtering
  - Field merging and variable extraction
  - Form ID to business label mapping
  - Step description generation

- **Execution Engine**
  - HTTP protocol replay
  - Variable interpolation (timestamp, random, etc.)
  - Assertion validation
  - Error recovery and retry

- **Web UI**
  - Dashboard with case statistics
  - Case detail editor with YAML source
  - Batch runner with progress tracking
  - Log viewer with filtering
  - Environment switcher

- **Infrastructure**
  - FastAPI backend server
  - Alpine.js + Tailwind CSS frontend
  - Docker deployment support
  - JSONL log persistence

---

## Version History

| Version | Date | Highlights |
|---------|------|------------|
| 2.0.0 | 2026-04-28 | Multi-perspective optimization, task system |
| 1.0.0 | 2026-04-27 | Initial release, core features complete |

---

## Roadmap

### v2.1.0 (Planned)

- [ ] SQLite database integration
- [ ] API authentication (JWT/API Key)
- [ ] Prometheus metrics export
- [ ] OpenAPI documentation
- [ ] Unit test coverage > 80%

### v2.2.0 (Planned)

- [ ] Kubernetes deployment
- [ ] Grafana dashboards
- [ ] Backup and restore automation
- [ ] Audit logging
- [ ] Performance caching

### v3.0.0 (Future)

- [ ] Plugin architecture
- [ ] Custom assertion library
- [ ] Test scheduling
- [ ] Email/Slack notifications
- [ ] Team collaboration features

---

[Unreleased]: https://github.com/Marsmjy/cosmic-replay-v2/compare/v2.0.0...HEAD
[2.0.0]: https://github.com/Marsmjy/cosmic-replay-v2/compare/v1.0.0...v2.0.0
[1.0.0]: https://github.com/Marsmjy/cosmic-replay-v2/releases/tag/v1.0.0

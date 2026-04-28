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

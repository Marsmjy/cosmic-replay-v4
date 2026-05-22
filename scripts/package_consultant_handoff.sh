#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DATE_TAG="${1:-$(date +%Y%m%d)}"
OUT_DIR="$ROOT/dist/cosmic-replay-consultant-handoff-$DATE_TAG"
SKILL_ZIP="$ROOT/dist/cosmic-replay-skills-$DATE_TAG.zip"
HANDOFF_ZIP="$ROOT/dist/cosmic-replay-consultant-handoff-$DATE_TAG.zip"

rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR/skills" "$OUT_DIR/demo" "$ROOT/dist"

cp -R "$ROOT/skills/cosmic-replay-overview" "$OUT_DIR/skills/"
cp -R "$ROOT/skills/cosmic-replay-troubleshooter" "$OUT_DIR/skills/"
cp -R "$ROOT/skills/cosmic-hr-expert" "$OUT_DIR/skills/"
cp "$ROOT/handoff/consultant-handoff-guide.html" "$OUT_DIR/"
cp -R "$ROOT/handoff/demo/." "$OUT_DIR/demo/"
cp "$ROOT/README.md" "$OUT_DIR/README-project.md"
cp "$ROOT/.env.example" "$OUT_DIR/.env.example"
cp -R "$ROOT/tests/fixtures/har_regression" "$OUT_DIR/har-regression-baseline"

cat > "$OUT_DIR/README-FIRST.txt" <<'TXT'
Cosmic Replay 顾问交付包

推荐阅读顺序：
1. consultant-handoff-guide.html
2. demo/qoder-work-initial-prompt.md
3. skills/cosmic-replay-troubleshooter/references/external-consultant-handoff.md
4. README-project.md

敏感信息提醒：
- 本包不应包含真实 HAR、cookie、token、数据库、账号密码。
- 执行用例前，请在目标工作区自行配置环境信息。
TXT

(
  cd "$OUT_DIR"
  zip -qr "$SKILL_ZIP" skills
  zip -qr "$HANDOFF_ZIP" .
)

echo "Skill zip: $SKILL_ZIP"
echo "Handoff zip: $HANDOFF_ZIP"
echo "Guide: $OUT_DIR/consultant-handoff-guide.html"

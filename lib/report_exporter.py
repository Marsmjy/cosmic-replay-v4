"""执行报告 HTML 导出模块

将报告数据渲染为独立 HTML 文件，可离线查看。
使用本地静态资源内嵌 Tailwind runtime 与 Chart.js，避免内网/离线环境图表不可用。
"""
import json
from string import Template
from datetime import datetime
from pathlib import Path


_STATIC_DIR = Path(__file__).parent / "webui" / "static"


def _read_static_asset(name: str) -> str:
    """Read a bundled browser asset for standalone report export."""
    content = (_STATIC_DIR / name).read_text(encoding="utf-8")
    return content.replace("</script", "<\\/script")


_HTML_TEMPLATE = Template(r"""<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>执行报告 - ${task_id}</title>
  <script>${tailwind_js}</script>
  <script>${chart_js}</script>
  <style>
    body { font-family: 'Inter', system-ui, -apple-system, sans-serif; }
    .card { background: rgba(30, 41, 59, 0.7); border: 1px solid rgba(71, 85, 105, 0.5); }
  </style>
</head>
<body class="bg-gray-900 text-gray-100 p-8 min-h-screen">
  <div class="max-w-5xl mx-auto">
    <!-- 标题 -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-white mb-2">${task_name}</h1>
      <div class="text-sm text-gray-400">
        <span>任务ID: ${task_id}</span>
        <span class="mx-2">|</span>
        <span>环境: ${env}</span>
        <span class="mx-2">|</span>
        <span>生成时间: ${generated_at}</span>
      </div>
    </div>

    <!-- 关键指标卡片 -->
    <div class="card rounded-lg p-6 mb-8" id="acceptance-section">
      <div class="flex flex-col md:flex-row md:items-start gap-4">
        <div id="acceptance-icon" class="w-10 h-10 rounded-xl flex items-center justify-center font-bold shrink-0"></div>
        <div class="flex-1">
          <div class="flex flex-wrap items-center gap-2 mb-1">
            <h2 class="text-lg font-semibold text-white" id="acceptance-title"></h2>
            <span class="text-xs rounded-full px-2 py-0.5 bg-slate-800 text-slate-300" id="acceptance-badge"></span>
          </div>
          <p class="text-sm text-gray-400" id="acceptance-summary"></p>
          <div class="grid grid-cols-2 md:grid-cols-6 gap-3 mt-4">
            <div class="bg-gray-950/40 rounded-lg p-3 border border-gray-800"><div class="text-xs text-gray-500">失败</div><div class="text-lg font-bold text-rose-300" id="acc-failed"></div></div>
            <div class="bg-gray-950/40 rounded-lg p-3 border border-gray-800"><div class="text-xs text-gray-500">入库已验证</div><div class="text-lg font-bold text-emerald-300" id="acc-write-ok"></div></div>
            <div class="bg-gray-950/40 rounded-lg p-3 border border-gray-800"><div class="text-xs text-gray-500">入库未验证</div><div class="text-lg font-bold text-amber-300" id="acc-write-risk"></div></div>
            <div class="bg-gray-950/40 rounded-lg p-3 border border-gray-800"><div class="text-xs text-gray-500">可自动修复</div><div class="text-lg font-bold text-sky-300" id="acc-auto"></div></div>
            <div class="bg-gray-950/40 rounded-lg p-3 border border-gray-800"><div class="text-xs text-gray-500">需确认</div><div class="text-lg font-bold text-amber-300" id="acc-manual"></div></div>
            <div class="bg-gray-950/40 rounded-lg p-3 border border-gray-800"><div class="text-xs text-gray-500">AI 诊断</div><div class="text-lg font-bold text-rose-300" id="acc-ai"></div></div>
          </div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
      <div class="card rounded-lg p-4 text-center">
        <div class="text-xs text-gray-400 mb-1">总用例</div>
        <div class="text-2xl font-bold text-white" id="stat-total"></div>
      </div>
      <div class="card rounded-lg p-4 text-center">
        <div class="text-xs text-gray-400 mb-1">通过率</div>
        <div class="text-2xl font-bold" id="stat-pass-rate"></div>
      </div>
      <div class="card rounded-lg p-4 text-center">
        <div class="text-xs text-gray-400 mb-1">总步骤</div>
        <div class="text-2xl font-bold text-sky-400" id="stat-steps"></div>
      </div>
      <div class="card rounded-lg p-4 text-center">
        <div class="text-xs text-gray-400 mb-1">总耗时</div>
        <div class="text-2xl font-bold text-purple-400" id="stat-duration"></div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
      <!-- 通过/失败饼图 -->
      <div class="card rounded-lg p-6">
        <h3 class="text-sm font-medium text-gray-300 mb-4">通过/失败分布</h3>
        <div style="height: 220px; display: flex; align-items: center; justify-content: center;">
          <canvas id="passFailChart"></canvas>
        </div>
      </div>
      <!-- 错误分类 -->
      <div class="card rounded-lg p-6">
        <h3 class="text-sm font-medium text-gray-300 mb-4">错误分类分布</h3>
        <div style="height: 220px; display: flex; align-items: center; justify-content: center;">
          <canvas id="errorChart"></canvas>
        </div>
      </div>
    </div>

    <!-- 性能概览 -->
    <div class="card rounded-lg p-6 mb-8" id="perf-section">
      <h3 class="text-sm font-medium text-gray-300 mb-4">性能概览</h3>
      <div class="text-sm text-gray-400 mb-4">
        平均步骤耗时: <span class="text-white font-medium" id="avg-step-duration"></span>
      </div>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <div class="text-xs text-red-400 mb-2 font-medium">最慢 Top 3</div>
          <div id="slowest-cases" class="space-y-1"></div>
        </div>
        <div>
          <div class="text-xs text-green-400 mb-2 font-medium">最快 Top 3</div>
          <div id="fastest-cases" class="space-y-1"></div>
        </div>
      </div>
    </div>

    <!-- 用例详情表格 -->
    <div class="card rounded-lg overflow-hidden mb-8">
      <div class="px-6 py-4 border-b border-slate-700">
        <h3 class="text-sm font-medium text-gray-300">用例执行详情</h3>
      </div>
      <div class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead class="bg-slate-800/50 text-xs text-gray-400">
            <tr>
              <th class="text-left px-6 py-3">用例名称</th>
              <th class="text-center px-4 py-3">状态</th>
              <th class="text-center px-4 py-3">入库证据</th>
              <th class="text-left px-4 py-3">下一步</th>
              <th class="text-right px-4 py-3">步骤</th>
              <th class="text-right px-4 py-3">耗时</th>
              <th class="text-left px-4 py-3">错误信息</th>
            </tr>
          </thead>
          <tbody id="case-table-body"></tbody>
        </table>
      </div>
    </div>

    <!-- 错误汇总 -->
    <div class="card rounded-lg p-6 mb-8" id="error-summary-section">
      <h3 class="text-sm font-medium text-rose-400 mb-4">错误汇总</h3>
      <div id="error-summary" class="space-y-2"></div>
    </div>

    <!-- 页脚 -->
    <div class="text-center text-xs text-gray-500 mt-12 pb-8">
      Generated by Cosmic Replay | ${export_time}
    </div>
  </div>

  <script>
    const REPORT_DATA = ${report_json};

    document.addEventListener('DOMContentLoaded', function() {
      const summary = REPORT_DATA.summary || {};
      const caseResults = REPORT_DATA.case_results || [];
      const errors = REPORT_DATA.errors || [];
      const errorBreakdown = REPORT_DATA.error_breakdown || {};
      const performance = REPORT_DATA.performance || {};
      const acceptance = REPORT_DATA.acceptance || {};

      // 验收结论
      const accStatus = acceptance.status || 'unknown';
      const accIcon = document.getElementById('acceptance-icon');
      const accStyle = {
        ready: ['✓', 'bg-emerald-600 text-white', '可交付'],
        needs_ai: ['!', 'bg-rose-600 text-white', '需 AI 诊断'],
        needs_repair: ['~', 'bg-amber-500 text-gray-950', '需修复'],
        unknown: ['?', 'bg-sky-600 text-white', '待验收'],
      }[accStatus] || ['?', 'bg-sky-600 text-white', '待验收'];
      accIcon.textContent = accStyle[0];
      accIcon.className += ' ' + accStyle[1];
      document.getElementById('acceptance-title').textContent = acceptance.title || '批量验收结论';
      document.getElementById('acceptance-badge').textContent = accStyle[2];
      document.getElementById('acceptance-summary').textContent = acceptance.summary_text || '暂无验收摘要';
      document.getElementById('acc-failed').textContent = acceptance.failed || 0;
      document.getElementById('acc-write-ok').textContent = acceptance.write_verified || 0;
      document.getElementById('acc-write-risk').textContent = acceptance.write_unverified || 0;
      document.getElementById('acc-auto').textContent = acceptance.auto_repairable || 0;
      document.getElementById('acc-manual').textContent = acceptance.manual_confirm || 0;
      document.getElementById('acc-ai').textContent = acceptance.ai_required || 0;

      // 填充指标卡片
      document.getElementById('stat-total').textContent = summary.total_cases || 0;
      const passRate = ((summary.pass_rate || 0) * 100).toFixed(1);
      const passRateEl = document.getElementById('stat-pass-rate');
      passRateEl.textContent = passRate + '%';
      passRateEl.className = 'text-2xl font-bold ' + (parseFloat(passRate) >= 100 ? 'text-green-400' : 'text-amber-400');
      document.getElementById('stat-steps').textContent = summary.total_steps || 0;
      document.getElementById('stat-duration').textContent = (summary.total_duration_s || 0).toFixed(1) + 's';

      // 通过/失败饼图
      const passFailCtx = document.getElementById('passFailChart').getContext('2d');
      new Chart(passFailCtx, {
        type: 'doughnut',
        data: {
          labels: ['通过', '失败'],
          datasets: [{
            data: [summary.passed_cases || 0, summary.failed_cases || 0],
            backgroundColor: ['#34d399', '#f87171'],
            borderWidth: 0
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { position: 'bottom', labels: { color: '#9ca3af', padding: 16 } }
          }
        }
      });

      // 错误分类柱状图
      const hasErrors = Object.values(errorBreakdown).some(v => v > 0);
      if (hasErrors) {
        const errorCtx = document.getElementById('errorChart').getContext('2d');
        new Chart(errorCtx, {
          type: 'bar',
          data: {
            labels: ['业务断言', '技术错误', '环境问题', '框架异常'],
            datasets: [{
              data: [
                errorBreakdown.business || 0,
                errorBreakdown.technical || 0,
                errorBreakdown.environment || 0,
                errorBreakdown.framework || 0
              ],
              backgroundColor: ['#fbbf24', '#f87171', '#a78bfa', '#fb923c'],
              borderWidth: 0
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
              x: { ticks: { color: '#9ca3af' }, grid: { color: '#374151' } },
              y: { beginAtZero: true, ticks: { color: '#9ca3af', stepSize: 1 }, grid: { color: '#374151' } }
            }
          }
        });
      } else {
        document.getElementById('errorChart').parentElement.innerHTML =
          '<div class="text-gray-500 text-sm flex items-center justify-center h-full">无错误分类数据</div>';
      }

      // 性能概览
      if (performance.avg_step_duration_s != null) {
        document.getElementById('avg-step-duration').textContent =
          performance.avg_step_duration_s.toFixed(3) + 's';
      }

      function renderCaseList(containerId, cases, colorClass) {
        const container = document.getElementById(containerId);
        if (!cases || cases.length === 0) {
          container.innerHTML = '<div class="text-xs text-gray-500">暂无数据</div>';
          return;
        }
        container.innerHTML = cases.map(c =>
          '<div class="flex justify-between text-xs py-1 border-b border-gray-700/50">' +
          '<span class="text-gray-300 truncate mr-2">' + escapeHtml(c.name) + '</span>' +
          '<span class="' + colorClass + ' whitespace-nowrap">' + c.duration_s.toFixed(1) + 's</span>' +
          '</div>'
        ).join('');
      }

      renderCaseList('slowest-cases', performance.slowest_cases, 'text-red-300');
      renderCaseList('fastest-cases', performance.fastest_cases, 'text-green-300');

      // 如果没有性能数据则隐藏
      if (!performance.avg_step_duration_s && (!performance.slowest_cases || performance.slowest_cases.length === 0)) {
        document.getElementById('perf-section').style.display = 'none';
      }

      // 用例详情表格
      const tbody = document.getElementById('case-table-body');
      tbody.innerHTML = caseResults.map(r => {
        const statusClass = r.passed ? 'text-emerald-400' : 'text-rose-400';
        const statusText = r.passed ? 'PASS' : 'FAIL';
        const errorText = r.error ? escapeHtml(r.error) : '-';
        return '<tr class="border-t border-slate-700/50 hover:bg-slate-800/30">' +
          '<td class="px-6 py-3 text-gray-200">' + escapeHtml(r.name) + '</td>' +
          '<td class="text-center px-4 py-3"><span class="' + statusClass + ' font-medium">' + statusText + '</span></td>' +
          '<td class="text-center px-4 py-3"><span class="' + writeStatusClass(r.write_status) + '">' + writeStatusLabel(r.write_status) + '</span></td>' +
          '<td class="px-4 py-3 text-xs text-gray-300">' + nextActionLabel(r.next_action) + '</td>' +
          '<td class="text-right px-4 py-3 text-gray-400">' + (r.step_ok || 0) + '/' + (r.step_count || 0) + '</td>' +
          '<td class="text-right px-4 py-3 text-gray-400">' + (r.duration_s || 0).toFixed(1) + 's</td>' +
          '<td class="px-4 py-3 text-xs text-rose-400 max-w-xs truncate">' + errorText + '</td>' +
          '</tr>';
      }).join('');

      // 错误汇总
      const errorSummaryEl = document.getElementById('error-summary');
      if (errors.length === 0) {
        document.getElementById('error-summary-section').style.display = 'none';
      } else {
        errorSummaryEl.innerHTML = errors.map(e =>
          '<div class="bg-rose-900/20 rounded px-4 py-3">' +
          '<span class="font-medium text-rose-300">' + escapeHtml(e.case || e.name || '') + '</span>: ' +
          '<span class="text-gray-300 text-xs">' + escapeHtml(e.error || '') + '</span>' +
          '</div>'
        ).join('');
      }
    });

    function escapeHtml(str) {
      if (!str) return '';
      return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
    }

    function writeStatusLabel(status) {
        return {
          verified: '已验证',
          manual_verified: '人工确认',
          unverified: '未验证',
          failed: '失败',
          not_applicable: '不适用',
        not_checked: '未检查'
      }[status || 'not_checked'] || status;
    }

    function writeStatusClass(status) {
      return 'inline-block rounded-full px-2 py-0.5 text-xs ' + ({
        verified: 'bg-emerald-900/70 text-emerald-300',
        manual_verified: 'bg-sky-900/70 text-sky-300',
        unverified: 'bg-amber-900/70 text-amber-300',
        failed: 'bg-rose-900/70 text-rose-300',
        not_applicable: 'bg-slate-800 text-slate-400',
        not_checked: 'bg-slate-800 text-slate-400'
      }[status || 'not_checked'] || 'bg-slate-800 text-slate-400');
    }

    function nextActionLabel(action) {
      return {
        none: '无需处理',
        auto_repair: '可自动修复',
        manual_confirm: '需确认',
        ai_agent: 'AI 诊断'
      }[action || 'none'] || action;
    }
  </script>
</body>
</html>""")


def export_html(report_data: dict) -> str:
    """
    将报告数据渲染为独立 HTML 字符串。
    report_data 结构与 /api/tasks/{id}/report 返回一致。
    """
    # 安全序列化 JSON（防止 XSS: </script> 等注入）
    report_json = json.dumps(report_data, ensure_ascii=False, default=str)
    report_json = report_json.replace("</", "<\\/")

    # Template 变量
    task_id = report_data.get("task_id", "unknown")
    task_name = report_data.get("task_name", "执行报告")
    env = report_data.get("env", "unknown")
    generated_at = report_data.get("generated_at", "")
    export_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    html = _HTML_TEMPLATE.safe_substitute(
        task_id=task_id,
        task_name=task_name or "执行报告",
        env=env,
        generated_at=generated_at,
        export_time=export_time,
        report_json=report_json,
        tailwind_js=_read_static_asset("tailwind.js"),
        chart_js=_read_static_asset("chart.umd.min.js"),
    )
    return html

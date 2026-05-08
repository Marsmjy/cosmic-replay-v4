"""任务完成邮件通知模块"""
import smtplib
import logging
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from typing import Optional

logger = logging.getLogger(__name__)


class EmailNotifier:
    """邮件通知器"""
    
    def __init__(self, config: dict):
        """
        config 结构:
        {
            "enabled": True/False,
            "smtp_host": "smtp.example.com",
            "smtp_port": 465,
            "smtp_ssl": True,
            "username": "user@example.com",
            "password": "xxx",
            "from_addr": "noreply@example.com",
            "recipients": ["user1@example.com", "user2@example.com"],
            "subject_prefix": "[Cosmic Replay]"
        }
        """
        self.enabled = config.get("enabled", False)
        self.smtp_host = config.get("smtp_host", "")
        self.smtp_port = config.get("smtp_port", 465)
        self.smtp_ssl = config.get("smtp_ssl", True)
        self.username = config.get("username", "")
        self.password = config.get("password", "")
        self.from_addr = config.get("from_addr", "")
        self.recipients = config.get("recipients", [])
        self.subject_prefix = config.get("subject_prefix", "[Cosmic Replay]")
    
    def notify_task_completed(self, report_data: dict) -> bool:
        """任务完成后发送报告摘要邮件"""
        if not self.enabled or not self.recipients:
            return False
        
        try:
            summary = report_data.get("summary", {})
            task_id = report_data.get("task_id", "unknown")
            total = summary.get("total_cases", 0)
            passed = summary.get("passed_cases", 0)
            failed = summary.get("failed_cases", 0)
            pass_rate = summary.get("pass_rate", 0)
            duration = summary.get("total_duration_s", 0)
            
            # 构建邮件主题
            status_emoji = "\u2705" if failed == 0 else "\u274c"
            subject = f"{self.subject_prefix} {status_emoji} 任务 {task_id} 执行完成 - 通过率 {pass_rate*100:.1f}%"
            
            # 构建 HTML 邮件正文
            html_body = self._build_html_body(report_data)
            
            # 发送邮件
            return self._send_email(subject, html_body)
        except Exception as e:
            logger.error(f"发送通知邮件失败: {e}")
            return False
    
    def _build_html_body(self, report_data: dict) -> str:
        """构建 HTML 邮件正文"""
        summary = report_data.get("summary", {})
        errors = report_data.get("errors", [])
        case_results = report_data.get("case_results", [])
        performance = report_data.get("performance", {})
        
        total = summary.get("total_cases", 0)
        passed = summary.get("passed_cases", 0)
        failed = summary.get("failed_cases", 0)
        pass_rate = summary.get("pass_rate", 0)
        duration = summary.get("total_duration_s", 0)
        
        # 简洁的 HTML 邮件模板
        html = f"""
        <div style="font-family: -apple-system, sans-serif; max-width: 600px; margin: 0 auto; background: #1f2937; color: #e5e7eb; padding: 24px; border-radius: 8px;">
            <h2 style="color: #f9fafb; margin-top: 0;">执行报告摘要</h2>
            
            <table style="width: 100%; border-collapse: collapse; margin: 16px 0;">
                <tr>
                    <td style="padding: 12px; text-align: center; background: #374151; border-radius: 6px;">
                        <div style="font-size: 24px; font-weight: bold; color: #ffffff;">{total}</div>
                        <div style="font-size: 12px; color: #9ca3af;">总用例</div>
                    </td>
                    <td style="padding: 12px; text-align: center; background: #374151; border-radius: 6px;">
                        <div style="font-size: 24px; font-weight: bold; color: {'#22c55e' if pass_rate >= 1 else '#f59e0b'};">{pass_rate*100:.1f}%</div>
                        <div style="font-size: 12px; color: #9ca3af;">通过率</div>
                    </td>
                    <td style="padding: 12px; text-align: center; background: #374151; border-radius: 6px;">
                        <div style="font-size: 24px; font-weight: bold; color: #22c55e;">{passed}</div>
                        <div style="font-size: 12px; color: #9ca3af;">通过</div>
                    </td>
                    <td style="padding: 12px; text-align: center; background: #374151; border-radius: 6px;">
                        <div style="font-size: 24px; font-weight: bold; color: #ef4444;">{failed}</div>
                        <div style="font-size: 12px; color: #9ca3af;">失败</div>
                    </td>
                </tr>
            </table>
            
            <p style="color: #9ca3af; font-size: 13px;">总耗时: {duration:.1f}s | 平均步骤耗时: {performance.get('avg_step_duration_s', 0):.3f}s</p>
        """
        
        # 失败用例列表
        if errors:
            html += '<h3 style="color: #ef4444; margin-top: 20px;">失败用例</h3>'
            html += '<table style="width: 100%; border-collapse: collapse; font-size: 13px;">'
            for err in errors[:10]:  # 最多显示10条
                html += f'''
                <tr style="border-bottom: 1px solid #374151;">
                    <td style="padding: 8px; color: #f87171;">{err.get("case_name", "")}</td>
                    <td style="padding: 8px; color: #9ca3af;">{err.get("error", "")[:100]}</td>
                </tr>'''
            html += '</table>'
        
        html += '''
            <p style="color: #6b7280; font-size: 11px; margin-top: 24px; text-align: center;">
                — Cosmic Replay Notification —
            </p>
        </div>
        '''
        return html
    
    def _send_email(self, subject: str, html_body: str) -> bool:
        """发送邮件"""
        msg = MIMEMultipart('alternative')
        msg['Subject'] = subject
        msg['From'] = self.from_addr
        msg['To'] = ', '.join(self.recipients)
        
        msg.attach(MIMEText(html_body, 'html', 'utf-8'))
        
        try:
            if self.smtp_ssl:
                server = smtplib.SMTP_SSL(self.smtp_host, self.smtp_port, timeout=10)
            else:
                server = smtplib.SMTP(self.smtp_host, self.smtp_port, timeout=10)
                server.starttls()
            
            if self.username and self.password:
                server.login(self.username, self.password)
            
            server.sendmail(self.from_addr, self.recipients, msg.as_string())
            server.quit()
            logger.info(f"通知邮件已发送至 {self.recipients}")
            return True
        except Exception as e:
            logger.error(f"SMTP发送失败: {e}")
            return False

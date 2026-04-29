# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 2.0.x   | :white_check_mark: |
| 1.0.x   | :x:                |

---

## Reporting a Vulnerability

**请不要在公开Issue中报告安全漏洞。**

### 报告方式

发送邮件至：[维护者邮箱待补充]

邮件标题：`[Security] cosmic-replay-v4 安全漏洞报告`

### 邮件内容

1. 漏洞类型（如：XSS、SQL注入、认证绕过）
2. 影响版本
3. 复现步骤
4. 概念验证代码（如有）
5. 建议的修复方案（可选）

### 响应时间

- **确认收到**：24小时内
- **初步评估**：3个工作日内
- **修复计划**：根据严重程度，1-14天内

---

## 已知安全考虑

### 敏感信息存储

**风险**：配置文件可能包含明文密码

**缓解措施**：
- 支持 `${ENV_VAR}` 环境变量注入
- 不建议将 `config/envs/*.yaml` 提交到版本控制

### HAR文件处理

**风险**：HAR文件可能包含敏感信息（Cookie、Token）

**缓解措施**：
- HAR文件默认不提交到版本控制
- 建议在导入后手动清理敏感字段

### API访问控制

**现状**：v2.0.0 无API认证

**计划**：v2.1.0 添加 JWT/API Key 认证

---

## 安全最佳实践

### 部署

1. **环境变量**：使用环境变量存储敏感信息
   ```bash
   export COSMIC_PASSWORD="your-password"
   export COSMIC_COOKIE="your-cookie"
   ```

2. **HTTPS**：生产环境必须使用HTTPS
   ```bash
   # Nginx反向代理示例
   server {
       listen 443 ssl;
       ssl_certificate /path/to/cert.pem;
       ssl_certificate_key /path/to/key.pem;
   }
   ```

3. **访问控制**：限制服务仅内网访问
   ```yaml
   # docker-compose.yml
   services:
     cosmic-replay:
       ports:
         - "127.0.0.1:8765:8765"  # 仅本地访问
   ```

4. **资源限制**：设置容器资源限制
   ```yaml
   deploy:
     resources:
       limits:
         memory: 2G
   ```

### 配置

1. 不要将 `config/envs/*.yaml` 提交到Git
2. 使用环境变量或密钥管理服务
3. 定期更换密码和Token

### 日志

1. 检查日志是否包含敏感信息
2. 生产环境设置合适的日志级别
3. 定期归档和清理日志

---

## 安全更新

安全更新将在 [GitHub Releases](https://github.com/Marsmjy/cosmic-replay-v4/releases) 发布。

严重安全漏洞将通过以下方式通知：
- GitHub Security Advisories
- Release Notes 标注 `[Security]`

---

## 贡献者安全指南

如果你发现代码中的安全问题，请：
1. 不要公开提交Issue
2. 按上述流程报告
3. 等待修复后再公开讨论

感谢你帮助保持 cosmic-replay-v4 的安全！

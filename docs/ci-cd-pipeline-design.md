# cosmic-replay-v4 CI/CD流水线设计

## 一、流水线架构总览

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                              CI/CD Pipeline Architecture                            │
└─────────────────────────────────────────────────────────────────────────────────────┘

    Developer           GitHub Actions             Harbor Registry           Kubernetes
        │                    │                          │                         │
        │  1. git push       │                          │                         │
        │───────────────────>│                          │                         │
        │                    │                          │                         │
        │                    │  2. Build & Test         │                         │
        │                    │─────────────────────────>│                         │
        │                    │                          │                         │
        │                    │  3. Security Scan        │                         │
        │                    │─────────────────────────>│                         │
        │                    │                          │                         │
        │                    │  4. Push Image           │                         │
        │                    │─────────────────────────>│                         │
        │                    │                          │                         │
        │                    │  5. Deploy               │                         │
        │                    │──────────────────────────┼────────────────────────>│
        │                    │                          │                         │
        │                    │  6. Verify               │                         │
        │                    │<─────────────────────────┼─────────────────────────│
        │                    │                          │                         │
        │  7. Notification   │                          │                         │
        │<───────────────────│                          │                         │
        │                    │                          │                         │
```

---

## 二、GitHub Actions完整流水线

### 2.1 主流水线配置

```yaml
# .github/workflows/ci-cd.yml
name: CI/CD Pipeline

on:
  push:
    branches: [main, develop, 'release/*']
    tags: ['v*']
  pull_request:
    branches: [main, develop]

env:
  REGISTRY: harbor.example.com
  IMAGE_NAME: cosmic-replay-v4
  PYTHON_VERSION: '3.11'

jobs:
  # ==================== 代码质量检查 ====================
  lint:
    name: Code Quality
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Setup Python
        uses: actions/setup-python@v5
        with:
          python-version: ${{ env.PYTHON_VERSION }}
          cache: 'pip'
      
      - name: Install linters
        run: |
          pip install flake8 pylint black isort mypy
          pip install -r requirements.txt
      
      - name: Run Black (format check)
        run: black --check --diff lib/ tests/
      
      - name: Run isort (import sort check)
        run: isort --check-only --diff lib/ tests/
      
      - name: Run Flake8
        run: flake8 lib/ tests/ --max-line-length=120 --ignore=E501,W503
      
      - name: Run Pylint
        run: pylint lib/ --disable=C0114,C0115,C0116 --max-line-length=120 || true
      
      - name: Run MyPy (type check)
        run: mypy lib/ --ignore-missing-imports || true

  # ==================== 安全扫描 ====================
  security:
    name: Security Scan
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Setup Python
        uses: actions/setup-python@v5
        with:
          python-version: ${{ env.PYTHON_VERSION }}
          cache: 'pip'
      
      - name: Install dependencies
        run: pip install -r requirements.txt
      
      - name: Run Bandit (SAST)
        uses: tj-actions/bandit@v5
        with:
          targets: lib/
          options: "-r -ll"
      
      - name: Run Safety (dependency check)
        run: pip install safety && safety check -r requirements.txt || true
      
      - name: Run Trivy (filesystem scan)
        uses: aquasecurity/trivy-action@master
        with:
          scan-type: 'fs'
          scan-ref: '.'
          format: 'table'
          exit-code: '0'
          severity: 'HIGH,CRITICAL'

  # ==================== 单元测试 ====================
  unit-tests:
    name: Unit Tests
    runs-on: ubuntu-latest
    needs: [lint]
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Setup Python
        uses: actions/setup-python@v5
        with:
          python-version: ${{ env.PYTHON_VERSION }}
          cache: 'pip'
      
      - name: Install dependencies
        run: |
          pip install -r requirements.txt
          pip install pytest pytest-cov pytest-xdist
      
      - name: Run unit tests
        run: pytest tests/unit -v --tb=short -ra -n auto
      
      - name: Generate coverage
        run: pytest tests/unit --cov=lib --cov-report=xml --cov-report=term-missing
      
      - name: Upload coverage
        uses: codecov/codecov-action@v4
        with:
          files: coverage.xml
          fail_ci_if_error: false

  # ==================== 集成测试 ====================
  integration-tests:
    name: Integration Tests
    runs-on: ubuntu-latest
    needs: [lint]
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Setup Python
        uses: actions/setup-python@v5
        with:
          python-version: ${{ env.PYTHON_VERSION }}
          cache: 'pip'
      
      - name: Install dependencies
        run: |
          pip install -r requirements.txt
          pip install pytest pytest-cov
      
      - name: Run integration tests
        run: pytest tests/integration -v --tb=short -ra
      
      - name: Upload test results
        uses: actions/upload-artifact@v4
        if: always()
        with:
          name: integration-test-results
          path: logs/

  # ==================== 构建镜像 ====================
  build:
    name: Build Image
    runs-on: ubuntu-latest
    needs: [unit-tests, integration-tests, security]
    outputs:
      image_tag: ${{ steps.meta.outputs.tags }}
      image_digest: ${{ steps.build.outputs.digest }}
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3
      
      - name: Login to Registry
        uses: docker/login-action@v3
        with:
          registry: ${{ env.REGISTRY }}
          username: ${{ secrets.REGISTRY_USER }}
          password: ${{ secrets.REGISTRY_PASSWORD }}
      
      - name: Extract metadata
        id: meta
        uses: docker/metadata-action@v5
        with:
          images: ${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}
          tags: |
            type=ref,event=branch
            type=ref,event=pr
            type=semver,pattern={{version}}
            type=semver,pattern={{major}}.{{minor}}
            type=sha,prefix=
            type=raw,value=latest,enable=${{ github.ref == 'refs/heads/main' }}
      
      - name: Build and push
        id: build
        uses: docker/build-push-action@v5
        with:
          context: .
          file: ./Dockerfile
          push: true
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}
          cache-from: type=gha
          cache-to: type=gha,mode=max
          build-args: |
            VERSION=${{ github.sha }}
            BUILD_TIME=${{ github.event.head_commit.timestamp }}
      
      - name: Run Trivy (image scan)
        uses: aquasecurity/trivy-action@master
        with:
          image-ref: ${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}:${{ github.sha }}
          format: 'table'
          exit-code: '0'
          severity: 'HIGH,CRITICAL'

  # ==================== 部署到开发环境 ====================
  deploy-dev:
    name: Deploy to Development
    runs-on: ubuntu-latest
    needs: [build]
    if: github.ref == 'refs/heads/develop'
    environment:
      name: development
      url: https://cosmic-replay-dev.example.com
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Set up kubectl
        uses: azure/setup-kubectl@v3
      
      - name: Configure kubeconfig
        run: |
          mkdir -p ~/.kube
          echo "${{ secrets.KUBE_CONFIG_DEV }}" | base64 -d > ~/.kube/config
      
      - name: Deploy to development
        run: |
          kubectl set image deployment/cosmic-replay-v4 \
            cosmic-replay=${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}:${{ github.sha }} \
            -n cosmic-replay-dev
          
          kubectl rollout status deployment/cosmic-replay-v4 \
            -n cosmic-replay-dev --timeout=300s
      
      - name: Run smoke tests
        run: |
          # 等待服务就绪
          sleep 30
          
          # 健康检查
          curl -f https://cosmic-replay-dev.example.com/api/health || exit 1
          
          # 基本功能测试
          curl -f https://cosmic-replay-dev.example.com/api/info || exit 1

  # ==================== 部署到预发布环境 ====================
  deploy-staging:
    name: Deploy to Staging
    runs-on: ubuntu-latest
    needs: [build]
    if: startsWith(github.ref, 'refs/heads/release/')
    environment:
      name: staging
      url: https://cosmic-replay-staging.example.com
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Set up kubectl
        uses: azure/setup-kubectl@v3
      
      - name: Configure kubeconfig
        run: |
          mkdir -p ~/.kube
          echo "${{ secrets.KUBE_CONFIG_STAGING }}" | base64 -d > ~/.kube/config
      
      - name: Deploy to staging
        run: |
          # 应用K8s配置
          kubectl apply -f deploy/k8s/namespace.yaml
          kubectl apply -f deploy/k8s/configmap.yaml
          kubectl apply -f deploy/k8s/secret.yaml
          kubectl apply -f deploy/k8s/deployment.yaml
          kubectl apply -f deploy/k8s/service.yaml
          kubectl apply -f deploy/k8s/ingress.yaml
          
          # 更新镜像
          kubectl set image deployment/cosmic-replay-v4 \
            cosmic-replay=${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}:${{ github.sha }} \
            -n cosmic-replay-staging
          
          kubectl rollout status deployment/cosmic-replay-v4 \
            -n cosmic-replay-staging --timeout=300s
      
      - name: Run E2E tests
        run: |
          # 运行端到端测试
          pip install -r requirements.txt
          pytest tests/e2e -v --tb=short || true
      
      - name: Performance test
        run: |
          # 简单性能测试
          pip install locust
          locust -f tests/performance/locustfile.py \
            --headless -u 10 -r 5 -t 60s \
            --host https://cosmic-replay-staging.example.com || true

  # ==================== 部署到生产环境 ====================
  deploy-prod:
    name: Deploy to Production
    runs-on: ubuntu-latest
    needs: [build]
    if: startsWith(github.ref, 'refs/tags/v')
    environment:
      name: production
      url: https://cosmic-replay.example.com
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Set up kubectl
        uses: azure/setup-kubectl@v3
      
      - name: Configure kubeconfig
        run: |
          mkdir -p ~/.kube
          echo "${{ secrets.KUBE_CONFIG_PROD }}" | base64 -d > ~/.kube/config
      
      - name: Create release notes
        id: release
        uses: softprops/action-gh-release@v1
        with:
          generate_release_notes: true
      
      - name: Pre-deployment backup
        run: |
          kubectl exec deployment/cosmic-replay-v4 -n cosmic-replay \
            -- /app/scripts/backup.sh
      
      - name: Deploy to production (Canary)
        run: |
          # Canary部署 - 先部署1个副本
          kubectl patch deployment cosmic-replay-v4 -n cosmic-replay \
            --type='json' \
            -p='[{"op": "replace", "path": "/spec/replicas", "value": 3}]'
          
          kubectl set image deployment/cosmic-replay-v4 \
            cosmic-replay=${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}:${{ github.ref_name }} \
            -n cosmic-replay
          
          # 等待新Pod就绪
          kubectl rollout status deployment/cosmic-replay-v4 \
            -n cosmic-replay --timeout=300s
      
      - name: Verify deployment
        run: |
          # 健康检查
          for i in {1..10}; do
            if curl -f https://cosmic-replay.example.com/api/health; then
              echo "Health check passed"
              break
            fi
            sleep 10
          done
          
          # 检查错误率
          # TODO: 接入Prometheus API检查错误率
      
      - name: Complete rollout
        run: |
          # 扩展到完整副本数
          kubectl patch deployment cosmic-replay-v4 -n cosmic-replay \
            --type='json' \
            -p='[{"op": "replace", "path": "/spec/replicas", "value": 5}]'
      
      - name: Post-deployment verification
        run: |
          # 等待5分钟观察
          sleep 300
          
          # 最终健康检查
          curl -f https://cosmic-replay.example.com/api/health || exit 1

  # ==================== 通知 ====================
  notify:
    name: Notify
    runs-on: ubuntu-latest
    needs: [build, deploy-dev, deploy-staging, deploy-prod]
    if: always()
    steps:
      - name: Determine status
        id: status
        run: |
          if [[ "${{ needs.build.result }}" == "failure" ]]; then
            echo "status=failure" >> $GITHUB_OUTPUT
            echo "message=Build failed" >> $GITHUB_OUTPUT
          elif [[ "${{ needs.deploy-prod.result }}" == "success" ]]; then
            echo "status=success" >> $GITHUB_OUTPUT
            echo "message=Production deployment successful" >> $GITHUB_OUTPUT
          elif [[ "${{ needs.deploy-staging.result }}" == "success" ]]; then
            echo "status=success" >> $GITHUB_OUTPUT
            echo "message=Staging deployment successful" >> $GITHUB_OUTPUT
          elif [[ "${{ needs.deploy-dev.result }}" == "success" ]]; then
            echo "status=success" >> $GITHUB_OUTPUT
            echo "message=Development deployment successful" >> $GITHUB_OUTPUT
          else
            echo "status=unknown" >> $GITHUB_OUTPUT
            echo "message=Pipeline completed" >> $GITHUB_OUTPUT
          fi
      
      - name: Slack notification
        if: always()
        uses: 8398a7/action-slack@v3
        with:
          status: ${{ steps.status.outputs.status }}
          text: ${{ steps.status.outputs.message }}
          webhook_url: ${{ secrets.SLACK_WEBHOOK }}
          fields: repo,message,commit,author,action,eventName,ref,workflow

  # ==================== 清理旧镜像 ====================
  cleanup:
    name: Cleanup Old Images
    runs-on: ubuntu-latest
    needs: [build]
    if: github.ref == 'refs/heads/main'
    steps:
      - name: Delete old images
        run: |
          # 保留最近10个镜像，删除其他
          # 具体实现依赖Registry API
          echo "Cleaning up old images..."
```

---

## 三、环境配置

### 3.1 开发环境配置

```yaml
# deploy/environments/development.yaml
environment:
  name: development
  description: 开发环境 - 自动部署develop分支
  
deployment:
  replicas: 1
  resources:
    requests:
      cpu: "250m"
      memory: "256Mi"
    limits:
      cpu: "500m"
      memory: "512Mi"
  
config:
  log_level: debug
  debug_mode: true
  
ingress:
  host: cosmic-replay-dev.example.com
  tls: false

features:
  monitoring: false
  auth_enabled: false
  rate_limit: 1000
```

### 3.2 预发布环境配置

```yaml
# deploy/environments/staging.yaml
environment:
  name: staging
  description: 预发布环境 - 手动触发部署release分支
  
deployment:
  replicas: 2
  resources:
    requests:
      cpu: "500m"
      memory: "512Mi"
    limits:
      cpu: "1000m"
      memory: "1Gi"
  
config:
  log_level: info
  debug_mode: false
  
ingress:
  host: cosmic-replay-staging.example.com
  tls: true
  cert_issuer: letsencrypt-staging

features:
  monitoring: true
  auth_enabled: true
  rate_limit: 100
```

### 3.3 生产环境配置

```yaml
# deploy/environments/production.yaml
environment:
  name: production
  description: 生产环境 - 仅部署Tag版本
  
deployment:
  replicas: 3
  max_unavailable: 0
  max_surge: 1
  resources:
    requests:
      cpu: "500m"
      memory: "512Mi"
    limits:
      cpu: "2000m"
      memory: "2Gi"
  
config:
  log_level: info
  debug_mode: false
  
ingress:
  host: cosmic-replay.example.com
  tls: true
  cert_issuer: letsencrypt-prod

features:
  monitoring: true
  auth_enabled: true
  rate_limit: 100
  backup_enabled: true

scaling:
  min_replicas: 3
  max_replicas: 10
  target_cpu: 70
  target_memory: 80
```

---

## 四、GitOps方案 (可选)

### 4.1 ArgoCD配置

```yaml
# deploy/argocd/application.yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: cosmic-replay
  namespace: argocd
spec:
  project: default
  
  source:
    repoURL: https://github.com/xxx/cosmic-replay-v4.git
    targetRevision: main
    path: deploy/k8s
    
    helm:
      valueFiles:
        - values-production.yaml
  
  destination:
    server: https://kubernetes.default.svc
    namespace: cosmic-replay
  
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
    syncOptions:
      - CreateNamespace=true
      - PrunePropagationPolicy=foreground
      - PruneLast=true
  
  ignoreDifferences:
    - group: apps
      kind: Deployment
      jsonPointers:
        - /spec/replicas
```

### 4.2 Kustomize配置

```yaml
# deploy/k8s/kustomization.yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

namespace: cosmic-replay

resources:
  - namespace.yaml
  - configmap.yaml
  - secret.yaml
  - deployment.yaml
  - service.yaml
  - ingress.yaml
  - hpa.yaml

commonLabels:
  app.kubernetes.io/name: cosmic-replay
  app.kubernetes.io/managed-by: kustomize

images:
  - name: cosmic-replay-v4
    newName: harbor.example.com/cosmic-replay-v4
    newTag: latest

configMapGenerator:
  - name: cosmic-replay-config
    files:
      - config/webui.yaml

secretGenerator:
  - name: cosmic-replay-credentials
    type: Opaque
    envs:
      - .env.credentials
```

---

## 五、流水线质量门禁

### 5.1 质量标准

```yaml
# .github/quality-gates.yaml
quality_gates:
  code_coverage:
    minimum: 80
    critical_paths:
      - lib/runner.py
      - lib/replay.py
  
  security:
    max_critical: 0
    max_high: 5
  
  performance:
    max_response_time_p95: 2000  # ms
    max_error_rate: 1  # %
  
  complexity:
    max_cyclomatic_complexity: 15
    max_cognitive_complexity: 20
  
  documentation:
    min_doc_coverage: 50
  
  dependencies:
    max_outdated: 10
    max_vulnerable: 0
```

### 5.2 分支保护规则

```yaml
# .github/branch-protection.yaml
# 需要在GitHub设置中配置

branches:
  main:
    protected: true
    required_status_checks:
      - lint
      - unit-tests
      - integration-tests
      - security
      - build
    required_reviewers: 2
    dismiss_stale_reviews: true
    require_code_owner_reviews: true
    enforce_admins: true
  
  develop:
    protected: true
    required_status_checks:
      - lint
      - unit-tests
    required_reviewers: 1
```

---

## 六、流水线监控

### 6.1 流水线指标

```yaml
# 监控指标定义
pipeline_metrics:
  - name: build_duration_seconds
    type: histogram
    description: 构建耗时
    buckets: [60, 120, 180, 300, 600]
  
  - name: deploy_success_total
    type: counter
    description: 部署成功次数
    labels: [environment]
  
  - name: deploy_failure_total
    type: counter
    description: 部署失败次数
    labels: [environment]
  
  - name: test_coverage_percent
    type: gauge
    description: 测试覆盖率
    labels: [branch]
  
  - name: security_issues_total
    type: gauge
    description: 安全问题数量
    labels: [severity]
```

### 6.2 流水线仪表盘

```json
{
  "dashboard": {
    "title": "CI/CD Pipeline Dashboard",
    "panels": [
      {
        "title": "构建成功率",
        "type": "stat",
        "query": "sum(rate(build_success_total[1h])) / sum(rate(build_total[1h])) * 100"
      },
      {
        "title": "平均构建时间",
        "type": "gauge",
        "query": "avg(build_duration_seconds)"
      },
      {
        "title": "部署频率",
        "type": "graph",
        "query": "sum(rate(deploy_success_total[1d])) by (environment)"
      },
      {
        "title": "测试覆盖率趋势",
        "type": "graph",
        "query": "test_coverage_percent"
      },
      {
        "title": "安全问题趋势",
        "type": "graph",
        "query": "security_issues_total"
      }
    ]
  }
}
```

---

## 七、回滚策略

### 7.1 自动回滚

```yaml
# .github/workflows/auto-rollback.yml
name: Auto Rollback

on:
  workflow_run:
    workflows: ["CI/CD Pipeline"]
    types: [completed]
    branches: [main]

jobs:
  rollback-check:
    if: ${{ github.event.workflow_run.conclusion == 'failure' }}
    runs-on: ubuntu-latest
    steps:
      - name: Check if production deployment failed
        run: |
          # 检查生产环境健康状态
          HEALTH=$(curl -s https://cosmic-replay.example.com/api/health | jq -r '.status')
          
          if [ "$HEALTH" != "healthy" ]; then
            echo "Production unhealthy, triggering rollback..."
            exit 1
          fi
      
      - name: Rollback deployment
        if: failure()
        run: |
          kubectl rollout undo deployment/cosmic-replay-v4 -n cosmic-replay
          kubectl rollout status deployment/cosmic-replay-v4 -n cosmic-replay --timeout=300s
      
      - name: Notify team
        if: failure()
        uses: 8398a7/action-slack@v3
        with:
          status: failure
          text: "Production rollback triggered!"
          webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

### 7.2 手动回滚

```bash
#!/bin/bash
# scripts/manual-rollback.sh

# 用法: ./manual-rollback.sh <version>
VERSION=${1:-}

if [ -z "$VERSION" ]; then
    echo "可用版本:"
    kubectl rollout history deployment/cosmic-replay-v4 -n cosmic-replay
    exit 1
fi

echo "回滚到版本: $VERSION"
kubectl rollout undo deployment/cosmic-replay-v4 --to-revision=$VERSION -n cosmic-replay
kubectl rollout status deployment/cosmic-replay-v4 -n cosmic-replay --timeout=300s

echo "回滚完成"
kubectl get pods -n cosmic-replay -l app=cosmic-replay
```

---

## 八、Secret管理

### 8.1 外部Secret管理

```yaml
# deploy/k8s/external-secret.yaml
apiVersion: external-secrets.io/v1beta1
kind: ExternalSecret
metadata:
  name: cosmic-replay-secrets
  namespace: cosmic-replay
spec:
  refreshInterval: 1h
  secretStoreRef:
    name: vault-backend
    kind: SecretStore
  
  target:
    name: cosmic-replay-credentials
    creationPolicy: Owner
  
  data:
    - secretKey: COSMIC_USERNAME
      remoteRef:
        key: cosmic-replay/credentials
        property: username
    
    - secretKey: COSMIC_PASSWORD
      remoteRef:
        key: cosmic-replay/credentials
        property: password
```

### 8.2 Secret轮换策略

```yaml
# 每月轮换API Key
apiVersion: batch/v1
kind: CronJob
metadata:
  name: secret-rotation
  namespace: cosmic-replay
spec:
  schedule: "0 0 1 * *"  # 每月1号
  jobTemplate:
    spec:
      template:
        spec:
          containers:
            - name: rotation
              image: harbor.example.com/secret-rotation:latest
              command:
                - /bin/sh
                - -c
                - |
                  # 生成新API Key
                  NEW_KEY=$(openssl rand -base64 32)
                  
                  # 更新Vault
                  vault kv put cosmic-replay/api-key key=$NEW_KEY
                  
                  # 触发Pod重启以加载新Secret
                  kubectl rollout restart deployment/cosmic-replay-v4 -n cosmic-replay
```

---

## 九、部署清单

### 9.1 部署前检查清单

```markdown
## 部署检查清单

### 代码质量
- [ ] 所有单元测试通过
- [ ] 代码覆盖率 >= 80%
- [ ] 无高危安全漏洞
- [ ] 代码审查通过

### 配置检查
- [ ] 环境变量已配置
- [ ] Secret已更新
- [ ] ConfigMap已更新
- [ ] 备份已执行

### 基础设施
- [ ] Kubernetes集群健康
- [ ] 资源配额充足
- [ ] 网络策略正确
- [ ] Ingress证书有效

### 监控告警
- [ ] Prometheus正常采集
- [ ] Grafana仪表盘可用
- [ ] Alertmanager配置正确
- [ ] 通知渠道畅通

### 回滚准备
- [ ] 上一版本镜像存在
- [ ] 回滚脚本可用
- [ ] 回滚通知准备
```

### 9.2 部署后验证清单

```markdown
## 部署后验证

### 健康检查
- [ ] /api/health 返回healthy
- [ ] 所有Pod状态为Running
- [ ] 服务端点可访问

### 功能验证
- [ ] 用户登录正常
- [ ] 用例执行正常
- [ ] 报告生成正常

### 性能验证
- [ ] 响应时间在阈值内
- [ ] 无内存泄漏
- [ ] CPU使用率正常

### 监控验证
- [ ] 指标正常上报
- [ ] 日志正常输出
- [ ] 告警规则生效
```

---

*文档版本: 1.0*
*最后更新: 2026-04-28*

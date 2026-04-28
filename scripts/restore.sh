#!/bin/bash
# cosmic-replay-v2 恢复脚本
# 用法: ./scripts/restore.sh <backup_file.tar.gz>

set -e

# 配置变量
BACKUP_DIR=${BACKUP_DIR:-"/data/backups/cosmic-replay"}
APP_DIR=${APP_DIR:-"/app"}

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 显示可用备份
list_backups() {
    log_info "可用备份列表:"
    echo ""
    ls -lht "${BACKUP_DIR}"/cosmic-replay-*-data.tar.gz 2>/dev/null | head -10
    echo ""
    log_info "用法: $0 <backup_file.tar.gz>"
}

# 主流程
main() {
    BACKUP_FILE=${1:-}
    
    log_info "=========================================="
    log_info "cosmic-replay-v2 恢复脚本"
    log_info "=========================================="
    
    # 检查备份文件
    if [ -z "${BACKUP_FILE}" ]; then
        list_backups
        exit 0
    fi
    
    # 如果只有文件名，补全路径
    if [[ "${BACKUP_FILE}" != /* ]]; then
        BACKUP_FILE="${BACKUP_DIR}/${BACKUP_FILE}"
    fi
    
    if [ ! -f "${BACKUP_FILE}" ]; then
        log_error "备份文件不存在: ${BACKUP_FILE}"
        list_backups
        exit 1
    fi
    
    log_info "备份文件: ${BACKUP_FILE}"
    
    # 校验备份
    BACKUP_BASENAME=$(basename "${BACKUP_FILE}" -data.tar.gz)
    if [ -f "${BACKUP_DIR}/${BACKUP_BASENAME}.sha256" ]; then
        log_info "校验备份完整性..."
        cd "${BACKUP_DIR}"
        if sha256sum -c "${BACKUP_BASENAME}.sha256" > /dev/null 2>&1; then
            log_info "校验通过"
        else
            log_error "校验失败，备份文件可能已损坏"
            read -p "是否继续恢复? (y/N): " continue_restore
            if [ "${continue_restore}" != "y" ]; then
                exit 1
            fi
        fi
    fi
    
    # 确认操作
    log_warn "警告: 恢复将覆盖现有数据!"
    echo ""
    log_info "当前数据将被备份到: /tmp/cosmic-replay-current-$(date +%s).tar.gz"
    echo ""
    read -p "确认继续恢复? (y/N): " confirm
    if [ "${confirm}" != "y" ]; then
        log_info "已取消"
        exit 0
    fi
    
    # 备份当前数据
    CURRENT_BACKUP="/tmp/cosmic-replay-current-$(date +%s).tar.gz"
    log_info "备份当前数据到: ${CURRENT_BACKUP}"
    tar -czf "${CURRENT_BACKUP}" \
        -C "${APP_DIR}" \
        cases/ config/ 2>/dev/null || true
    
    # 停止服务
    log_info "停止服务..."
    if command -v systemctl &> /dev/null; then
        systemctl stop cosmic-replay 2>/dev/null || true
    fi
    
    # 恢复数据
    log_info "恢复数据..."
    tar -xzf "${BACKUP_FILE}" -C "${APP_DIR}"
    
    # 恢复日志（如果存在）
    LOG_BACKUP="${BACKUP_DIR}/${BACKUP_BASENAME}-logs.tar.gz"
    if [ -f "${LOG_BACKUP}" ]; then
        log_info "恢复日志..."
        tar -xzf "${LOG_BACKUP}" -C "${APP_DIR}" || true
    fi
    
    # 恢复数据库（如果存在）
    DB_BACKUP="${BACKUP_DIR}/${BACKUP_BASENAME}-db.sql"
    if [ -f "${DB_BACKUP}" ]; then
        log_info "恢复数据库..."
        if [ -n "${DB_HOST}" ]; then
            mysql -h "${DB_HOST}" -u "${DB_USER}" -p"${DB_PASS}" \
                cosmic_replay < "${DB_BACKUP}" || {
                log_warn "数据库恢复失败"
            }
        fi
    fi
    
    # 设置权限
    log_info "设置权限..."
    chown -R cosmic:cosmic "${APP_DIR}/cases" "${APP_DIR}/config" 2>/dev/null || true
    
    # 启动服务
    log_info "启动服务..."
    if command -v systemctl &> /dev/null; then
        systemctl start cosmic-replay
    fi
    
    # 验证恢复
    log_info "验证服务状态..."
    sleep 5
    
    HEALTH_URL="http://localhost:8766/api/health"
    for i in {1..10}; do
        if curl -sf "${HEALTH_URL}" > /dev/null 2>&1; then
            log_info "服务恢复成功"
            break
        fi
        sleep 2
    done
    
    echo ""
    log_info "=========================================="
    log_info "恢复完成"
    log_info "如需回滚: tar -xzf ${CURRENT_BACKUP} -C ${APP_DIR}"
    log_info "=========================================="
}

# 执行
main "$@"

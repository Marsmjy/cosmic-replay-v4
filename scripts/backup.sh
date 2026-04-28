#!/bin/bash
# cosmic-replay-v2 备份脚本
# 用法: ./scripts/backup.sh

set -e

# 配置变量
BACKUP_DIR=${BACKUP_DIR:-"/data/backups/cosmic-replay"}
RETENTION_DAYS=${RETENTION_DAYS:-30}
APP_DIR=${APP_DIR:-"/app"}
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_NAME="cosmic-replay-${DATE}"
S3_BUCKET=${S3_BUCKET:-""}

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

# 检查目录
check_directories() {
    if [ ! -d "${APP_DIR}" ]; then
        log_error "应用目录不存在: ${APP_DIR}"
        exit 1
    fi
    
    mkdir -p "${BACKUP_DIR}"
    log_info "备份目录: ${BACKUP_DIR}"
}

# 创建备份
create_backup() {
    log_info "开始备份: ${DATE}"
    
    # 备份数据目录
    log_info "备份用例和配置..."
    tar -czf "${BACKUP_DIR}/${BACKUP_NAME}-data.tar.gz" \
        -C "${APP_DIR}" \
        cases/ \
        config/ \
        --exclude='*.log' \
        --exclude='*.pyc' \
        --exclude='__pycache__' \
        2>/dev/null || {
        log_warn "数据目录备份可能不完整"
    }
    
    # 备份日志（压缩）
    if [ -d "${APP_DIR}/logs" ]; then
        log_info "备份日志..."
        tar -czf "${BACKUP_DIR}/${BACKUP_NAME}-logs.tar.gz" \
            -C "${APP_DIR}" logs/ 2>/dev/null || {
            log_warn "日志备份可能不完整"
        }
    fi
    
    # 备份数据库（如果使用）
    if [ -n "${DB_HOST}" ]; then
        log_info "备份数据库..."
        mysqldump -h "${DB_HOST}" -u "${DB_USER}" -p"${DB_PASS}" \
            cosmic_replay > "${BACKUP_DIR}/${BACKUP_NAME}-db.sql" 2>/dev/null || {
            log_warn "数据库备份可能不完整"
        }
    fi
    
    # 生成校验和
    log_info "生成校验和..."
    cd "${BACKUP_DIR}"
    sha256sum ${BACKUP_NAME}* > "${BACKUP_NAME}.sha256"
    
    log_info "备份文件:"
    ls -lh "${BACKUP_DIR}/${BACKUP_NAME}"*
}

# 上传到远程存储
upload_to_remote() {
    if [ -n "${S3_BUCKET}" ]; then
        log_info "上传到S3: ${S3_BUCKET}"
        aws s3 sync "${BACKUP_DIR}/${BACKUP_NAME}"* \
            "s3://${S3_BUCKET}/backups/" \
            --storage-class STANDARD_IA || {
            log_warn "S3上传失败"
        }
    fi
}

# 清理旧备份
cleanup_old_backups() {
    log_info "清理${RETENTION_DAYS}天前的备份..."
    find "${BACKUP_DIR}" -name "cosmic-replay-*.tar.gz" -mtime +${RETENTION_DAYS} -delete
    find "${BACKUP_DIR}" -name "cosmic-replay-*.sha256" -mtime +${RETENTION_DAYS} -delete
    find "${BACKUP_DIR}" -name "cosmic-replay-*.sql" -mtime +${RETENTION_DAYS} -delete
    
    # 清理空文件
    find "${BACKUP_DIR}" -type f -empty -delete 2>/dev/null || true
    
    log_info "当前备份列表:"
    ls -lht "${BACKUP_DIR}" | head -10
}

# 验证备份
verify_backup() {
    log_info "验证备份完整性..."
    
    if [ -f "${BACKUP_DIR}/${BACKUP_NAME}.sha256" ]; then
        cd "${BACKUP_DIR}"
        if sha256sum -c "${BACKUP_NAME}.sha256" > /dev/null 2>&1; then
            log_info "备份校验通过"
            return 0
        else
            log_error "备份校验失败"
            return 1
        fi
    fi
}

# 发送通知
send_notification() {
    local status=$1
    local message=$2
    
    if [ -n "${WEBHOOK_URL}" ]; then
        curl -s -X POST "${WEBHOOK_URL}" \
            -H 'Content-Type: application/json' \
            -d "{\"status\": \"${status}\", \"message\": \"${message}\", \"backup\": \"${BACKUP_NAME}\"}" \
            > /dev/null 2>&1 || true
    fi
}

# 主流程
main() {
    log_info "=========================================="
    log_info "cosmic-replay-v2 备份脚本"
    log_info "=========================================="
    
    check_directories
    
    if create_backup; then
        verify_backup
        upload_to_remote
        cleanup_old_backups
        send_notification "success" "备份成功: ${BACKUP_NAME}"
        log_info "备份完成: ${BACKUP_NAME}"
    else
        send_notification "failure" "备份失败"
        log_error "备份失败"
        exit 1
    fi
}

# 执行
main "$@"

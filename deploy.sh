#!/bin/bash

# ==========================================
# 项目部署脚本
# 支持开发环境和生产环境部署
# ==========================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 默认配置
ENVIRONMENT="dev"
COMPOSE_FILE="docker-compose.yml"
REGISTRY="ghcr.io"
IMAGE_NAME="myjavaproject"

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  -e, --environment    部署环境 (dev|staging|prod) [默认: dev]"
    echo "  -t, --tag           镜像标签 [默认: latest]"
    echo "  -r, --registry      镜像仓库地址 [默认: ghcr.io]"
    echo "  -h, --help          显示帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 -e dev                              # 部署开发环境"
    echo "  $0 -e prod -t v1.0.0                   # 部署生产环境，使用 v1.0.0 标签"
    echo "  $0 -e staging -t develop               # 部署测试环境，使用 develop 标签"
}

# 打印带颜色的信息
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 Docker 和 Docker Compose
check_docker() {
    log_info "检查 Docker 环境..."
    
    if ! command -v docker &> /dev/null; then
        log_error "Docker 未安装，请先安装 Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose 未安装，请先安装 Docker Compose"
        exit 1
    fi
    
    # 检查 Docker 服务是否运行
    if ! docker info &> /dev/null; then
        log_error "Docker 服务未运行，请启动 Docker 服务"
        exit 1
    fi
    
    log_success "Docker 环境检查通过"
}

# 加载环境变量
load_env() {
    local env_file=".env.${ENVIRONMENT}"
    
    if [ -f "$env_file" ]; then
        log_info "加载环境变量文件: $env_file"
        export $(grep -v '^#' "$env_file" | xargs)
    elif [ -f ".env" ]; then
        log_info "加载环境变量文件: .env"
        export $(grep -v '^#' ".env" | xargs)
    else
        log_warning "未找到环境变量文件，使用默认配置"
    fi
}

# 设置环境配置
setup_environment() {
    case $ENVIRONMENT in
        dev)
            COMPOSE_FILE="docker-compose.yml"
            log_info "使用开发环境配置"
            ;;
        staging)
            COMPOSE_FILE="docker-compose.dev.yml"
            log_info "使用测试环境配置"
            ;;
        prod)
            COMPOSE_FILE="docker-compose.prod.yml"
            log_info "使用生产环境配置"
            ;;
        *)
            log_error "未知的环境: $ENVIRONMENT"
            exit 1
            ;;
    esac
    
    if [ ! -f "$COMPOSE_FILE" ]; then
        log_error "Compose 文件不存在: $COMPOSE_FILE"
        exit 1
    fi
}

# 登录镜像仓库
login_registry() {
    if [ -n "$DOCKER_REGISTRY_TOKEN" ]; then
        log_info "登录镜像仓库: $REGISTRY"
        echo "$DOCKER_REGISTRY_TOKEN" | docker login "$REGISTRY" -u "$DOCKER_REGISTRY_USERNAME" --password-stdin
        log_success "镜像仓库登录成功"
    else
        log_warning "未设置 DOCKER_REGISTRY_TOKEN，跳过登录"
    fi
}

# 拉取最新镜像
pull_images() {
    log_info "拉取最新镜像..."
    
    if [ -n "$TAG" ]; then
        export DOCKER_REGISTRY_URL="$REGISTRY"
        export IMAGE_TAG="$TAG"
    fi
    
    docker-compose -f "$COMPOSE_FILE" pull
    log_success "镜像拉取完成"
}

# 部署服务
deploy() {
    log_info "开始部署服务..."
    log_info "环境: $ENVIRONMENT"
    log_info "Compose 文件: $COMPOSE_FILE"
    
    # 停止旧服务
    log_info "停止旧服务..."
    docker-compose -f "$COMPOSE_FILE" down --remove-orphans
    
    # 启动新服务
    log_info "启动新服务..."
    docker-compose -f "$COMPOSE_FILE" up -d
    
    # 等待服务启动
    log_info "等待服务启动..."
    sleep 10
    
    # 检查服务状态
    check_services
    
    log_success "部署完成！"
}

# 检查服务状态
check_services() {
    log_info "检查服务状态..."
    
    local services
    services=$(docker-compose -f "$COMPOSE_FILE" ps -q)
    
    if [ -z "$services" ]; then
        log_error "没有运行中的服务"
        return 1
    fi
    
    for service in $services; do
        local name
        name=$(docker inspect --format='{{.Name}}' "$service" | sed 's/\///')
        local status
        status=$(docker inspect --format='{{.State.Status}}' "$service")
        
        if [ "$status" = "running" ]; then
            log_success "服务 $name 运行正常"
        else
            log_error "服务 $name 状态异常: $status"
        fi
    done
}

# 显示服务日志
show_logs() {
    log_info "显示服务日志 (最近 50 行)..."
    docker-compose -f "$COMPOSE_FILE" logs --tail=50 -f &
    LOG_PID=$!
    
    # 10秒后停止显示日志
    sleep 10
    kill $LOG_PID 2>/dev/null || true
}

# 清理旧镜像和容器
cleanup() {
    log_info "清理旧镜像和容器..."
    
    # 删除停止的容器
    docker container prune -f
    
    # 删除未使用的镜像
    docker image prune -f
    
    # 删除未使用的卷（可选）
    # docker volume prune -f
    
    log_success "清理完成"
}

# 健康检查
health_check() {
    log_info "执行健康检查..."
    
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -sf http://localhost:7002/actuator/health > /dev/null 2>&1; then
            log_success "应用健康检查通过"
            return 0
        fi
        
        log_info "等待应用启动... ($attempt/$max_attempts)"
        sleep 5
        attempt=$((attempt + 1))
    done
    
    log_error "健康检查失败，应用可能未正常启动"
    return 1
}

# 备份数据
backup_data() {
    if [ "$ENVIRONMENT" = "prod" ]; then
        log_info "备份生产环境数据..."
        
        local backup_dir="backups/$(date +%Y%m%d_%H%M%S)"
        mkdir -p "$backup_dir"
        
        # 备份 MySQL 数据
        if docker-compose -f "$COMPOSE_FILE" ps | grep -q mysql; then
            log_info "备份 MySQL 数据..."
            docker-compose -f "$COMPOSE_FILE" exec -T mysql mysqldump -u root -p"${MYSQL_ROOT_PASSWORD}" chongwufuwu > "$backup_dir/mysql_backup.sql"
            log_success "MySQL 数据备份完成: $backup_dir/mysql_backup.sql"
        fi
        
        # 备份 Redis 数据
        if docker-compose -f "$COMPOSE_FILE" ps | grep -q redis; then
            log_info "备份 Redis 数据..."
            docker-compose -f "$COMPOSE_FILE" exec -T redis redis-cli BGSAVE
            log_success "Redis 数据备份完成"
        fi
    fi
}

# 主函数
main() {
    # 解析命令行参数
    while [[ $# -gt 0 ]]; do
        case $1 in
            -e|--environment)
                ENVIRONMENT="$2"
                shift 2
                ;;
            -t|--tag)
                TAG="$2"
                shift 2
                ;;
            -r|--registry)
                REGISTRY="$2"
                shift 2
                ;;
            -h|--help)
                show_help
                exit 0
                ;;
            *)
                log_error "未知参数: $1"
                show_help
                exit 1
                ;;
        esac
    done
    
    # 显示部署信息
    echo "========================================="
    echo "      项目部署脚本"
    echo "========================================="
    echo "环境: $ENVIRONMENT"
    echo "时间: $(date '+%Y-%m-%d %H:%M:%S')"
    echo "========================================="
    
    # 执行部署流程
    check_docker
    load_env
    setup_environment
    
    # 生产环境先备份
    if [ "$ENVIRONMENT" = "prod" ]; then
        backup_data
    fi
    
    login_registry
    pull_images
    deploy
    health_check
    
    # 可选：清理旧资源
    # cleanup
    
    echo ""
    echo "========================================="
    log_success "部署完成！"
    echo "========================================="
    echo "环境: $ENVIRONMENT"
    echo "访问地址: http://localhost:7002"
    echo "健康检查: http://localhost:7002/actuator/health"
    echo "========================================="
}

# 执行主函数
main "$@"

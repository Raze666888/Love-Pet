# CI/CD Docker 部署指南

## 📋 概述

本项目已实现完整的 CI/CD 流程，支持自动构建 Docker 镜像并部署到不同环境。

## 🏗️ 架构概览

```
GitHub Actions CI/CD Pipeline
├── CI Pipeline (ci.yml)
│   ├── 单元测试
│   ├── 集成测试
│   └── 代码覆盖率报告
│
├── CD Pipeline (cd-docker.yml)
│   ├── 构建 Docker 镜像
│   ├── 推送到 GitHub Container Registry
│   ├── 部署到 Staging
│   └── 部署到 Production
│
└── Docker 部署
    ├── docker-compose.yml (开发环境)
    ├── docker-compose.dev.yml (测试环境)
    └── docker-compose.prod.yml (生产环境)
```

## 🚀 快速开始

### 1. 本地开发环境

```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f app

# 停止服务
docker-compose down
```

### 2. 使用部署脚本

```bash
# 给脚本添加执行权限
chmod +x deploy.sh

# 部署开发环境
./deploy.sh -e dev

# 部署测试环境
./deploy.sh -e staging -t develop

# 部署生产环境
./deploy.sh -e prod -t v1.0.0
```

## 🔧 配置说明

### GitHub Actions 配置

在 GitHub 仓库设置中添加以下 Secrets：

| Secret 名称 | 说明 | 必需 |
|------------|------|------|
| `GITHUB_TOKEN` | 自动提供，用于推送镜像到 GHCR | ✅ |
| `SONAR_HOST_URL` | SonarQube 服务器地址 | ❌ |
| `SONAR_LOGIN` | SonarQube 登录令牌 | ❌ |

### 环境变量配置

1. 复制示例文件：
```bash
cp .env.example .env
```

2. 编辑 `.env` 文件，填入实际配置值

3. 对于生产环境，创建 `.env.prod`：
```bash
cp .env.example .env.prod
```

## 📦 Docker 镜像

### 镜像标签策略

| 触发条件 | 生成的标签 |
|---------|-----------|
| Push to main | `main`, `latest` |
| Push to develop | `develop` |
| Tag v1.0.0 | `1.0.0`, `1.0`, `latest` |
| Pull Request | `pr-{number}` |

### 镜像地址

```
ghcr.io/{username}/{repository}:{tag}
```

示例：
```
ghcr.io/yourname/myjavaproject:latest
ghcr.io/yourname/myjavaproject:v1.0.0
```

## 🌐 部署环境

### 开发环境 (Development)

- **触发条件**: Push to `develop` 分支
- **配置文件**: `docker-compose.yml`
- **特点**: 包含所有服务，便于本地开发调试

### 测试环境 (Staging)

- **触发条件**: Push to `develop` 分支 或 手动触发
- **配置文件**: `docker-compose.dev.yml`
- **特点**: 接近生产环境配置，用于测试

### 生产环境 (Production)

- **触发条件**: Push to `main` 分支 或 Tag 推送
- **配置文件**: `docker-compose.prod.yml`
- **特点**: 
  - 资源限制
  - 健康检查
  - 日志轮转
  - Nginx 反向代理

## 🔒 安全配置

### Docker 安全最佳实践

1. **非 root 用户运行**: 应用使用 `appuser` (UID 1000) 运行
2. **最小化镜像**: 使用 Alpine Linux 基础镜像
3. **多阶段构建**: 构建和运行分离，减少攻击面
4. **健康检查**: 自动检测服务状态

### 网络隔离

```yaml
networks:
  app-network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.20.0.0/16
```

## 📊 监控和日志

### 健康检查端点

```
http://localhost:7002/actuator/health
```

### 查看日志

```bash
# 查看所有服务日志
docker-compose logs

# 查看特定服务日志
docker-compose logs app

# 实时跟踪日志
docker-compose logs -f app

# 查看最近 100 行
docker-compose logs --tail=100 app
```

### 日志文件位置

- 应用日志: `./logs/` (挂载到容器 `/app/logs`)
- Nginx 日志: `./logs/nginx/`

## 🔄 更新部署

### 自动更新 (GitHub Actions)

1. 推送代码到对应分支
2. GitHub Actions 自动构建镜像
3. 自动部署到对应环境

### 手动更新

```bash
# 拉取最新镜像
docker-compose pull

# 重启服务
docker-compose up -d

# 或者使用部署脚本
./deploy.sh -e prod
```

## 🛠️ 故障排查

### 常见问题

#### 1. 服务无法启动

```bash
# 检查服务状态
docker-compose ps

# 查看详细日志
docker-compose logs app

# 检查端口占用
netstat -tlnp | grep 7002
```

#### 2. 数据库连接失败

```bash
# 检查 MySQL 状态
docker-compose exec mysql mysqladmin ping

# 查看 MySQL 日志
docker-compose logs mysql
```

#### 3. 镜像拉取失败

```bash
# 登录 GitHub Container Registry
docker login ghcr.io -u USERNAME

# 检查镜像权限
# 确保 GitHub Token 有 packages:read 权限
```

### 清理命令

```bash
# 停止所有服务
docker-compose down

# 删除所有容器和卷（谨慎使用）
docker-compose down -v

# 清理未使用的镜像
docker image prune -f

# 清理所有未使用的资源
docker system prune -f
```

## 📝 版本发布流程

1. **开发阶段**
   ```bash
   git checkout develop
   git add .
   git commit -m "feat: new feature"
   git push origin develop
   ```
   → 自动部署到 Staging

2. **发布阶段**
   ```bash
   git checkout main
   git merge develop
   git tag v1.0.0
   git push origin main --tags
   ```
   → 自动部署到 Production

3. **验证部署**
   - 检查 GitHub Actions 运行状态
   - 访问健康检查端点
   - 验证应用功能

## 🔗 相关链接

- [GitHub Actions 文档](https://docs.github.com/en/actions)
- [Docker 文档](https://docs.docker.com/)
- [GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)

## 📞 支持

如有问题，请：
1. 查看 GitHub Actions 日志
2. 检查 Docker 容器日志
3. 提交 Issue 到项目仓库

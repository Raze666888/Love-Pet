# 安全配置说明

## 环境变量配置

为了保护敏感信息，本项目使用环境变量来配置阿里云OSS等服务的密钥信息。

### 配置步骤

1. 复制 `.env.example` 文件为 `.env`
2. 在 `.env` 文件中填入真实的配置信息
3. 确保 `.env` 文件不被提交到版本控制系统

### 环境变量说明

| 变量名 | 说明 | 示例值 |
|--------|------|--------|
| OSS_ENABLED | 是否启用OSS | true |
| OSS_ENDPOINT | OSS端点 | https://oss-cn-shenzhen.aliyuncs.com |
| OSS_ACCESS_KEY_ID | 阿里云AccessKey ID | 请填入真实值 |
| OSS_ACCESS_KEY_SECRET | 阿里云AccessKey Secret | 请填入真实值 |
| OSS_BUCKET_NAME | OSS存储桶名称 | 请填入真实值 |
| OSS_URL_PREFIX | OSS访问前缀 | https://your-bucket.oss-cn-shenzhen.aliyuncs.com/ |

### 安全注意事项

1. **永远不要**将真实的AccessKey信息提交到代码仓库
2. 定期轮换AccessKey
3. 使用最小权限原则配置AccessKey权限
4. 在生产环境中使用更安全的密钥管理方案

### 本地开发配置

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑环境变量文件
# 在 .env 文件中填入真实的配置信息
```

### 生产环境配置

在生产环境中，建议通过系统环境变量或容器编排工具来设置这些配置项，而不是使用 `.env` 文件。

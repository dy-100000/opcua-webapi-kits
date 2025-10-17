# 远程仓库设置指南

## 当前状态
- ✅ 已移除原有的远程仓库配置
- ✅ 所有代码更改已提交到本地仓库
- ✅ 项目已优化，移除了未使用的依赖项

## 设置新的远程仓库

### 方法1: 使用提供的脚本
```bash
# 运行设置脚本
./setup-remote.sh https://github.com/yourusername/your-repo.git

# 推送到新仓库
git push -u origin main
```

### 方法2: 手动设置
```bash
# 添加新的远程仓库
git remote add origin https://github.com/yourusername/your-repo.git

# 验证配置
git remote -v

# 推送到新仓库
git push -u origin main
```

### 方法3: 如果仓库已存在，先拉取再推送
```bash
# 添加远程仓库
git remote add origin https://github.com/yourusername/your-repo.git

# 拉取远程更改
git pull origin main --allow-unrelated-histories

# 推送更改
git push -u origin main
```

## 注意事项
- 确保新仓库已创建
- 如果遇到网络问题，可以尝试使用SSH而不是HTTPS
- 如果仓库已存在内容，可能需要使用 `--allow-unrelated-histories` 参数

## 项目信息
- 项目名称: OPC UA Web Browser Tool
- 技术栈: Vue 3 + Vite + TypeScript + Element Plus
- 主要功能: OPC UA客户端，支持节点浏览、数据读写等操作

#!/bin/bash

# 设置新的远程仓库脚本
# 使用方法: ./setup-remote.sh <新的仓库URL>

if [ $# -eq 0 ]; then
    echo "使用方法: ./setup-remote.sh <新的仓库URL>"
    echo "例如: ./setup-remote.sh https://github.com/username/repository.git"
    exit 1
fi

NEW_REPO_URL=$1

echo "正在添加新的远程仓库: $NEW_REPO_URL"
git remote add origin $NEW_REPO_URL

echo "验证远程仓库配置:"
git remote -v

echo ""
echo "现在您可以推送代码到新仓库:"
echo "git push -u origin main"

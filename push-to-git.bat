@echo off
echo ========================================
echo           Git 推送脚本
echo ========================================
echo.

echo 当前Git状态:
git status --short
echo.

echo 最近的提交:
git log --oneline -3
echo.

echo ========================================
echo 选择推送目标:
echo   1. GitHub (origin)
echo   2. Gitee (gitee) 
echo   3. 两个都推送
echo   4. 仅显示状态，不推送
echo ========================================
echo.

set /p choice="请选择 (1-4): "

if "%choice%"=="1" (
    echo.
    echo 正在推送到GitHub...
    git push origin main
    if %errorlevel% eq 0 (
        echo ✅ 成功推送到GitHub!
    ) else (
        echo ❌ 推送到GitHub失败
        echo 可能的原因:
        echo   - 网络连接问题
        echo   - 需要身份验证
        echo   - 权限不足
    )
) else if "%choice%"=="2" (
    echo.
    echo 正在推送到Gitee...
    git push gitee main
    if %errorlevel% eq 0 (
        echo ✅ 成功推送到Gitee!
    ) else (
        echo ❌ 推送到Gitee失败
        echo 可能的原因:
        echo   - SSH密钥未配置
        echo   - 网络连接问题
        echo   - 权限不足
    )
) else if "%choice%"=="3" (
    echo.
    echo 正在推送到GitHub...
    git push origin main
    if %errorlevel% eq 0 (
        echo ✅ 成功推送到GitHub!
    ) else (
        echo ❌ 推送到GitHub失败
    )
    
    echo.
    echo 正在推送到Gitee...
    git push gitee main
    if %errorlevel% eq 0 (
        echo ✅ 成功推送到Gitee!
    ) else (
        echo ❌ 推送到Gitee失败
    )
) else if "%choice%"=="4" (
    echo.
    echo 📊 详细Git状态:
    echo.
    echo --- 分支信息 ---
    git branch -v
    echo.
    echo --- 远程仓库 ---
    git remote -v
    echo.
    echo --- 未推送的提交 ---
    git log origin/main..HEAD --oneline
    echo.
) else (
    echo 无效选择，退出。
)

echo.
echo ========================================
echo 💡 提示:
echo   - 如果推送失败，请检查网络连接和权限
echo   - GitHub可能需要Personal Access Token
echo   - Gitee可能需要SSH密钥配置
echo   - 代码已成功提交到本地仓库
echo ========================================
echo.

echo 按任意键退出...
pause >nul

@echo off
CHCP 65001 > nul
echo ===================================================
echo 🚀 MONOREPO AUTOMATION: GIT PUSH + LOCAL DOCKER    🚀
echo ===================================================

echo [1/4] 📂 Entering backend directory and building JAR...
cd backend
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ❌ MAVEN ERROR: Build failed! Aborting push and docker restart.
    cd ..
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo [2/4] 🛑 Restarting local Docker containers...
docker compose down
docker compose up --build -d

echo.
echo [3/4] 📂 Returning to root directory and pushing to GitHub...
cd ..
git add .
git commit -m "Automated deployment: updated codebase and security resources"
git push origin main
rem ^^^ Change 'main' to 'master' if your repository uses the legacy branch name

echo.
echo ===================================================
echo ✅ SUCCESS: ALL SYSTEMS UPDATED!
echo 💻 Local environment: http://localhost:8080 
echo ☁️ Cloud deployment: Render build triggered (allow 5-8 mins).
echo ===================================================
pause
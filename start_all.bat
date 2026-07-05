@echo off
REM AI Scam Shield - Start all services

title AI Scam Shield - All Services
echo.
echo ========================================
echo   AI Scam Shield - Starting Services
echo ========================================
echo.

REM Check if required tools exist
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21 or higher
    pause
    exit /b 1
)

where node >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Node.js is not installed or not in PATH
    echo Please install Node.js 18 or higher
    pause
    exit /b 1
)

echo [OK] Java and Node.js are installed
echo.

REM Start Backend in a new window
echo Starting Backend (Spring Boot)...
start "AI Scam Shield - Backend" cmd /k "cd backend && mvnw.cmd spring-boot:run"

REM Wait a moment for backend to start
timeout /t 5 /nobreak

REM Start Frontend in a new window
echo Starting Frontend (React + Vite)...
start "AI Scam Shield - Frontend" cmd /k "cd frontend && npm install && npm run dev"

echo.
echo ========================================
echo   Services Starting...
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:5173
echo Health:   http://localhost:8080/api/health
echo.
echo Press Ctrl+C in each window to stop the services
echo.
pause

@echo off
echo ============================================
echo   AI Scam Shield Backend - Starting...
echo ============================================
echo.
echo NOTE: Set these env vars for full functionality:
echo   set OPENAI_API_KEY=sk-...
echo   set SAFE_BROWSING_API_KEY=AIza...
echo   set JWT_SECRET=your-32-char-secret (optional - has dev default)
echo.

set MVN=C:\Users\ASUS\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6\bin\mvn.cmd

cd /d "%~dp0"
"%MVN%" spring-boot:run

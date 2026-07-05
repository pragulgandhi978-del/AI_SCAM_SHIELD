@echo off
setlocal

set BASE=http://localhost:8080/api
set CURL=curl -s

echo ================================================================
echo  STEP 1: Register user
echo ================================================================
%CURL% -X POST %BASE%/register ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"TestUser\",\"email\":\"verify@test.com\",\"password\":\"pass1234\"}" ^
  -o step1.json
type step1.json
echo.

rem Extract token from JSON (simple grep approach)
for /f "tokens=2 delims=:," %%a in ('findstr "token" step1.json') do (
    set RAW=%%a
    goto :gottoken
)
:gottoken
set TOKEN=%RAW:"=%
set TOKEN=%TOKEN: =%
echo TOKEN=%TOKEN%
echo.

echo ================================================================
echo  STEP 2: report-scam (expects 201)
echo ================================================================
%CURL% -o step2.json -w "HTTP %{http_code}" ^
  -X POST %BASE%/report-scam ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -d "{\"contentText\":\"You won a prize click now\",\"category\":\"Lottery Scam\"}"
echo.
type step2.json
echo.

echo ================================================================
echo  STEP 3: history (expects 200 with content array)
echo ================================================================
%CURL% -o step3.json -w "HTTP %{http_code}" ^
  -H "Authorization: Bearer %TOKEN%" ^
  "%BASE%/history?page=0&size=10"
echo.
type step3.json
echo.

echo ================================================================
echo  STEP 4: dashboard (expects 200 with counts)
echo ================================================================
%CURL% -o step4.json -w "HTTP %{http_code}" ^
  -H "Authorization: Bearer %TOKEN%" ^
  %BASE%/dashboard
echo.
type step4.json
echo.

echo ================================================================
echo  STEP 5: admin/users with USER token (expects 403)
echo ================================================================
%CURL% -o step5.json -w "HTTP %{http_code}" ^
  -H "Authorization: Bearer %TOKEN%" ^
  %BASE%/admin/users
echo.
type step5.json
echo.

echo ================================================================
echo  STEP 6: admin login (expects 200 with admin token)
echo ================================================================
%CURL% -o step6.json -w "HTTP %{http_code}" ^
  -X POST %BASE%/admin/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"admin1234\"}"
echo.
type step6.json
echo.

rem Extract admin token
for /f "tokens=2 delims=:," %%a in ('findstr "token" step6.json') do (
    set RAWADMIN=%%a
    goto :gotadmintoken
)
:gotadmintoken
set ADMIN_TOKEN=%RAWADMIN:"=%
set ADMIN_TOKEN=%ADMIN_TOKEN: =%
echo ADMIN_TOKEN=%ADMIN_TOKEN%
echo.

echo ================================================================
echo  STEP 7: admin/users with ADMIN token (expects 200 with users)
echo ================================================================
%CURL% -o step7.json -w "HTTP %{http_code}" ^
  -H "Authorization: Bearer %ADMIN_TOKEN%" ^
  %BASE%/admin/users
echo.
type step7.json
echo.

echo ================================================================
echo  ALL TESTS DONE
echo ================================================================
endlocal

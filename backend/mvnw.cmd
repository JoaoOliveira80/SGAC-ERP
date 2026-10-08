@echo off
setlocal
set "MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\sgac-maven-3.9.11\apache-maven-3.9.11"
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
  powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0.mvn\wrapper\install-maven.ps1"
  if errorlevel 1 exit /b 1
)
call "%MAVEN_HOME%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%

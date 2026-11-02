@echo off
setlocal
cd /d "%~dp0"

echo ================================================
echo RoadsAndRoofsTFC - DeepSpace GitHub sync
echo ================================================

where git >nul 2>&1
if errorlevel 1 (
  echo ERROR: Git was not found in PATH.
  exit /b 1
)

git switch main
if errorlevel 1 exit /b 1

git add -A
if errorlevel 1 exit /b 1

git diff --cached --quiet
if errorlevel 1 (
  git commit -m "Sync Sky of Grind DeepSpace source changes"
  if errorlevel 1 exit /b 1
) else (
  echo No local changes to commit.
)

git push -u deepspace main
if errorlevel 1 (
  echo ERROR: Push failed. Local changes remain committed and can be pushed again.
  exit /b 1
)

echo.
echo Successfully synced to https://github.com/evilcatshb/roadsandroofstfc-deepspace-fork
exit /b 0

@echo off
setlocal

:: Set environment variables
set CAS_URL=http://localhost
set CAS_TGT_HTTPS=false
set CPI_URL=http://localhost
set CAS_SERVICE_ID=../portal_new/cas/cpi
set CPI_USERNAME=whoisit
set CPI_PASSWORD=whoisit567

:: Check if an argument was provided
if "%1"=="" (
    echo Usage: %0 [install|order]
    exit /b 1
)

:: Validate the argument
if not "%1"=="install" if not "%1"=="order" (
    echo Invalid option: %1
    echo Usage: %0 [install|order]
    exit /b 1
)

:: Run the executable with the provided option and append output
irsync-windows-x64-1.2.5.exe %1 >> irsync.log 2>&1
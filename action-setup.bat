@echo off
rem Tạo thư mục .github và workflows
mkdir .github
mkdir .github\workflows
rem Tải build-debug.yml
powershell -Command "Invoke-WebRequest -Uri https://app-service.hihoay.io/files/build-debug.yml' -OutFile '.github\workflows\build-debug.yml'"
rem Tải build-release.yml
powershell -Command "Invoke-WebRequest -Uri https://app-service.hihoay.io/files/build-release.yml' -OutFile '.github\workflows\build-release.yml'"
rem Tải .env
powershell -Command "Invoke-WebRequest -Uri https://app-service.hihoay.io/files/.env' -OutFile '.github\.env'"
echo Đã tạo thư mục và tải các tệp YAML thành công.
pause

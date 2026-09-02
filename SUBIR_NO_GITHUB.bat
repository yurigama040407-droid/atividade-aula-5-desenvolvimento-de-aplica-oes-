@echo off
echo ========================================================
echo  PUBLICAR PROJETO NO SEU GITHUB
echo ========================================================
echo.
set /p REPO_URL= gh repo clone yurigama040407-droid/atividade-aula-5-desenvolvimento-de-aplica-oes-
if "%REPO_URL%"=="" (
    echo Nenhum link fornecido. Operacao cancelada.
    pause
    exit /b
)

git remote remove origin 2>nul
git remote add origin %REPO_URL%
git branch -M main
git push -u origin main

echo.
echo ========================================================
echo  PROJETO ENVIADO COM SUCESSO PARA O GITHUB!
echo ========================================================
pause

@echo off
echo ========================================================
echo  PUBLICAR PROJETO NO GITHUB
echo ========================================================
echo.
git add -A
git commit -m "Atualiza projeto com tela da Media do ENADE e estrutura limpa" 2>nul
git push -u origin main
echo.
echo ========================================================
echo  PROCESSO FINALIZADO!
echo ========================================================
pause

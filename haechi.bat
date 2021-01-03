@echo off
cd "%~dp0"
solc-windows --ast-compact-json %1 > %1.ast | clip
java -jar dist\haechi.jar %1
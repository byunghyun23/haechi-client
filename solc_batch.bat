@echo off
cd "%~dp0"
solc-windows --ast-compact-json %1 > %1.ast 2> %1.error
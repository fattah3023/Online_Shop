@echo off
for %%i in (app-*.jar) do(
java -jar "%%i">out.log
)
pause



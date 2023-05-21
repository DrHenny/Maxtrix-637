@echo off
title runserver
java -Xmx815m -cp bin;lib/*; com.rs.Launcher true true false
pause
@echo off
"C:\Program Files\Java\jdk1.7.0_04\bin\javac.exe" -d bin -cp lib/*; -sourcepath src src/com/rs/game/player/Player.java
echo compiled Player folder
"C:\Program Files\Java\jdk1.7.0_04\bin\javac.exe" -d bin -cp lib/*; -sourcepath src src/com/rs/Launcher.java
echo compiled Server folder
"C:\Program Files\Java\jdk1.7.0_04\bin\javac.exe" -d bin -cp lib/*; -sourcepath src src/com/rs/game/player/dialogues/*.java
echo compiling Dialogues
"C:\Program Files\Java\jdk1.7.0_04\bin\javac.exe" -d bin -cp lib/*; -sourcepath src src/com/rs/*.java
echo compiled Main Server Folder
"C:\Program Files\Java\jdk1.7.0_04\bin\javac.exe" -d bin -cp lib/*; -sourcepath src src/com/rs/game/player/content/*.java
echo compiled Main Server Folder
"C:\Program Files\Java\jdk1.7.0_04\bin\javac.exe" -d bin -cp lib/*; -sourcepath src src/com/rs/net/decoders/handlers/*.java
echo Compiled all Successfully
pause
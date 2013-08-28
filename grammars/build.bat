@echo on
SET grammar=BigDataScript.g4
SET init=main
SET antlr4=java -Xmx1g -cp c:\wk\jars\antlr-4.1-complete.jar org.antlr.v4.Tool
SET grun=java -Xmx1g -cp c:\wk\jars\antlr-4.1-complete.jar org.antlr.v4.runtime.misc.TestRig

rem Delete old files
del *.class *.java 

rem Compile
echo Create Lexer and Parser
%antlr4% -visitor -package ca.mcgill.mcb.pcingola.bigDataScript.antlr %grammar%

rem Copy to source dir
copy *.java ..\src\ca\mcgill\mcb\pcingola\bigDataScript\antlr\


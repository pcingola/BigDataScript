@echo off
SET base=ca.mcgill.mcb.pcingola.bigDataScript.antlr.BigDataScript
SET grammar=%base%.g4
SET init=programUnit
SET antlr4=java -Xmx1g -cp c:\wk\jars\antlr-4.1-complete.jar; org.antlr.v4.Tool
SET grun=java -Xmx1g -cp c:\wk\jars\antlr-4.1-complete.jar;..\bin org.antlr.v4.runtime.misc.TestRig
SET testFile=test45.bds

echo This assumes the gramar is already compiled

echo Compile
rem javac -cp c:\wk\jars\antlr-4.1-complete.jar *.java

echo Tokens
%grun% %base% %init% -tokens %testFile%

echo Tree
%grun% %base% %init% -tree %testFile%

echo Trace
%grun% %base% %init% -trace %testFile%

echo Diagnostics
%grun% %base% %init% -diagnostics %testFile%

echo gui
%grun% %base% %init% -gui %testFile%

del *.class
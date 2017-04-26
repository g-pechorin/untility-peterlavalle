@ECHO OFF

SET MCS="C:/Program Files (x86)/Unity/5.1.4f1/Editor/Data/MonoBleedingEdge/bin/mcs.bat"

REM %MCS% -r:"C:/Program Files (x86)/Unity/5.1.4f1/Editor/Data/Managed/UnityEngine.dll" -target:library "../puntility.unity/Assets/puntility/*.cs" -out:puntility.Plugin.dll

%MCS% -r:"C:/Program Files (x86)/Unity/5.1.4f1/Editor/Data/Managed/UnityEngine.dll" ^
	-r:"C:/Program Files (x86)/Unity/5.1.4f1/Editor/Data/Managed/UnityEditor.dll" ^
	-target:library -d:UNITY_EDITOR -out:puntility.Editor.dll ^
	"../puntility.unity/Assets/puntility/*.cs"


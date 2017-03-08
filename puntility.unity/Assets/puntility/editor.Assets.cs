#if UNITY_EDITOR
/// Peter LaValle / nottingham.ac.uk / gmail.com

/// Changes;
///   2016-11-05;
///     - first version to try and "condense" my existing shenanigans
///     - perfected the creation menu
///
using UnityEngine;
using UnityEditor;
using System.IO;

public static class Assets
{
	public static T CreateScriptableObjectFile<T>() where T : ScriptableObject
	{
		var asset = ScriptableObject.CreateInstance<T>();

		// "yoyo" solution from https://forum.unity3d.com/threads/how-to-get-currently-selected-folder-for-putting-new-asset-into.81359/
		string assetFolder = "Assets";
		foreach (UnityEngine.Object obj in Selection.GetFiltered(typeof(UnityEngine.Object), SelectionMode.Assets))
		{
			assetFolder = AssetDatabase.GetAssetPath(obj);
			if (File.Exists(assetFolder))
			{
				assetFolder = Path.GetDirectoryName(assetFolder);
			}
			break;
		}

		// "TokiZR" from http://answers.unity3d.com/questions/480226/select-asset-for-rename.html
		ProjectWindowUtil.CreateAsset(asset, assetFolder + "/" + (typeof(T).Name) + ".asset");

		return asset;
	}
}
#endif

/// Peter LaValle
/// PropertyDrawer for InputProperty
using System;
using UnityEngine;

using System.IO;
using UnityEditor;

namespace peterlavalle
{
	[CustomPropertyDrawer(typeof(InputProperty))]
	public class InputPropertyDrawer : PropertyDrawer
	{
		/// <summary>
		/// changes serializationMode to text
		/// </summary>
		static InputPropertyDrawer()
		{
			EditorSettings.serializationMode = SerializationMode.ForceText;
		}
		
		const string marker = "m_Name: ";

		/// <summary>
		/// Overrides the default GUI to draw a Popup that selects an input axis
		/// </summary>
		/// <param name="position"></param>
		/// <param name="property"></param>
		/// <param name="label"></param>
		public override void OnGUI(Rect position, SerializedProperty property, GUIContent label)
		{
			// re-warn the user that we need force-text
			if (SerializationMode.ForceText != EditorSettings.serializationMode)
			{
				Debug.Log("Forcing `EditorSettings.serializationMode = SerializationMode.ForceText` ... since that's the only way that this'll work");
				EditorSettings.serializationMode = SerializationMode.ForceText;
			}

			// Using BeginProperty / EndProperty on the parent property means that
			// prefab override logic works on the entire property.
			EditorGUI.BeginProperty(position, label, property);

			var inputName = property.FindPropertyRelative("inputName");
			var oldIndex = ArrayUtility.IndexOf(InputNames, inputName.stringValue);

			var newIndex = EditorGUI.Popup(position, label.text, Math.Max(0, oldIndex), InputNames);

			if (newIndex != oldIndex)
			{
				inputName.stringValue = InputNames[newIndex];
			}

			EditorGUI.EndProperty();
		}

		private string[] _InputNames_inputNames = null;

		/// <summary>
		/// The names from "/ProjectSettings/InputManager.asset"
		/// </summary>
		private string[] InputNames
		{
			get
			{
				Debug.Assert(SerializationMode.ForceText == EditorSettings.serializationMode);

				if (null != _InputNames_inputNames)
					return _InputNames_inputNames;

				var pathToFile = Application.dataPath;
				pathToFile = pathToFile.Substring(0, pathToFile.LastIndexOf('/'));
				pathToFile += "/ProjectSettings/InputManager.asset";

				return _InputNames_inputNames = File.ReadAllLines(pathToFile)
												.Filter(line => -1 != line.IndexOf(marker))
												.Mapping(line => line.Substring(marker.Length + line.IndexOf(marker)))
												.Distinct()
												.ToArray();
			}
		}
	}
}

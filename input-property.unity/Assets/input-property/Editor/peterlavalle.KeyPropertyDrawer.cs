
using System;
using UnityEngine;
using UnityEditor;

namespace peterlavalle
{
	[CustomPropertyDrawer(typeof(KeyProperty))]
	public class KeyPropertyDrawer : PropertyDrawer
	{
		/// <summary>
		/// changes serializationMode to text
		/// </summary>
		static KeyPropertyDrawer()
		{
			EditorSettings.serializationMode = SerializationMode.ForceText;
		}


		/// <summary>
		/// Overrides the default GUI to draw a Popup that selects an input axis
		/// </summary>
		/// <param name="position"></param>
		/// <param name="property"></param>
		/// <param name="label"></param>
		public override void OnGUI(Rect position, SerializedProperty property, GUIContent label)
		{
			// re-warn the user that
			if (SerializationMode.ForceText != EditorSettings.serializationMode)
			{
				Debug.Log("Forcing `EditorSettings.serializationMode = SerializationMode.ForceText` ... since that's the only way that this'll work");
				EditorSettings.serializationMode = SerializationMode.ForceText;
			}

			// Using BeginProperty / EndProperty on the parent property means that
			// prefab override logic works on the entire property.
			EditorGUI.BeginProperty(position, label, property);

			var inputName = property.FindPropertyRelative("keyName");

			var keyNames = ((KeyCode[])Enum.GetValues(typeof(KeyCode))).Mapping(code => code.ToString()).ToArray();

			var oldIndex = inputName.enumValueIndex;

			var newIndex = EditorGUI.Popup(position, label.text, Math.Max(0, oldIndex), keyNames);

			if (newIndex != oldIndex)
			{
				inputName.enumValueIndex = newIndex;
			}

			EditorGUI.EndProperty();
		}
	}
}

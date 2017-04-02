using System;

using UnityEngine;



#if UNITY_EDITOR
using System.Collections.Generic;
using System.IO;
using UnityEditor;

#endif


namespace peterlavalle

{

/// <summary>

/// Add instances of this as a member in MonoBehaviours.

/// Read the properties to read input

/// </summary>

[Serializable]

public struct KeyProperty

{

#if UNITY_EDITOR

	/// <summary>

	/// changes serializationMode to text

	/// </summary>

	static KeyProperty()

	{

		EditorSettings.serializationMode = SerializationMode.ForceText;

	}

#endif







#pragma warning disable 0649 // we're going to edit this with the inspector so let's disable the warning



	[SerializeField]

	private KeyCode keyName;

#pragma warning restore 0649



	public bool ButtonHeld {
		get {
			return Input.GetKey(keyName);
		}
	}

	public bool ButtonDown {
		get {
			return Input.GetKeyDown(keyName);
		}
	}

	public bool ButtonGone {
		get {
			return Input.GetKeyUp(keyName);
		}
	}



#if UNITY_EDITOR
}



[CustomPropertyDrawer(typeof(KeyProperty))]

public class KeyPropertyDrawer : PropertyDrawer

{





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



#endif

}

}
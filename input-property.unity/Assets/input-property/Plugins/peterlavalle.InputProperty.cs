/// Peter LaValle

/// Magical object - use it to get inputs wiothout the fiddley string literals

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

public struct InputProperty

{

#if UNITY_EDITOR

	/// <summary>

	/// changes serializationMode to text

	/// </summary>

	static InputProperty()

	{

		EditorSettings.serializationMode = SerializationMode.ForceText;

	}

#endif



#pragma warning disable 0649 // we're going to edit this with the inspector so let's disable the warning



	[SerializeField]

	private string inputName;

#pragma warning restore 0649



	public float Axis {
		get {
			return Input.GetAxis(inputName);
		}
	}

	public float AxisRaw {
		get {
			return Input.GetAxisRaw(inputName);
		}
	}

	public bool ButtonHeld {
		get {
			return Input.GetButton(inputName);
		}
	}

	public bool ButtonDown {
		get {
			return Input.GetButtonDown(inputName);
		}
	}

	public bool ButtonGone {
		get {
			return Input.GetButtonUp(inputName);
		}
	}



#if UNITY_EDITOR

}



[CustomPropertyDrawer(typeof(InputProperty))]

public class InputPropertyDrawer : PropertyDrawer

{

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

#endif

}

}

/// Peter LaValle
/// Magical object - use it to get inputs wiothout the fiddley string literals
using System;
using UnityEngine;

#if UNITY_EDITOR
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
#pragma warning disable 0649 // we're going to edit this with the inspector so let's disable the warning
		[SerializeField]
		private string inputName;
#pragma warning restore 0649

		public float Axis
		{
			get
			{
				return Input.GetAxis(inputName);
			}
		}
		public float AxisRaw
		{
			get
			{
				return Input.GetAxisRaw(inputName);
			}
		}
		public bool ButtonHeld
		{
			get
			{
				return Input.GetButton(inputName);
			}
		}
		public bool ButtonDown
		{
			get
			{
				return Input.GetButtonDown(inputName);
			}
		}
		public bool ButtonGone
		{
			get
			{
				return Input.GetButtonUp(inputName);
			}
		}
	}
}
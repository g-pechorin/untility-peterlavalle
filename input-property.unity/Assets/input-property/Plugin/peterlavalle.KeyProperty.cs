
using System;
using UnityEngine;

namespace peterlavalle
{
	/// <summary>
	/// Add instances of this as a member in MonoBehaviours.
	/// Read the properties to read input
	/// </summary>
	[Serializable]
	public struct KeyProperty
	{
#pragma warning disable 0649 // we're going to edit this with the inspector so let's disable the warning
		[SerializeField]
		private KeyCode keyName;
#pragma warning restore 0649

		public bool ButtonHeld
		{
			get
			{
				return Input.GetKey(keyName);
			}
		}
		public bool ButtonDown
		{
			get
			{
				return Input.GetKeyDown(keyName);
			}
		}
		public bool ButtonGone
		{
			get
			{
				return Input.GetKeyUp(keyName);
			}
		}
	}
}

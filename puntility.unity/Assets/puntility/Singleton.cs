using UnityEngine;
using System.Collections;




/// <summary>
///magic singleton method
/// </summary>
/// 2016-10-14 ;
///		- merged together ; mist whatnots
/// 2016-09-27 ;
///		- created from previous things
public static class Singleton
{
	public abstract class ABehaviour : MonoBehaviour
	{
	}

	static public T of<T>() where T : Behaviour
	{
#if UNITY_EDITOR
		if (!Application.isPlaying)
			return null;
#endif

		var found = Object.FindObjectsOfType<T>();

		switch (found.Length)
		{
			case 0:
				if (typeof(ABehaviour).IsAssignableFrom(typeof(T)))
					return new GameObject(":Singleton.of<" + typeof(T).Name + ">").AddComponent<T>();
				else
					return null;

			case 1:
				return found[0];

			default:
				throw new UnityException();
		}
	}
}

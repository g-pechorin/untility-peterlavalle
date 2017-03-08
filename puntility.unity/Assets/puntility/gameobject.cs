using UnityEngine;
using System.Collections;

/// <summary>
/// </summary>
///
/// 2016-10-14 ;
///		- merged together
/// 2016-09-25 ;
///		- condensed the component variant into here
///		
/// 2016-09-21 ;
///		- added this comment block
public static class gameobject
{
	public static T OptComponent<T>(this Component component) where T : Component
	{
		return component.gameObject.OptComponent<T>();
	}

	public static T OptComponent<T>(this GameObject gameObject) where T : Component
	{
		var component = gameObject.GetComponent<T>();

		if (null != component)
			return component;

		return gameObject.AddComponent<T>();
	}

	public static T TopComponent<T>(this Component component) where T : Component
	{
		return null != component ? component.gameObject.TopComponent<T>() : null;
	}

	public static T TopComponent<T>(this GameObject gameObject) where T : Component
	{
		var components = gameObject.GetComponents<T>();

		switch (components.Length)
		{
			case 0:
				return gameObject.transform.parent.TopComponent<T>();
			case 1:
				return components[0];
			default:
				throw new UnityException();
		}
	}

	public static T TopComponent<R, T>(this Component component)
		where T : Component
		where R : Component
	{
		return component.gameObject.TopComponent<R, T>();
	}

	public static T TopComponent<R, T>(this GameObject gameObject)
		where T : Component
		where R : Component
	{
		var components = gameObject.GetComponents<T>();

		switch (components.Length)
		{
			case 0:
				return null == gameObject.GetComponent<R>() ? gameObject.transform.parent.TopComponent<R, T>() : null;
			case 1:
				return components[0];
			default:
				throw new UnityException();
		}
	}
}

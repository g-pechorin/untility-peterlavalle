using UnityEngine;
using System.Collections;

public static class extComponent
{
  public static T OptComponent<T>(this Component component) where T : Component
  {
    return component.gameObject.OptComponent<T>();
  }

  public static T TopComponent<T>(this Component component) where T : Component
  {
    return null != component ? component.gameObject.TopComponent<T>() : null;
  }

  public static T TopComponent<R, T>(this Component component)
    where T : Component
    where R : Component
  {
    return component.gameObject.TopComponent<R, T>();
  }
}

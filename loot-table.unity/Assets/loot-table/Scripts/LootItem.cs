using UnityEngine;
using System.Collections;

public class LootItem : MonoBehaviour
{
#if UNITY_EDITOR
  void Update()
  {
    Debug.Log("LootItem scripts shouldn't be on actually running objects bro");
  }
#endif
}

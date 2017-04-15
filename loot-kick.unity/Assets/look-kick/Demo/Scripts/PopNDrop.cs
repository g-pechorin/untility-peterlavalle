using UnityEngine;
using System.Collections;

public class PopNDrop : MonoBehaviour
{
  public LootTable lootTable;

  void OnTriggerEnter(Collider other)
  {
    if ("Player" != other.tag)
      return;

    // find out where we are
    var position = transform.position;

    // remove ourself
    DestroyObject(gameObject);

    // create some loot!
    lootTable.Spawn(new System.Random()).transform.position = position;
  }
}

using UnityEngine;
using System.Collections;

[CreateAssetMenu()]
public class LootTable : ScriptableObject
{
  public LootItem[] lootItems = new LootItem[0];

  public GameObject Spawn(System.Random random)
  {
#if UNITY_EDITOR
    foreach (var lootItem in lootItems)
      Debug.Assert(null != lootItem, "Bro - clean your loot!");
#endif

    var loot = Instantiate(lootItems[random.Next() % lootItems.Length].gameObject);

    Destroy(loot.GetComponent<LootItem>());

    return loot;
  }
}

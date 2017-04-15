using UnityEngine;
using System.Collections.Generic;

public class Flooring : MonoBehaviour
{
  public GameObject rootFloor;
  public int span = 2;
  private HashSet<string> activated = new HashSet<string>();
  private Vector3 extents;

  void Start()
  {
    activated.Clear();
    extents = rootFloor.GetComponent<Renderer>().bounds.extents;

    activated.Add("0,0");
  }

  void Update()
  {
    var x = transform.position.x / extents.x;
    var y = transform.position.z / extents.z;

    var i = Mathf.FloorToInt((x * 0.5f) + 0.5f);
    var j = Mathf.FloorToInt((y * 0.5f) + 0.5f);

    for (var a = -span; a <= span; ++a)
      for (var b = -span; b <= span; ++b)
        Touch(i + a, j + b);
  }

  void Touch(int i, int j)
  {
    var id = i + "," + j;

    if (activated.Contains(id))
      return;

    var tile = Instantiate(rootFloor);

    tile.name += id;
    tile.transform.position += new Vector3(2 * i * extents.x, 0, 2 * j * extents.z);
    activated.Add(id);
  }
}

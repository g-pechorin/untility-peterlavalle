using UnityEngine;
using System.Collections.Generic;

public class Flooring : MonoBehaviour
{
  public GameObject rootFloor;

  private HashSet<string> activated = new HashSet<string>();
  private Vector3 extents;

  void Start()
  {
    activated.Clear();
    extents = rootFloor.GetComponent<Renderer>().bounds.extents;

    if (rootFloor.activeInHierarchy)
      activated.Add("0,0");
    else
      Touch(0, 0);
  }

  void Update()
  {
    var x = transform.position.x / extents.x;
    var y = transform.position.z / extents.z;

    var i = Mathf.FloorToInt((x * 0.5f) + 0.5f);
    var j = Mathf.FloorToInt((y * 0.5f) + 0.5f);

    Touch(i, j);
  }

  void Touch(int i, int j)
  {
    var id = i + "," + j;

    if (activated.Contains(id))
      return;

    Instantiate(rootFloor).transform.position += new Vector3(2 * i * extents.x, 0, 2 * j * extents.z);
    activated.Add(id);
  }
}

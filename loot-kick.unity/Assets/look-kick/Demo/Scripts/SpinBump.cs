using UnityEngine;
using System.Collections;

public class SpinBump : MonoBehaviour
{
  public int seed = 1983;

  public float shift = 3.14f;

  void Start()
  {
    var random = new System.Random(transform.position.ToString().GetHashCode() ^ seed);

    // spin the box
    var eulerAngles = transform.eulerAngles;
    eulerAngles.y += (float)(random.NextDouble() * 360.0);
    transform.eulerAngles = eulerAngles;

    // shift the box
    var x = System.Math.Abs(random.NextDouble()) * random.NextDouble() * shift;
    var z = System.Math.Abs(random.NextDouble()) * random.NextDouble() * shift;
    transform.position += new Vector3((float)x, 0, (float)z);
  }
}

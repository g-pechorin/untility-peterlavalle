using UnityEngine;
using System.Collections;

/// <summary>
/// Moves the game object around
/// </summary>
public class Looter : MonoBehaviour
{
  const float speed = 3.14f;
  const float sprint = 19.83f;

  void Update()
  {
    var step = (Input.GetButton("Jump") ? sprint : speed) * Time.deltaTime;

    transform.position += new Vector3(Input.GetAxis("Horizontal") * step, 0, Input.GetAxis("Vertical") * step);
  }
}

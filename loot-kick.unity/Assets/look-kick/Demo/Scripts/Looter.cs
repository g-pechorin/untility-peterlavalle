using UnityEngine;
using System.Collections;

/// <summary>
/// Moves the game object around
/// </summary>
public class Looter : MonoBehaviour
{
  const float speed = 3.14f;

  void Update()
  {
    transform.position += new Vector3(Input.GetAxis("Horizontal") * speed * Time.deltaTime, 0, Input.GetAxis("Vertical") * speed * Time.deltaTime);
  }
}

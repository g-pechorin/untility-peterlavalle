using UnityEngine;
using System.Collections;

public class Spin : MonoBehaviour

{

	public peterlavalle.InputProperty input = new peterlavalle.InputProperty();

	public peterlavalle.KeyProperty key = new peterlavalle.KeyProperty();



	public Vector3 speeds = new Vector3(0, 31.4f, 0);



	void Update()

	{

		var localRotation = transform.localRotation.eulerAngles;

		localRotation += speeds * Time.deltaTime * Mathf.Max(input.Axis, key.ButtonHeld ? 1 : 0);

		transform.localRotation = Quaternion.Euler(localRotation);

	}
}

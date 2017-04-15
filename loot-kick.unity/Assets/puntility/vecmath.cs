using UnityEngine;

/// <summary>
/// This was taken (and re-styled) from a forum post - not sure what license it would be under
/// </summary>
public static class vecmath
{
	/// <summary>
	/// Extract translation from transform matrix.
	/// </summary>
	/// <param name="matrix">Transform matrix</param>
	/// <returns>
	/// Translation offset.
	/// </returns>
	public static Vector3 GetTranslationFromMatrix(this Matrix4x4 matrix)
	{
		return new Vector3(
		           matrix.m03,
		           matrix.m13,
		           matrix.m23);
	}

	/// <summary>
	/// Extract rotation quaternion from transform matrix.
	/// </summary>
	/// <param name="matrix">Transform matrix</param>
	/// <returns>
	/// Quaternion representation of rotation transform.
	/// </returns>
	public static Quaternion GetRotationFromMatrix(this Matrix4x4 matrix)
	{
		Vector3 forward;
		forward.x = matrix.m02;
		forward.y = matrix.m12;
		forward.z = matrix.m22;

		Vector3 upwards;
		upwards.x = matrix.m01;
		upwards.y = matrix.m11;
		upwards.z = matrix.m21;

		return Quaternion.LookRotation(forward, upwards);
	}

	/// <summary>
	/// Extract scale from transform matrix.
	/// </summary>
	/// <param name="matrix">Transform matrix</param>
	/// <returns>
	/// Scale vector.
	/// </returns>
	public static Vector3 GetScaleFromMatrix(this Matrix4x4 matrix)
	{
		return new Vector3(
		           new Vector4(matrix.m00, matrix.m10, matrix.m20, matrix.m30).magnitude,
		           new Vector4(matrix.m01, matrix.m11, matrix.m21, matrix.m31).magnitude,
		           new Vector4(matrix.m02, matrix.m12, matrix.m22, matrix.m32).magnitude);
	}

	/// <summary>
	/// Set transform component from TRS matrix.
	/// </summary>
	/// <param name="transform">Transform component.</param>
	/// <param name="matrix">Transform matrix</param>
	public static void SetTransformFromMatrix(this Transform transform, Matrix4x4 matrix)
	{
		transform.localPosition = matrix.GetTranslationFromMatrix();
		transform.localRotation = matrix.GetRotationFromMatrix();
		transform.localScale = matrix.GetScaleFromMatrix();
	}
}

using UnityEngine;
using System.Collections;
public static class rando
{
	public static double Next(this System.Random random, double min, double max)
	{
		return min + (max - min) * random.NextDouble();
	}
}

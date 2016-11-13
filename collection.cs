using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// These allow me to use some parts of C# like some parts of Scala
/// ... someday I'll add a lazy-Stream
/// </summary>
/// 
/// 2016-11-05 ;
///		- copied some stuff from quad-fight that was missing
///
/// 2016-10-14 ;
///		- merged together
/// 2016-09-21 ;
///		- added this comment block
///		- added some null-handling stuff
///		- added a "Has" method
public static class collection
{
	public delegate void Lambda<I>(I a);
	public delegate O Lambda1<I0, O>(I0 a0);
	public delegate O Lambda2<I0, I1, O>(I0 a0, I1 a1);

	public static List<T> Distinct<T>(this IEnumerable<T> inputs)
	{
		return inputs.FoldLeft(new List<T>(), (list, next) =>
		{
			if (-1 == list.IndexOf(next))
				list.Add(next);

			return list;
		});
	}

	public static List<T> Filter<T>(this IEnumerable<T> input, Lambda1<T, bool> lambda)
	{
		var result = new List<T>();

		if (null != input)
			foreach (var i in input)
				if (lambda(i))
					result.Add(i);

		return result;
	}

	public static T Find<T>(this IEnumerable<T> input, Lambda1<T, bool> lambda) where T : class
	{
		foreach (var i in input)
			if (lambda(i))
				return i;

		return null;
	}

	public static List<T> Flatten<T>(this IEnumerable<T[]> inputs)
	{
		var result = new List<T>();

		foreach (var i in inputs)
			foreach (var j in i)
				result.Add(j);

		return result;
	}

	public static List<T> Flatten<T>(this IEnumerable<IEnumerable<T>> inputs)
	{
		var result = new List<T>();

		foreach (var i in inputs)
			foreach (var j in i)
				result.Add(j);

		return result;
	}

	public static O FoldLeft<T, O>(this IEnumerable<T> inputs, O value, Lambda2<O, T, O> lambda)
	{
		foreach (var i in inputs)
			value = lambda(value, i);

		return value;
	}

	public static List<List<T>> GroupsOf<T>(this IEnumerable<T> inputs, int count)
	{
		List<List<T>> output = new List<List<T>>();

		var last = new List<T>();

		foreach (var entry in inputs)
		{
			if (0 == last.Count)
				output.Add(last);

			last.Add(entry);

			if (count == last.Count)
				last = new List<T>();
		}

		return output;
	}

	public static bool Has<T>(this IEnumerable<T> input, Lambda1<T, bool> lambda)
	{
		if (null != input)
			foreach (var i in input)
				if (lambda(i))
					return true;

		return false;
	}

	public static T Head<T>(this IEnumerable<T> input)
	{
		foreach (var i in input)
			return i;

		throw new UnityException();
	}

	public static bool Includes<T>(this IEnumerable<T> input, params T[] things) where T : class
	{
		foreach (var thing in things)
			if (!input.Has(t => t == thing))
				return false;

		return true;
	}

	public static int HeadIndexOf<T>(this IEnumerable<T> input, Lambda1<T, bool> lambda)
	{
		int index = 0;

		foreach (var i in input)
			if (lambda(i))
				return index;
			else
				++index;

		return -1;
	}

	public static bool IsEmpty<T>(this IEnumerable<T> input) where T : class
	{
		return !(input.IsNonEmpty());
	}

	public static bool IsNonEmpty<T>(this IEnumerable<T> input) where T : class
	{
		return input.GetEnumerator().MoveNext();
	}

	public static T Pull<T>(this List<T> list)
	{
		var head = list[0];
		list.RemoveAt(0);
		return head;
	}

	public static IEnumerable<T> RandomOrder<T>(this IEnumerable<T> input)
	{
		var array = input.ToArrayCopy();

		var seen = new HashSet<int>();
		var list = new List<T>();

		while (seen.Count < array.Length)
		{
			var index = Random.Range(0, array.Length);

			while (seen.Contains(index))
				index = (index + 1) % array.Length;

			seen.Add(index);
			list.Add(array[index]);
		}

		return list;
	}

	public static T Reduce<T>(this IEnumerable<T> input, Lambda2<T, T, T> lambda)
	{
		return input.Tail().FoldLeft(input.Head(), lambda);
	}

	public static List<O> Mapping<I, O>(this IEnumerable<I> input, Lambda1<I, O> lambda)
	{
		var result = new List<O>();

		foreach (var i in input)
			result.Add(lambda(i));

		return result;
	}

	public static List<T> Tail<T>(this IEnumerable<T> input)
	{
		var result = new List<T>();
		bool head = true;

		foreach (var i in input)
			if (head)
				head = false;
			else
				result.Add(i);

		return result;
	}

	public static T[] ToArrayCopy<T>(this IEnumerable<T> input)
	{
		List<T> list = input as List<T>;

		if (null == list)
		{
			list = new List<T>();
			if (null != input)
				foreach (var i in input)
					list.Add(i);
		}

		T[] result = new T[list.Count];
		for (int i = 0; i < result.Length; ++i)
		{
			result[i] = list[i];
		}
		return result;
	}
}

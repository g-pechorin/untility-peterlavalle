/// Peter LaValle / nottingham.ac.uk / gmail.com
///
/// Extension methods for string(s)
///
/// 2017-02-11 ;
///		- head-stubstring
/// 2016-11-05 ;
///		- better substring
///

using UnityEngine;
using System.Collections;

public static class strings
{
	public static string SubString(this string value, int head, char last)
	{
		return value.Substring(head, value.LastIndexOf(last));
	}

	public static string SubString(this string value, char head)
	{
		return value.Substring(value.IndexOf(head) + 1);
	}
}

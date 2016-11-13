/// Peter LaValle / nottingham.ac.uk / gmail.com
/// 
/// Extension methods for string(s)
/// 
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
}

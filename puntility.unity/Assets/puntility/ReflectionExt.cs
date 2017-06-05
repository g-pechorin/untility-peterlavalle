
public static class ReflectionExt
{
  public static string GetValueString(this System.Reflection.FieldInfo field, object subject)
  {
    return field.GetValue(subject).ToString();
  }

  public static void SetValueString(this System.Reflection.FieldInfo field, object subject, string value)
  {
    switch (field.FieldType.Name)
    {
      case "Boolean":
        field.SetValue(subject, bool.Parse(value));
        return;

      case "Int32":
        field.SetValue(subject, int.Parse(value));
        return;

      case "String":
        field.SetValue(subject, value);
        break;

      case "Single":
        field.SetValue(subject, float.Parse(value));
        return;

      default:
        throw new System.Exception("field.FieldType.Name = `" + field.FieldType.Name + "`");
    }
  }

  public static string GetValueString(this System.Reflection.PropertyInfo property, object subject)
  {
    return property.GetGetMethod().Invoke(subject, new object[] { subject }).ToString();
  }

  public static void SetValueString(this System.Reflection.PropertyInfo property, object subject, string value)
  {
    switch (property.PropertyType.Name)
    {
      case "Boolean":
        property.GetSetMethod().Invoke(subject, new object[] { bool.Parse(value) });
        return;

      case "Int32":
        property.GetSetMethod().Invoke(subject, new object[] { int.Parse(value) });
        return;

      case "String":
        property.GetSetMethod().Invoke(subject, new object[] { value });
        return;

      case "Single":
        property.GetSetMethod().Invoke(subject, new object[] { float.Parse(value) });
        return;

      default:
        throw new System.Exception("field.PropertyType.Name = `" + property.PropertyType.Name + "`");
    }
  }

}

[HomePage on BitBucket](https://bitbucket.org/g-pechorin/unti)

This is ...

* a "mee too" code dump of extension methods and utility classes for Unity
* **unit** a gradle setup to export packages and rebuild/reimport dependencies

... obviously, the later needs more-work.

## unti


### Adding a new project

1. add it to `settings.gradle`
1. add a (properly named) folder
1. create a Unity3D project in that folder
1. edit `build.gradle` to do dependencies



## Contents

### [puntility](puntility.unitypackage) 

A bunch of extension methods that I'm fond of.
Also has a nice class to make ScriptedObject asset creation easier.

### [input-property](input-property.unitypackage) 

It's a neat class for controls.

You get a drop-down in the inspector, and a `struct` with RO properties in `C#`

(It relies upon the `puntility` package)

### [loot-table](loot-table.unitypackage)

A [ScriptableObject](https://docs.unity3d.com/510/Documentation/ScriptReference/ScriptableObject.html) demonstrating [a sort of loot-table](loot-table.unity/Assets/loot-table/Scripts/LootTable.cs) for spawning [loot items](loot-table.unity/Assets/loot-table/Scripts/LootItem.cs) with a [demonstration](loot-table.unity/Assets/loot-table/).


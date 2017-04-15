
This is ...

* a "mee too" code dump of extension methods and utility classes for Unity
* a gradle setup to export packages and rebuild/reimport dependencies

... obviously, the later needs more-work.

## Contents
### [puntility](puntility.unitypackage) 

A bunch of extension methods that I'm fond of.
Also has a nice class to make ScriptedObject asset creation easier.

### [input-property](input-property.unitypackage) 

It's a neat class for controls.

You get a drop-down in the inspector, and a `struct` with RO properties in `C#`

(It relies upon the `puntility` package)

## Adding a new project

1. add it to `settings.gradle`
1. add a (properly named) folder
1. create a Unity3D project in that folder
1. edit `build.gradle` to do dependencies

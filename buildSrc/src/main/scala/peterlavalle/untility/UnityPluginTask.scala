
package peterlavalle.untility

import java.io.File
import java.nio.file.Files

import org.gradle.api.{GradleException, Project}
import org.gradle.api.tasks.TaskAction

import scala.beans.BeanProperty
import scala.collection.JavaConversions._

class UnityPluginTask extends TUnityTask {

  lazy val tempProject: File = {
    require(getProject.getBuildDir.exists() || getProject.getBuildDir.mkdirs())
    val tempProject = File.createTempFile(getName, ".unity", getProject.getBuildDir)
    requyre[GradleException](
      tempProject.exists() && tempProject.delete() && tempProject.mkdirs(),
      "Error when trying to create tempProject's folder"
    )
    tempProject
  }

  val unityName: String = getProject.unityName

  @BeanProperty
  var monoCompiler: File =
    osName match {
      case "windows" =>
        unityHome / "Editor/Data/MonoBleedingEdge/lib/mono/4.5/mcs.exe"
      //unityHome / "Editor/Data/MonoBleedingEdge/bin/mcs.bat"
      //unityHome / "Editor/Data/Mono/lib/mono/2.0/gmcs"
    }

  @BeanProperty
  var monoOptions: Array[String] =
    Array(
      /*s"-r:${
        '"' + (osName match {
          case "windows" =>
            (unityHome / "Editor/Data/Mono/lib/mono/2.0/mscorlib.dll").AbsolutePath
        }) + '"'
      }",
      */
      "-optimize",
      // "-nostdlib",
      "-target:library"
    )


  @TaskAction
  def action(): Unit =
    require(
      unityPackage.exists()
    )

  lazy val unityPackage: File = {

    // copy our project settings
    requyre[GradleException]((tempProject / "ProjectSettings").mkdirs(), "Failed to create ProjectSettings/ folder")
    (getProject.getProjectDir ** "^ProjectSettings/.*")
      .foreach {
        setting =>
          Files.copy(
            (getProject.getProjectDir / setting).toPath,
            (tempProject / setting).toPath
          )
      }

    requyre[GradleException]((tempProject / "Assets").mkdirs(), "Failed to create Assets/ folder")

    // import any/all "other" projects
    config.links.foreach {
      case link: Project =>
        invoke(
          tempProject, List("-importPackage", link.findTask[UnityPluginTask].unityPackage)
        ) match {
          case 0 => ;
        }
    }

    // collect source
    val csSource: Stream[String] =
      (getProject.getProjectDir ** ".*\\.cs")
        .filter(_.startsWith(s"Assets/$unityName/"))
        .filterNot(_.matches("(^|(.*/))(Demo|Test)/.*"))

    // compile our Plugin code into a DLL
    val dllProjectPlugin: File = {
      System.err.println("TODO ; make this into a task")
      tempProject / s"Assets/$unityName/Plugin/$unityName.Plugin.dll"
    }
    require(dllProjectPlugin.getParentFile.exists() || dllProjectPlugin.getParentFile.mkdirs())


    val csSourcePlugin: Stream[File] =
      csSource
        .filterNot(_.contains("/Editor/"))
        .map(getProject.getProjectDir / _)

    if (csSourcePlugin.isEmpty)
      ???
    else
      shellScript(s"$unityName - compile plugin.dll")(
        List(
          s"${'"' + monoCompiler.getAbsolutePath + '"'}",
          s"-r:${'"' + dllUnityEngine.AbsolutePath + '"'}",

          (tempProject / "Assets" ** ".*\\.dll")
            .filterNot(_.contains("/Editor/"))
            .map(tempProject / _)
            .map {
              file: File =>
                s"-r:${'"' + file.AbsolutePath + '"'}"
            },

          monoOptions.toStream,

          csSource
            .filterNot(_.contains("/Editor/"))
            .map(getProject.getProjectDir / _),

          s"-out:${'"' + dllProjectPlugin.AbsolutePath + '"'}"
        )
      ) match {
        case 0 => ;
      }

    // compile our Editor code into a DLL
    val dllProjectEditor: File = {
      System.err.println("TODO ; make this into a task")
      tempProject / s"Assets/$unityName/Editor/$unityName.Editor.dll"
    }
    require(dllProjectEditor.getParentFile.exists() || dllProjectEditor.getParentFile.mkdirs())

    val csSourceEditor: Stream[File] =
      csSource
        .filter(_.contains("/Editor/"))
        .map(getProject.getProjectDir / _)

    if (csSourceEditor.isEmpty)
      ???
    else
      shellScript(s"$unityName - compile editor.dll")(
        List(
          s"${'"' + monoCompiler.getAbsolutePath + '"'}",
          s"-r:${'"' + dllUnityEngine.AbsolutePath + '"'}",
          s"-r:${'"' + dllUnityEditor.AbsolutePath + '"'}",
          s"-r:${'"' + dllProjectPlugin.AbsolutePath + '"'}",

          (tempProject / "Assets" ** ".*\\.dll")
            .map(tempProject / _)
            .map {
              file: File =>
                s"-r:${'"' + file.AbsolutePath + '"'}"
            },

          monoOptions.toStream,

          csSourceEditor,

          s"-out:${'"' + dllProjectEditor.AbsolutePath + '"'}"
        )
      ) match {
        case 0 => ;
      }

    // copy our assets
    (getProject.getProjectDir **)
      .filter(_.startsWith(s"Assets/$unityName/"))
      .filterNot(_.matches("(^|(.*/))(Demo|Test)/.*"))
      .filterNot {
        case code if code.endsWith(".cs.meta") || code.endsWith(".cs") => true
        case meta if meta.endsWith(".meta") =>
          (getProject.getProjectDir / meta.dropRight(5)).exists()
      }
      .map(">>> " + _)
      .foreach {
        asset =>
          s"TODO ; need the copy-asset code for asset ; $asset" halt
      }

    // perform the final export

    val assets =
      (tempProject **)
        .filterNot(_.endsWith(".meta"))
        .filterNot(_.matches("(^|(.*/))(Demo|Test)/.*"))
        .filter(_.startsWith(s"Assets/$unityName/"))

    val unityPackage = getProject.getRootDir / s"$unityName.unitypackage"

    if (unityPackage.exists())
      require(unityPackage.delete(), "Delete command failed")

    require(!unityPackage.exists(), "Delete command didn't work")

    // https://forum.unity3d.com/threads/exportpackage-command-line-in-unity-3-5-7.210480/
    invoke(tempProject,
      List("-exportPackage") ++ assets ++ List(unityPackage.AbsolutePath)
    )

    require(unityPackage.exists(), "Package wasn't created")

    unityPackage
  }
}

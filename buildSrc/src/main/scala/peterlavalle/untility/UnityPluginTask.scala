
package peterlavalle.untility

import java.io.File
import java.nio.file.Files

import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

import scala.collection.JavaConversions._

class UnityPluginTask extends TUnityTask {

  lazy val tempProject: File =
    File.createTempFile(getName, ".unity", getProject.getBuildDir)

  def monoCompiler: File =
    osName match {
      case "windows" =>
        unityHome / "Editor/Data/MonoBleedingEdge/lib/mono/4.5/mcs.exe"
      //unityHome / "Editor/Data/MonoBleedingEdge/bin/mcs.bat"
    }

  def dllUnityEngine: File =
    osName match {
      case "windows" =>
        unityHome / "Editor/Data/Managed/UnityEngine.dll"
    }

  def dllUnityEditor: File =
    osName match {
      case "windows" =>
        unityHome / "Editor/Data/Managed/UnityEditor.dll"
    }

  @TaskAction
  def action(): Unit = {
    require(tempProject.exists() && tempProject.delete() && tempProject.mkdirs())

    // copy our project settings
    (getProject.getProjectDir **)
      .filter(_.startsWith("ProjectSettings/"))
      .foreach {
        setting =>
          Files.copy(
            (getProject.getProjectDir / setting).toPath, {
              val output = tempProject / setting
              require(output.getParentFile.exists() || output.getParentFile.mkdirs())
              output.toPath
            }
          )
      }

    // import any/all "other" projects
    config.links.foreach {
      case link: Project =>
        invoke(
          tempProject, List("-importPackage", link.findTask[UnityExportTask].unityPackage)
        ) match {
          case 0 => ;
        }
    }

    val unityName: String = getProject.unityName


    // compile our Plugin code into a DLL
    val dllPlugin = tempProject / s"Assets/$unityName/Plugin/$unityName.Plugin.dll"
    require(dllPlugin.getParentFile.exists() || dllPlugin.getParentFile.mkdirs())

    shellScript(
      List(
        s"${'"' + monoCompiler.getAbsolutePath + '"'}",
        s"-r:${'"' + dllUnityEngine.AbsolutePath + '"'}",
        config.links.toStream.map {
          link =>
            "Link the project plugin.DLL"
        },
        "-target:library",

        (getProject.getProjectDir ** ".*\\.cs")
          .filter(_.startsWith(s"Assets/$unityName/"))
          .filterNot(_.matches("(^|(.*/))(Demo|Test)/.*"))
          .filterNot(_.contains("/Editor/"))
          .map(getProject.getProjectDir / _),

        s"-out:${'"' + dllPlugin.AbsolutePath + '"'}"
      )
    ) match {
      case 0 => ;
    }

    // compile our Editor code into a DLL
    val dllEditor = tempProject / s"Assets/$unityName/Editor/$unityName.Editor.dll"
    require(dllEditor.getParentFile.exists() || dllEditor.getParentFile.mkdirs())

    shellScript(
      List(
        s"${'"' + monoCompiler.getAbsolutePath + '"'}",
        s"-r:${'"' + dllUnityEngine.AbsolutePath + '"'}",
        s"-r:${'"' + dllUnityEditor.AbsolutePath + '"'}",
        s"-r:${'"' + dllPlugin.AbsolutePath + '"'}",
        config.links.toStream.map {
          link =>
            "Link the project plugin.DLL"
        },
        config.links.toStream.map {
          link =>
            "Link the project editor.DLL"
        },
        "-target:library",

        (getProject.getProjectDir ** ".*\\.cs")
          .filter(_.startsWith(s"Assets/$unityName/"))
          .filterNot(_.matches("(^|(.*/))(Demo|Test)/.*"))
          .filter(_.contains("/Editor/"))
          .map(getProject.getProjectDir / _),

        s"-out:${'"' + dllEditor.AbsolutePath + '"'}"
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
    "perform the final export" halt
  }
}


package peterlavalle.untility

import java.io.File

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

  @TaskAction
  def action(): Unit = {
    require(tempProject.exists() && tempProject.delete() && tempProject.mkdirs())

    // create an empty project
    invoke(
      tempProject,
      List(
        "-createProject",
        tempProject.AbsolutePath
      )
    ) match {
      case 0 => ;
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

    val dllPlugin =
      tempProject / s"Assets/$unityName/Plugin/$unityName.Plugin.dll"

    require(dllPlugin.getParentFile.exists() || dllPlugin.getParentFile.mkdirs())

    // compile our Plugin code into DLLs
    shellScript(
      List(
        s"${'"' + monoCompiler.getAbsolutePath + '"'}",
        s"-r:${'"' + dllUnityEngine.AbsolutePath + '"'}",
        "-target:library",

        (getProject.getProjectDir ** ".*\\.cs")
          .filter(_.startsWith(s"Assets/$unityName/"))
          .filterNot(_.contains("/Editor/"))
          .map(getProject.getProjectDir / _),

        s"-out:${'"' + dllPlugin.AbsolutePath + '"'}"
      )
    ) match {
      case 0 => ;
    }

    s"Done ; ${dllPlugin.AbsolutePath}" halt


    // compile our Editor code into DLLs
    "compile our Editor code into DLLs" halt

    // copy our assets
    "copy our assets" halt

    // perform the final export
    "perform the final export" halt
  }
}

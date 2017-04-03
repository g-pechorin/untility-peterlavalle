package peterlavalle.untility

import java.io.File

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UnityExportTask extends TUnityTask {

  @TaskAction
  def action(): Unit = {
    require(unityPackage.exists())
  }

  lazy val unityPackage: File = {
    require(
      getProject.getName.endsWith(".unity"),
      "Project folders must end with `.unity`"
    )

    val name = getProject.unityName

    require((getProject.getProjectDir / s"Assets/$name/").isDirectory)
    require((getProject.getProjectDir / s"Assets/$name.meta").isFile)

    val assets =
      ((getProject.getProjectDir / s"Assets/$name/") **)
        .filterNot(_.endsWith(".meta"))
        .filterNot(_.matches("(^|(.*/))(Demo|Test)/.*"))
        .map(s"Assets/$name/" + _)

    val unityPackage = getProject.getRootDir / s"$name.unitypackage"

    // https://forum.unity3d.com/threads/exportpackage-command-line-in-unity-3-5-7.210480/
    invoke(
      List("-exportPackage" )++ assets ++ List(unityPackage.AbsolutePath)
    )

    unityPackage
  }
}

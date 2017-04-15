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
    val assetDirectory = (getProject.getProjectDir / "Assets" / s"$name").getAbsoluteFile

    /*
    require(
      assetDirectory.exists(),
      s"Couldn't find an asset folder named $name\n\t${assetDirectory.AbsolutePath}"
    )

    require(
      assetDirectory.isDirectory,
      s"Couldn't find an asset folder named $name\n\t${assetDirectory.AbsolutePath}"
    )

    require(
      (getProject.getProjectDir / "Assets" / s"$name.meta").isFile,
      s"Couldn't find the meta-file for ${name}"
    )
    */

    val assets =
      (assetDirectory **)
        .filterNot(_.endsWith(".meta"))
        .filterNot(_.matches("(^|(.*/))(Demo|Test)/.*"))
        .map(s"Assets/$name/" + _)

    val unityPackage = getProject.getRootDir / s"$name.unitypackage"

    if (unityPackage.exists())
      require(unityPackage.delete(), "Delete command failed")

    require(!unityPackage.exists(), "Delete command didn't work")

    // https://forum.unity3d.com/threads/exportpackage-command-line-in-unity-3-5-7.210480/
    invoke(
      List("-exportPackage") ++ assets ++ List(unityPackage.AbsolutePath)
    )

    require(unityPackage.exists(), "Package wasn't created")

    unityPackage
  }
}

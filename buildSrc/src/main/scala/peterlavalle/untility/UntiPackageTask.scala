package peterlavalle.untility

import java.io.File

import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

/**
  * Assembles the final .unitypackage
  */
class UntiPackageTask extends TUnityTask {
  setDescription(
    "Assembles the final .unitypackage"
  )

  @TaskAction
  def action(): Unit =
    require(unityPackage.exists())

  lazy val unityPackage: File = {
    val makeSpace = task[UntiMakeSpaceTask].makeSpace

    val unityPackage = getProject.getRootDir / s"$unityName.unitypackage"

    if (unityPackage.exists())
      requyre[GradleException](unityPackage.delete(), s"Delete `$unityName.unitypackage` failed")

    requyre[GradleException](!unityPackage.delete(), s"Delete `$unityName.unitypackage` failed")

    val assets = makeSpace ** "^Assets/.*"

    // https://forum.unity3d.com/threads/exportpackage-command-line-in-unity-3-5-7.210480/
    invoke(
      makeSpace,
      List("-exportPackage") ++ assets ++ List(unityPackage.AbsolutePath)
    ) match {
      case 0 =>
        requyre[GradleException](
          unityPackage.exists(),
          s"Package `$unityName.unitypackage` wasn't created"
        )

        unityPackage
      case fail =>
        throw new GradleException(
          s"Failed -exportPackage with code `$fail` in module $unityName from\n  >${makeSpace.AbsolutePath}<"
        )
    }
  }
}

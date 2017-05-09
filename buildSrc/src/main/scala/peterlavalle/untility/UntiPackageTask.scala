package peterlavalle.untility

import java.io.File
import java.util.Date

import org.gradle.api.GradleException
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.internal.GradleInternal
import org.gradle.api.tasks.{TaskAction, TaskDependency}
import org.gradle.initialization.BuildRequestMetaData

import scala.beans.BeanProperty

/**
  * Assembles the final .unitypackage
  */
class UntiPackageTask extends TUnityTask with PublishArtifact {
  setDescription(
    "Assembles the final .unitypackage"
  )


  override def getType: String = "unitypackage"

  override def getDate: Date = {
    new Date(
      getProject.getGradle.asInstanceOf[GradleInternal].getServices.get[BuildRequestMetaData](classOf[BuildRequestMetaData]).getBuildTimeClock.getStartTime
    )
  }

  override def getExtension: String = "unitypackage"

  override def getClassifier: String = "unitypackage"


  @BeanProperty
  val file: File = getProject.getRootDir / s"$unityName.unitypackage"

  override def getBuildDependencies: TaskDependency =
    this.getTaskDependencies

  @TaskAction
  def action(): Unit =
    require(unityPackage.exists())

  lazy val unityPackage: File = {
    val makeSpace = task[UntiMakeSpaceTask].makeSpace

    val unityPackage = getProject.getRootDir / s"$unityName.unitypackage"

    if (unityPackage.exists())
      requyre[GradleException](unityPackage.delete(), s"Delete `$unityName.unitypackage` failed")

    requyre[GradleException](!unityPackage.delete(), s"Delete `$unityName.unitypackage` failed")

    val assets =
      (makeSpace ** "^Assets/.*")
        .filterNot(_.endsWith(".meta"))
        .filter(_.startsWith(s"Assets/$unityName/"))

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

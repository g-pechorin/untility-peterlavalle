package peterlavalle.untility

import java.io.File

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UnityExportTask extends DefaultTask {

  lazy val unityEditor: File =
    osName match {
      case "windows" =>
        ('A' to 'Z')
          .map(drive => s"$drive:/Program Files (x86)/Unity/5.1.4f1/Editor/Unity.exe")
          .map(new File(_))
          .find(_.exists) match {
          case Some(file: File) => file
        }
    }

  @TaskAction
  def action(): Unit = {

    require(
      getProject.getName.endsWith(".unity"),
      "Project folders must end with `.unity`"
    )

    val name = getProject.getName.replaceAll("\\.unity$", "")

    require((getProject.getProjectDir / s"Assets/$name/").isDirectory)
    require((getProject.getProjectDir / s"Assets/$name.meta").isFile)

    val assets =
      ((getProject.getProjectDir / s"Assets/$name/") **)
        .filterNot(_.endsWith(".meta"))
        .filterNot(_.matches("(^|(.*/))Demo/.*"))
        .map(s"Assets/$name/" + _)

    // https://forum.unity3d.com/threads/exportpackage-command-line-in-unity-3-5-7.210480/
    val commands =
      List(unityEditor.AbsolutePath, "-batchmode", "-projectPath", getProject.getProjectDir.AbsolutePath, "-exportPackage") ++ assets ++ List((getProject.getProjectDir / s"../$name.unitypackage").AbsolutePath, "-quit")

    getProject.getBuildDir.shell(new Feedback("unity:"))(commands: _ *) match {
      case 0 => ;
      case ret =>
        commands.foldLeft(s"I did `$ret`")(_ + "\n  " + _) halt
    }

  }
}

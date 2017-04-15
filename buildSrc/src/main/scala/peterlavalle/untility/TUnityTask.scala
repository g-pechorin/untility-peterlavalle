package peterlavalle.untility

import java.io.File

import org.gradle.api.DefaultTask
import peterlavalle.{Feedback, OverWriter}

trait TUnityTask extends DefaultTask {

  lazy val unityEditor: File =
    osName match {
      case "windows" =>
        (for (version <- List("/5.1.4f1", "/5.6.0f3", ""); folder <- List("Program Files (x86)", "Program Files"); drive <- 'A' to 'Z')
          yield new File(s"$drive:/$folder/Unity$version/Editor/Unity.exe"))
          .find(_.exists) match {
          case Some(file: File) => file
        }
    }

  def config: Config =
    getProject.getExtensions.findByName("unity").asInstanceOf[Config]

  def invoke(commands: Iterable[AnyRef]): Int = {

    val actualCommand =
      if (config.batchMode)
        List(unityEditor.AbsolutePath, "-batchmode")
      else
        List(unityEditor.AbsolutePath, "-nographics")

    val actualProject =
      List("-projectPath", getProject.getProjectDir.AbsolutePath)

    val actualCommands =
      commands.map {
        case string: String => string
        case file: File => file.AbsolutePath
      }

    val actual =
      actualCommand ++ actualProject ++ actualCommands ++ List("-quit")

    require("windows" == osName)
    val script =
      new OverWriter(getProject.getBuildDir / s"${getProject.getName}-${actual.hashCode()}.bat")
        .appund("@ECHO OFF\r\n")
        .appund(actual.map {
          next: String =>
            require(!next.contains("\r"))
            require(!next.contains("\t"))
            if (next.contains(' ') || next.contains('\t')) {
              require(!next.contains("\""))
              '"' + next + "\" "
            } else
              next
        }.reduce(_ + ' ' + _))
        .closeFile.getAbsolutePath

    getProject.getBuildDir.shell(new Feedback("unity:"))(script)
  }
}

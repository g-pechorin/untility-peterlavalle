package peterlavalle.untility

import java.io.File

import org.gradle.api.DefaultTask
import org.gradle.api.internal.AbstractTask
import peterlavalle.{Feedback, OverWriter}

import scala.collection.immutable.Stream.Empty
import scala.reflect.ClassTag

trait TUntiTask extends DefaultTask {

  def dllUnityEngine: File =
    osName match {
      case "windows" =>
        config.unityHome / "Editor/Data/Managed/UnityEngine.dll"
    }

  def dllUnityEditor: File =
    osName match {
      case "windows" =>
        config.unityHome / "Editor/Data/Managed/UnityEditor.dll"
    }

  lazy val unityEditor: File =
    osName match {
      case "windows" =>
        config.unityHome / "Editor/Unity.exe"
    }

  val unityName: String = getProject.unityName

  def task[T <: AbstractTask](implicit classTag: ClassTag[T]): T =
    getProject.findTask(classTag)

  setGroup("unti")

  def shellScript(commands: Iterable[Any]): Int =
    shellScript(getName)(commands)

  def shellScript(shellName: String)(commands: Iterable[Any]): Int =
    shellScript(shellName, getProject.getBuildDir)(commands)

  def shellScript(shellName: String, workingDir: File)(commands: Iterable[Any]): Int =
    workingDir.shell(new Feedback(s"$shellName; "))(
      osName match {
        case "windows" =>
          def recu(todo: Stream[Any]): List[String] =
            todo match {
              case (head: String) #:: tail =>
                head :: recu(tail)
              case (file: File) #:: tail =>
                file.AbsolutePath :: recu(tail)
              case (stream: Stream[_]) #:: tail =>
                recu(stream) ++ recu(tail)
              case Empty =>
                Nil
            }

          new OverWriter(File.createTempFile("script-", ".bat", getProject.getBuildDir))
            .appund("@ECHO OFF\r\n")
            .appund("\r\n")
            .appund(recu(commands.toStream).reduce(_ + " " + _))
            .appund("\r\n")
            .closeFile.AbsolutePath
      }
    )

  def config: Config =
    getProject.getExtensions.findByName("unity").asInstanceOf[Config]

  def invoke(commands: Iterable[AnyRef]): Int = {
    invoke(getProject.getProjectDir, commands)
  }

  def invoke(projectDir: File, commands: Iterable[AnyRef]): Int = {
    val actualCommand =
      if (config.batchMode)
        List(unityEditor.AbsolutePath, "-batchmode")
      else
        List(unityEditor.AbsolutePath, "-nographics")

    val actualProject =
      List("-projectPath", projectDir.AbsolutePath)

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

    projectDir.shell(new Feedback("unity:"))(script)
  }
}

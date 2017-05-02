package peterlavalle.untility

import java.io.File

import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

trait TUntiDLLTask extends TUnityTask {
  val csFilter: String => Boolean

  val label: String

  // collect source
  def csSources: Stream[File] =
    (getProject.getProjectDir ** ".*\\.cs")
      .filter(_.startsWith(s"Assets/$unityName/"))
      .filterNot(_.matches("(^|(.*/))(Demo|Test)/.*"))
      .filter(csFilter)
      .map(getProject.getProjectDir / _)

  @TaskAction
  def action() =
    require(
      dllAssembly.exists()
    )

  def dllReferences: Stream[File] =
    dllUnityEngine #:: (task[UntiMakeSpaceTask].makeSpace ** "Assets/.*\\.dll")
      .filterNot(_.startsWith(s"Assets/$unityName/"))
      .map(task[UntiMakeSpaceTask].makeSpace / _)

  lazy val dllAssembly: File = {

    val workSpace = task[UntiMakeSpaceTask].makeSpace
    val dllAssembly = workSpace / s"Assets/$unityName/$label/$unityName.$label.dll"

    requyre[GradleException](
      !dllAssembly.exists() || dllAssembly.delete(),
      "Couldn't delete the old DLL - likely something is holding it"
    )

    requyre[GradleException](
      dllAssembly.getParentFile.exists() || dllAssembly.getParentFile.mkdirs(),
      "Couldn't create parent-dir for .cs assembly"
    )

    requyre[GradleException](
      csSources.nonEmpty,
      "Need some .cs - soz"
    )

    if (csSources.isEmpty)
      ???
    else
      shellScript(s"$unityName - compile $label's .dll")(
        List(
          s"${'"' + config.monoCompiler.getAbsolutePath + '"'}",

          dllReferences.map(_.AbsolutePath).distinct
            .map {
              path: String =>
                s"-r:${'"' + path + '"'}"
            },

          config.monoLibraries.toStream,
          config.monoOptions.toStream,

          csSources,

          s"-out:${'"' + dllAssembly.AbsolutePath + '"'}"
        )
      ) match {
        case 0 => dllAssembly
      }
  }
}

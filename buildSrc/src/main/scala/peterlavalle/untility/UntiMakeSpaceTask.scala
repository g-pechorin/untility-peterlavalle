package peterlavalle.untility

import java.io.File
import java.nio.file.Files

import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

/**
  * Generates an empty Unity workspace and copies in `ProjectSettings/`
  *
  * From experience, the 'only' reliable way to reproducibly import a pile of stuff is into a blank project
  */
class UntiMakeSpaceTask extends TUntiTask {

  setDescription("Generates an empty Unity workspace and copies in `ProjectSettings/`")

  @TaskAction
  def action(): Unit =
    require(
      makeSpace.exists()
    )

  lazy val makeSpace: File = {
    require(getProject.getBuildDir.exists() || getProject.getBuildDir.mkdirs())
    val makeSpace = getProject.getBuildDir / s"unit-${getProject.getName}"

    if (makeSpace.exists())
      requyre[GradleException](
        makeSpace.unlink(),
        s"Error deleting ${makeSpace.AbsolutePath}"
      )
    requyre[GradleException](makeSpace.mkdirs(),
      s"Error when trying to create ${makeSpace.AbsolutePath}"
    )

    // copy our project settings
    requyre[GradleException]((makeSpace / "ProjectSettings").mkdirs(), "Failed to create ProjectSettings/ folder")
    (getProject.getProjectDir ** "^ProjectSettings/.*")
      .foreach {
        setting =>
          Files.copy(
            (getProject.getProjectDir / setting).toPath,
            (makeSpace / setting).toPath
          )
      }

    // make the assets folder
    requyre[GradleException](
      (makeSpace / s"Assets/$unityName").mkdirs(),
      "Failed to create Assets/ folder"
    )

    makeSpace
  }
}

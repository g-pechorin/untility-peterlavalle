package peterlavalle.untility

import org.gradle.api.tasks.TaskAction

class UntiAssembleAssetsTask extends TUntiTask {

  setDescription("Copy (non code) assets into workspace")

  @TaskAction
  def action(): Unit =
    (getProject.getProjectDir **)
      .filter(_.startsWith(s"Assets/$unityName/"))
      .filterNot(_.matches("(^|(.*/))(Demo|Test)/.*"))
      .filterNot {
        case code if code.endsWith(".cs.meta") || code.endsWith(".cs") => true
        case meta if meta.endsWith(".meta") =>
          (getProject.getProjectDir / meta.dropRight(5)).exists()
      }
      .map(">>> " + _)
      .foreach {
        asset =>
          s"TODO ; need the copy-asset code for asset ; $asset" halt
      }
}

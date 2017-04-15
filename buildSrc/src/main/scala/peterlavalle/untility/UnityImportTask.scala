package peterlavalle.untility

import org.gradle.api.Project

import scala.collection.JavaConversions._
import org.gradle.api.tasks.TaskAction
import peterlavalle.OverWriter

class UnityImportTask extends TUnityTask {

  @TaskAction
  def action(): Unit = {
    config.links.foreach {
      case link: Project =>

        // TODO ; somehow, strip out the old stuff
        /*
        // delete the old contents
        require(
          (getProject.getProjectDir / "Assets" / link.unityName).unlink,
          s"Failed to delete `${(getProject.getProjectDir / "Assets" / link.unityName).AbsolutePath}/`"
        )

        // ignore whatever forever
        require(
          new OverWriter(getProject.getProjectDir / "Assets" / link.unityName / ".gitignore")
            .appund("*.*\n")
            .appund("*\n")
            .appund("!.gitignore\n")
            .closeFile.exists(),
          s"Failed to ignore all in `${(getProject.getProjectDir / "Assets" / link.unityName).AbsolutePath}/`"
        )
        */

        // use unity to import package
        // TODO ; somehow, import the packages even if they're needed
        invoke(List("-importPackage", link.findTask[UnityExportTask].unityPackage))
    }
  }

}

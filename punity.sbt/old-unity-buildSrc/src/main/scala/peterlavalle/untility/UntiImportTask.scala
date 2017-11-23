package peterlavalle.untility

import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import scala.collection.JavaConversions._

class UntiImportTask extends TUntiTask {
  @TaskAction
  def action(): Unit =
    // import any/all "other" projects
    config.links.foreach {
      case link: Project =>
        invoke(
          task[UntiMakeSpaceTask].makeSpace, List("-importPackage", link.findTask[UntiPackageTask].unityPackage)
        ) match {
          case 0 => ;
        }
    }

}
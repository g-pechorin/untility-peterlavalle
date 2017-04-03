package peterlavalle.untility

import java.util

import org.gradle.api.Project

import scala.beans.BeanProperty

class Config {
  @BeanProperty
  var owner: Project = null

  val links = new util.LinkedList[AnyRef]()

  def setLink(other: Project): Unit = {

    // I depend on youuuuuu ...
    owner.findTask[UnityImportTask].dependsOn(other.findTask[UnityExportTask])

    links.add(other)
  }
}

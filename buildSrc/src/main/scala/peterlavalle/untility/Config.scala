package peterlavalle.untility

import java.util

import org.gradle.api.Project

import scala.beans.BeanProperty

class Config {
  @BeanProperty
  var owner: Project = null

  /**
    * Unity won't import if there are compiler errors ... even when those errors would be fixed by an import
    */
  @BeanProperty
  var batchMode: Boolean = true

  val links = new util.LinkedList[AnyRef]()

  def setLink(other: Project): Unit = {

    // I depend on youuuuuu ...
    owner.findTask[UnityPluginTask].dependsOn(other.findTask[UnityPluginTask])

    links.add(other)
  }
}
